package www.coders.org.qr_fintech_client;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.concurrent.ExecutionException;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SendListFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SendListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SendListFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    EditText result_list;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    public static final String my_shared_preferences = "login_information";

    public SendListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SendListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SendListFragment newInstance(String param1, String param2) {
        SendListFragment fragment = new SendListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_send_list, container, false);

        ListView listView = (ListView)view.findViewById(R.id.send_listView);
        TextView balance = (TextView)view.findViewById(R.id.balance_send);

        SharedPreferences sp = getActivity().getSharedPreferences(my_shared_preferences, Context.MODE_PRIVATE);

        String loginID = sp.getString("id", null);
        String password = sp.getString("pw", null);


        // 잔액 띄우기
        JSONObject jsonObject = new JSONObject();
        try {
            //로그인 아이디 받아오기

            //  jsonObject.accumulate("id", id_text.getText());
            jsonObject.accumulate("id", loginID);
            jsonObject.accumulate("pw", password);
            HttpAsyncTask httpTask = new HttpAsyncTask(jsonObject);

           // String result = httpTask.execute(getContext().getString(R.string.server_ip) + "/send/list/" + loginID).get();
            String result = httpTask.execute(getContext().getString(R.string.server_ip) + "/user/").get();

            JSONObject json = new JSONObject(result);

            String result1 = json.getString("result");

            if (result1.equals("1")) {


                String balance1 = json.getString("balance");

                String formattedBal = moneyFormatToWon(balance1);

                balance.setText(formattedBal + "원");

            } else {

                String msg = json.getString("msg");
                Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();

            }


        } catch (JSONException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();

        }


        //list view



        JSONObject jsonObject2 = new JSONObject();
        try {

            //  jsonObject.accumulate("id", id_text.getText());
            jsonObject2.accumulate("id", loginID);
            jsonObject2.accumulate("pw", password);
            HttpAsyncTask httpTask = new HttpAsyncTask(jsonObject2);

            String result = httpTask.execute(getContext().getString(R.string.server_ip) + "/send/list/" + loginID).get();

            JSONObject json = new JSONObject(result);

            JSONArray rows = json.getJSONArray("rows");

            String result1 = json.getString("result");

            if (result1.equals("1")) {

                ListViewAdapter mAdapter = new ListViewAdapter();

                for(int i = rows.length()-1; i >=0; i--) {

                    String from_user_id = rows.getJSONObject(i).getString("from_user_id");
                    String to_user_id = rows.getJSONObject(i).getString("to_user_id");
                    String date = rows.getJSONObject(i).getString("time");
                    String bal = rows.getJSONObject(i).getString("to_result"); //수정하기
                    String value = rows.getJSONObject(i).getString("value"); //수정하기
                    String from_val = rows.getJSONObject(i).getString("from_result");

                    String state = "";
                    //천의 자리마다 콤마 표시하기
                    String tmp_val = moneyFormatToWon(value);

                    if(from_val.equals("-1")){ // 충전 (채움)
                        String tmp = moneyFormatToWon(bal);
                        to_user_id = "+" + tmp_val;
                        bal = tmp + "원";
                        state = "채움";
                    }
                    else{ //받음 | 보냄

                        if(to_user_id.equals(loginID)){ //받음
                            to_user_id = "+" + tmp_val;
                            String tmp = moneyFormatToWon(bal);
                            bal  = tmp + "원";
                            state = "받음";
                        }
                        else if(from_user_id.equals(loginID)){ //보냄
                            to_user_id = "-" + tmp_val;
                            String tmp = moneyFormatToWon(from_val);
                            bal = tmp + "원";
                            state = "보냄";
                        }


                    }

                    mAdapter.addItem(from_user_id, to_user_id,date, state, bal);
                }

                listView.setAdapter(mAdapter);



            } else {

                String msg = json.getString("msg");
                Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();

            }


        } catch (JSONException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();

        }

        return view;
    }

    //천의 자리마다 콤마 표시하는 메소드
    public static String moneyFormatToWon(String inputMoney) {
        NumberFormat nf = NumberFormat.getNumberInstance();
        String formattedMoney = (String)nf.format(Integer.parseInt(inputMoney));
        return formattedMoney;
    }

}
