package www.coders.org.qr_fintech_client;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    ProgressDialog pDialog;
    Button btn_register, btn_login;
    EditText txt_id, txt_password, txt_confirm_password, txt_name, txt_nickname, txt_phone, txt_about;
    ImageView image_profile;
    Intent intent;

    String imageString;
    String imagePath = null;
    Bitmap bitmap;
    ByteArrayOutputStream baos;


    static final int PICK_FROM_CAMERA = 0;
    static final int PICK_FROM_ALBUM = 1;
    static final int CROP_FROM_IMAGE = 2;
    Uri uri;

    int success;
    ConnectivityManager conMgr;

    private String url;

    private static final String TAG = RegisterActivity.class.getSimpleName();

    private static final String TAG_SUCCESS = "result";
    private static final String TAG_MESSAGE = "msg";

    String tag_json_obj = "json_obj_req";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //인터넷 연결 체크
        conMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        {
            if (conMgr.getActiveNetworkInfo() != null
                    && conMgr.getActiveNetworkInfo().isAvailable()
                    && conMgr.getActiveNetworkInfo().isConnected()) {
            } else {
                Toast.makeText(getApplicationContext(), "인터넷 연결 상태를 확인해주세요.",
                        Toast.LENGTH_LONG).show();
            }
        }
        Context context = this;
        url = context.getString(R.string.server_ip) + "/mobile/user_insert";

        btn_login = (Button) findViewById(R.id.btn_login);
        btn_register = (Button) findViewById(R.id.btn_register);
        txt_id = (EditText) findViewById(R.id.txt_id);
        txt_password = (EditText) findViewById(R.id.txt_password);
        txt_confirm_password = (EditText) findViewById(R.id.txt_confirm_password);
        txt_name = (EditText) findViewById(R.id.txt_name);
        txt_nickname = (EditText) findViewById(R.id.txt_nickname);
        txt_phone = (EditText) findViewById(R.id.txt_phone);
        txt_phone.addTextChangedListener(new PhoneNumberFormattingTextWatcher());
        txt_about = (EditText) findViewById(R.id.txt_about);
        image_profile = (ImageView)findViewById(R.id.profile_image);
        image_profile.setOnClickListener(this);
        btn_login.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                intent = new Intent(RegisterActivity.this, MainActivity.class);
                finish();
                startActivity(intent);
            }
        });

        btn_register.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                final String id = txt_id.getText().toString();
                final String password = txt_password.getText().toString();
                String confirm_password = txt_confirm_password.getText().toString();

                if (conMgr.getActiveNetworkInfo() != null
                        && conMgr.getActiveNetworkInfo().isAvailable()
                        && conMgr.getActiveNetworkInfo().isConnected()) {
                    if(password.compareTo(confirm_password)==0){
                        checkRegister(id, password);
                    }
                    else{
                        Toast.makeText(getApplicationContext(), "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "인터넷 연결 상태를 확인해주세요.", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }


    private void checkRegister(final String id, final String password)  {
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        pDialog.setMessage("계정 등록중 ...");
        showDialog();

        ///
        Map<String, String> params = new HashMap<String, String>();
        params.put("id", id);
        params.put("pw", password);
        params.put("name", txt_name.getText().toString());
        params.put("nickname", txt_nickname.getText().toString());
        params.put("phone_number", txt_phone.getText().toString());
        params.put("about", txt_about.getText().toString());
        params.put("balance", String.valueOf(0));

        Map<String, String> files = new HashMap<String, String>();

        if(imagePath!=null){
            files.put("img",imagePath);
        }

        JSONObject jObj = new JSONObject();
        try {
            MultipartUpload multipartUpload = new MultipartUpload(url,"UTF-8");
            jObj = multipartUpload.execute(params,files).get();

            success = jObj.getInt(TAG_SUCCESS);

            if (success == 1) {

                Log.e("Successfully Register!", jObj.toString());

                Toast.makeText(getApplicationContext(),
                        "계정 등록이 완료되었습니다.", Toast.LENGTH_LONG).show();

                txt_id.setText("");
                txt_password.setText("");
                txt_confirm_password.setText("");
                txt_name.setText("");
                txt_nickname.setText("");
                txt_phone.setText("");
                txt_about.setText("");

                intent = new Intent(RegisterActivity.this, MainActivity.class);
                finish();
                startActivity(intent);

            } else {
                Toast.makeText(getApplicationContext(),
                        jObj.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

    @Override
    public void onBackPressed() {
        intent = new Intent(RegisterActivity.this, MainActivity.class);
        finish();
        startActivity(intent);
    }

    void doTakeAlbumAction()
    {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
        startActivityForResult(intent,PICK_FROM_ALBUM);
    }

    private void sendPicture(Uri imgUri) {

        imagePath = getRealPathFromURI(imgUri); // path 경로
        ExifInterface exif = null;
        try {
            exif = new ExifInterface(imagePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        int exifOrientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
        int exifDegree = exifOrientationToDegrees(exifOrientation);

        bitmap = BitmapFactory.decodeFile(imagePath);//경로를 통해 비트맵으로 전환
        image_profile.setImageBitmap(rotate(bitmap, exifDegree));//이미지 뷰에 비트맵 넣기
        //baos = new ByteArrayOutputStream();
        //bitmap.compress(Bitmap.CompressFormat.JPEG,100,baos);
        //byte[] imageBytes = baos.toByteArray();
        //imageString = Base64.encodeToString(imageBytes,Base64.DEFAULT);

    }

    private int exifOrientationToDegrees(int exifOrientation) {
        if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_90) {
            return 90;
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_180) {
            return 180;
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_270) {
            return 270;
        }
        return 0;
    }

    private Bitmap rotate(Bitmap src, float degree) {
        // Matrix 객체 생성
        Matrix matrix = new Matrix();
        // 회전 각도 셋팅
        matrix.postRotate(degree);
        // 이미지와 Matrix 를 셋팅해서 Bitmap 객체 생성
        return Bitmap.createBitmap(src, 0, 0, src.getWidth(),
                src.getHeight(), matrix, true);
    }

    private String getRealPathFromURI(Uri contentUri) {
        int column_index=0;
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
        if(cursor.moveToFirst()){
            column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        }

        return cursor.getString(column_index);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode != RESULT_OK)
            return;
        switch(requestCode)
        {
            case PICK_FROM_ALBUM:
            {
                sendPicture(data.getData()); //갤러리에서 가져오기
                break;
            }
        }
    }

    DialogInterface.OnClickListener albumListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            doTakeAlbumAction();
        }
    };

    DialogInterface.OnClickListener cancelListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            dialog.dismiss();
        }
    };
    @Override
    public void onClick(View v) {
        new AlertDialog.Builder(RegisterActivity.this).setTitle("업로드할 이미지 선택")
                .setPositiveButton("앨범선택",albumListener)
                .setNegativeButton("취소",cancelListener)
                .show();


    }

}
