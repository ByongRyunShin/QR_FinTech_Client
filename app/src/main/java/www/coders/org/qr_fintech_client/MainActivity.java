package www.coders.org.qr_fintech_client;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

       //startActivity(new Intent(MainActivity.this, SendMoneyActivity.class));
        startActivity(new Intent(MainActivity.this, MainScreenActivity.class));
    }
}
