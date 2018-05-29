package www.coders.org.qr_fintech_client;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.InputStream;

public class UserShowQRActivity extends AppCompatActivity {

    public static final String my_shared_preferences = "login_information";
    public static final String QR_URL = "http://49.173.132.147:9999/qrcode/";
    static String qrNumber = null;
    SharedPreferences sharedpreferences;
    private String id;
    public final static String TAG_ID = "id";
    Bitmap qrImage = null;

    EditText userQrNumber, userId;
    TextView showUserQR;
    ImageView QrImage;
    Button cancelButton;
    Boolean Image_load = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_show_qr);

        sharedpreferences = getSharedPreferences(my_shared_preferences, Context.MODE_PRIVATE);
        id = sharedpreferences.getString(TAG_ID, null);

        showUserQR = (TextView)findViewById(R.id.show_user_QR);
        userQrNumber = (EditText)findViewById(R.id.user_show_qrNumber);
        userQrNumber.setFocusable(false); userQrNumber.setClickable(false);
        userId = (EditText)findViewById(R.id.user_show_id);
        userId.setFocusable(false); userId.setClickable(false);
        QrImage = (ImageView)findViewById(R.id.user_show_qrCode) ;
        cancelButton = (Button)findViewById(R.id.user_show_cancel_button);

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                UserShowQRActivity.super.onBackPressed();
                finish();
            }
        });

        new DownloadImageTask(UserShowQRActivity.this,QrImage).execute(QR_URL+id); //Qr이미지 등록
        showUserQR.setText(id+"님의 QR코드");
        userId.setText(id); //user id 등록
    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;
        private ProgressDialog dialog;

        public DownloadImageTask(UserShowQRActivity activity, ImageView bmImage) {
            this.bmImage = bmImage;
            dialog = new ProgressDialog(activity);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog.setMessage("잠시만 기다려주세요...");
            dialog.show();
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
        }

        protected Bitmap doInBackground(String... urls) {
            String url = urls[0];
            Bitmap down_Image = null;
            try {
                InputStream in = new java.net.URL(url).openStream();
                down_Image = BitmapFactory.decodeStream(in);
                bmImage.setImageBitmap(down_Image);
                qrImage = down_Image;

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        qrNumber = qrReader.readQRImage(qrImage);
                        userQrNumber.setText(qrNumber); //QR 번호 등록
                    }
                });
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return down_Image;
        }

    }
}
