package www.coders.org.qr_fintech_client;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

public class SendMoneyFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    EditText money_text, id_text, pw_text, send_to_text;
    Button send_btn;
    TextView text_result;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SendMoneyFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SendMoneyFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SendMoneyFragment newInstance(String param1, String param2) {
        SendMoneyFragment fragment = new SendMoneyFragment();
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

        View view = inflater.inflate(R.layout.fragment_send_money, container, false);
        // Inflate the layout for this fragment
        send_to_text = (EditText)view.findViewById(R.id.send_id_text);
        id_text = (EditText)view.findViewById(R.id.my_id_text);
        money_text = (EditText)view.findViewById(R.id.send_money_text);
        pw_text = (EditText)view.findViewById(R.id.send_password);

        text_result = (TextView)view.findViewById(R.id.send_result) ;

        send_btn = (Button)view.findViewById(R.id.send_btn);



        send_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                JSONObject jsonObject = new JSONObject();
                try {


                    jsonObject.accumulate("id", id_text.getText());
                    jsonObject.accumulate("pw", pw_text.getText());
                    jsonObject.accumulate("cost", money_text.getText());
                    HttpAsyncTask httpTask = new HttpAsyncTask(jsonObject);


                    String result = httpTask.execute(getContext().getString(R.string.server_ip) + "/send/" + id_text.getText() + "$"+ send_to_text.getText()).get();


                    JSONObject json = new JSONObject(result);

                    String result1 = json.getString("result");

                    if(result1.equals("1")){

                        String balance = json.getString("balance");

                        String cost = json.getString("cost");

                        Toast.makeText(getActivity(), cost + "원 " +"송금이 완료되었습니다. 잔액: " + balance + "원", Toast.LENGTH_SHORT).show();

                        text_result.setText(cost + "원 " +"송금, 잔액: " + balance + "원");


                    }

                    else {

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
            }
        });

        return view;
    }

}
