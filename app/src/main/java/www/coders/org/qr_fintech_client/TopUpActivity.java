package www.coders.org.qr_fintech_client;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

public class TopUpActivity extends AppCompatActivity {

    EditText money_text, id_text, pw_text;
    Button topup_btn;
    TextView text_result;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top_up);


        id_text = (EditText)findViewById(R.id.id_text);
        money_text = (EditText)findViewById(R.id.money_text);
        pw_text = (EditText)findViewById(R.id.password);

        text_result = (TextView)findViewById(R.id.text_result) ;

        topup_btn = (Button)findViewById(R.id.topup_btn);


        topup_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                JSONObject jsonObject = new JSONObject();
                try {


                    jsonObject.accumulate("id", id_text.getText());
                    jsonObject.accumulate("pw", pw_text.getText());
                    jsonObject.accumulate("cost", money_text.getText());
                    HttpAsyncTask httpTask = new HttpAsyncTask(jsonObject);



                    //만약 상점 리스트에서 넘어왔다면, 상점 충전
                    //shop number 받아오기
                    //String shopNum = "7";

                    //String result = httpTask.execute("http://192.168.0.2:3000/mobile/charge/" + id_text.getText() + "^" + shopNum).get();



                    //개인 계좌에 충전하기

                    String result = httpTask.execute(R.string.server_ip + "/mobile/charge/" + id_text.getText()).get();


                    JSONObject json = new JSONObject(result);

                    String result1 = json.getString("result");

                    if(result1.equals("1")){

                        String balance = json.getString("balance");

                        Toast.makeText(TopUpActivity.this, "충전이 완료되었습니다. 잔액: " + balance + "원", Toast.LENGTH_SHORT).show();

                        text_result.setText("잔액: " + balance + "원");


                    }
                    else {

                        String msg = json.getString("msg");
                        Toast.makeText(TopUpActivity.this, msg, Toast.LENGTH_SHORT).show();

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



    }



}
