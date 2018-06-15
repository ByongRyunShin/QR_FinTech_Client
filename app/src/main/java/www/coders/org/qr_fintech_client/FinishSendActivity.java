package www.coders.org.qr_fintech_client;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class FinishSendActivity extends AppCompatActivity {

    Button sendOk;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finish_send);

        sendOk = (Button)findViewById(R.id.ok_send_btn);
        sendOk.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v){
                Intent intent = new Intent(FinishSendActivity.this, MainScreenActivity.class);
                finish();
                startActivity(intent);
            }
        });

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(FinishSendActivity.this, MainScreenActivity.class);
        finish();
        startActivity(intent);
    }
}
