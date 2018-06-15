package www.coders.org.qr_fintech_client;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserShowQRActivity extends AppCompatActivity {

    public static final String my_shared_preferences = "login_information";
    public static final String QR_URL = "http://49.173.132.147:9999/qrcode/";
    SharedPreferences sharedpreferences;
    private String id, name, password;
    public final static String TAG_ID = "id";
    public final static String TAG_NAME = "name";
    public final static String TAG_PASSWORD = "pw";

    private ProgressDialog pDialog;

    Bitmap qrImage = null;

    EditText userQrNumber, userId;
    TextView showUserQR;
    ImageView QrImage;
    Button cancelButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_show_qr);

        sharedpreferences = getSharedPreferences(my_shared_preferences, Context.MODE_PRIVATE);
        id = sharedpreferences.getString(TAG_ID, null);
        name = sharedpreferences.getString(TAG_NAME, null);
        id = sharedpreferences.getString(TAG_ID, null);
        password = sharedpreferences.getString(TAG_PASSWORD, null);

        showUserQR = (TextView) findViewById(R.id.show_user_QR);
        userId = (EditText) findViewById(R.id.user_show_id);
        userId.setFocusable(false);
        userId.setClickable(false);
        QrImage = (ImageView) findViewById(R.id.user_show_qrCode);
        cancelButton = (Button) findViewById(R.id.user_show_cancel_button);

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                UserShowQRActivity.super.onBackPressed();
                /*Intent intent = new Intent(UserShowQRActivity.this, MainScreenActivity.class);
                finish();
                startActivity(intent);*/
            }
        });

        showUserQR.setText(name + "님의 QR코드");
        userId.setText(id); //user id 등록
        qrCreate(id, password);
    }


    private void qrCreate(final String id, final String password) {
        //로딩창 띄우기
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        pDialog.setMessage("로그인 중 ...");
        pDialog.show();

        NetRetrofit.getEncryptQr().do_qrCode(id, password).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    Log.e("qrCreate", "Success");
                    if (response.body() != null) {
                        // display the image data in a ImageView or save it
                        Bitmap bitmap = BitmapFactory.decodeStream(response.body().byteStream());
                        QrImage.setImageBitmap(bitmap);
                        pDialog.dismiss();
                    } else {
                        Log.e("qrCreate", "Failed");
                        Toast.makeText(getApplicationContext(), "지금은 서버 점검중입니다.", Toast.LENGTH_LONG).show();
                        pDialog.dismiss();
                    }

                } else {
                    Log.e("qrCreate", "Failed");
                    Toast.makeText(getApplicationContext(), "지금은 서버 점검중입니다.", Toast.LENGTH_LONG).show();
                    pDialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("qrCreate Server", "Failed");
                Toast.makeText(getApplicationContext(), "지금은 서버 점검중입니다.", Toast.LENGTH_LONG).show();
                pDialog.dismiss();
            }
        });
    }
}