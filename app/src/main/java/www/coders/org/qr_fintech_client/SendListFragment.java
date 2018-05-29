package www.coders.org.qr_fintech_client;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

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

    TextView result_list;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    public static final String my_shared_preferences = "my_shared_preferences";


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

        result_list = (TextView) view.findViewById(R.id.result_send_list);


        JSONObject jsonObject = new JSONObject();
        try {
            //로그인 아이디 받아오기
            SharedPreferences sp = getActivity().getSharedPreferences(my_shared_preferences, Context.MODE_PRIVATE);

            String loginID = sp.getString("id", null);
            String password = sp.getString("pw", null);
            //  jsonObject.accumulate("id", id_text.getText());
            jsonObject.accumulate("id", loginID);
            jsonObject.accumulate("pw", password);
            HttpAsyncTask httpTask = new HttpAsyncTask(jsonObject);

            String result = httpTask.execute(getContext().getString(R.string.server_ip) + "/send/list/" + loginID).get();


            JSONObject json = new JSONObject(result);

            String result1 = json.getString("result");

            if (result1.equals("1")) {

               /* String balance = json.getString("balance");

                String cost = json.getString("cost");

                Toast.makeText(getActivity(), cost + "원 " + "송금이 완료되었습니다. 잔액: " + balance + "원", Toast.LENGTH_SHORT).show();
*/
                result_list.setText(result);

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
        //return inflater.inflate(R.layout.fragment_send_list, container, false);
    }


}
