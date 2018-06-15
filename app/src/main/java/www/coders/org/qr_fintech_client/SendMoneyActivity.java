package www.coders.org.qr_fintech_client;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

public class SendMoneyActivity extends AppCompatActivity implements View.OnClickListener{


    public static final String my_shared_preferences = "login_information";

    EditText money_text, id_text, pw_text, send_to_text;
    Button send_btn, ok_send_btn;
    TextView text_result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_money);


//        View view = inflater.inflate(R.layout.fragment_send_money, container, false);
 //       sendMoneyFragmentView = view;
        // Inflate the layout for this fragment
//        getActivity().setTitle("               송금 하기");

        send_to_text = (EditText)findViewById(R.id.send_id_text);
        //id_text = (EditText)view.findViewById(R.id.my_id_text);
        money_text = (EditText)findViewById(R.id.send_money_text);
        pw_text = (EditText)findViewById(R.id.send_password);

        send_btn = (Button)findViewById(R.id.send_btn);


        send_btn.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        JSONObject jsonObject = new JSONObject();
        try {

            SharedPreferences sp = this.getSharedPreferences(my_shared_preferences, Context.MODE_PRIVATE);
            String loginID = sp.getString("id", null);

            jsonObject.accumulate("id", loginID);
            jsonObject.accumulate("pw", pw_text.getText());
            jsonObject.accumulate("cost", money_text.getText());
            HttpAsyncTask httpTask = new HttpAsyncTask(jsonObject);

            String result = httpTask.execute(this.getString(R.string.server_ip) + "/send/" + loginID + "$"+ send_to_text.getText()).get();

            //String result = httpTask.execute(getContext().getString(R.string.server_ip) + "/send/" + id_text.getText() + "$"+ send_to_text.getText()).get();


            JSONObject json = new JSONObject(result);

            String result1 = json.getString("result");

            if(result1.equals("1")){

                String balance = json.getString("balance");

                String cost = json.getString("cost");

                Toast.makeText(getApplicationContext(), cost + "원 " +"송금이 완료되었습니다. 잔액: " + balance + "원", Toast.LENGTH_SHORT).show();

              //  text_result.setText(cost + "원 " +"송금, 잔액: " + balance + "원");
                send_to_text.setText("");
                money_text.setText("");
                pw_text.setText("");


                Intent intent = new Intent(SendMoneyActivity.this, FinishSendActivity.class);
                finish();
                startActivity(intent);

            }

            else {

                String msg = json.getString("msg");
                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();

            }



        } catch (JSONException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();

        }

    }
}
