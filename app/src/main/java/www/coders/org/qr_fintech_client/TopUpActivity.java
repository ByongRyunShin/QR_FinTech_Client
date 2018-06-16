package www.coders.org.qr_fintech_client;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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

    EditText money_text, pw_text;
    Button topup_btn;
    TextView text_result;


    public static final String my_shared_preferences = "login_information";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top_up);


        money_text = (EditText)findViewById(R.id.money_text);
        pw_text = (EditText)findViewById(R.id.password);

     //   text_result = (TextView)findViewById(R.id.text_result) ;

        topup_btn = (Button)findViewById(R.id.topup_btn);



        final Context context = this;

        topup_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                JSONObject jsonObject = new JSONObject();
                try {

                    //기존 로그인 된 정보를 불러오기
                    SharedPreferences sp = getSharedPreferences(my_shared_preferences, Context.MODE_PRIVATE);

                    String loginID = sp.getString("id", null);

                   // jsonObject.accumulate("id", id_text.getText());
                    jsonObject.accumulate("id", loginID);
                    jsonObject.accumulate("pw", pw_text.getText());
                    jsonObject.accumulate("cost", money_text.getText());
                    HttpAsyncTask httpTask = new HttpAsyncTask(jsonObject);



                    //만약 상점 리스트에서 넘어왔다면, 상점 충전
                    //shop number 받아오기
                    //String shopNum = "7";

                    //String result = httpTask.execute("http://192.168.0.2:3000/mobile/charge/" + id_text.getText() + "^" + shopNum).get();



                    //개인 계좌에 충전하기

                    String result = httpTask.execute(context.getString(R.string.server_ip) + "/charge/" + loginID).get();


                    JSONObject json = new JSONObject(result);

                    String result1 = json.getString("result");

                    if(result1.equals("1")){ //충전 성공했을 경우

                        String balance = json.getString("balance");

                        Toast.makeText(TopUpActivity.this, "충전이 완료되었습니다. 잔액: " + balance + "원", Toast.LENGTH_SHORT).show();

                      //  text_result.setText("잔액: " + balance + "원");

                        money_text.setText("");
                        pw_text.setText("");

                        Intent intent = new Intent(TopUpActivity.this, FinishTopupActivity.class);
                        finish();
                        startActivity(intent);

                    }
                    else { //충전 실패했을 경우

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

    @Override
    public void onBackPressed() {
        //Intent intent = new Intent(TopUpActivity.this, MainScreenActivity.class);
        finish();
        //startActivity(intent);
    }

}
