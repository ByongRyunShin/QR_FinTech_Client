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

public class SendMoneyActivity extends AppCompatActivity {

    EditText money_text, id_text, pw_text, send_to_text;
    Button send_btn;
    TextView text_result;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_money);

        send_to_text = (EditText)findViewById(R.id.send_id_text);
        id_text = (EditText)findViewById(R.id.my_id_text);
        money_text = (EditText)findViewById(R.id.send_money_text);
        pw_text = (EditText)findViewById(R.id.send_password);

        text_result = (TextView)findViewById(R.id.send_result) ;

        send_btn = (Button)findViewById(R.id.send_btn);


        send_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                JSONObject jsonObject = new JSONObject();
                try {


                    jsonObject.accumulate("id", id_text.getText());
                    jsonObject.accumulate("pw", pw_text.getText());
                    jsonObject.accumulate("cost", money_text.getText());
                    HttpAsyncTask httpTask = new HttpAsyncTask(jsonObject);


                    String result = httpTask.execute("http://192.168.0.2:3000/mobile/send/" + id_text.getText() + "$"+ send_to_text.getText()).get();


                    JSONObject json = new JSONObject(result);

                    String result1 = json.getString("result");

                    if(result1.equals("1")){

                        String balance = json.getString("balance");

                        String cost = json.getString("cost");

                        Toast.makeText(SendMoneyActivity.this, cost + "원 " +"송금이 완료되었습니다. 잔액: " + balance + "원", Toast.LENGTH_SHORT).show();

                        text_result.setText(cost + "원 " +"송금, 잔액: " + balance + "원");


                    }

                    else {

                        String msg = json.getString("msg");
                        Toast.makeText(SendMoneyActivity.this, msg, Toast.LENGTH_SHORT).show();

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

