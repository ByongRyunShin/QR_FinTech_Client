package www.coders.org.qr_fintech_client;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {

    ProgressDialog pDialog;
    Button btn_register, btn_login;
    EditText txt_id, txt_password;
    Intent intent;

    int success;
    ConnectivityManager conMgr; // Network 연결 확인 class

    private String url;

    //json tag--------------------------------------------

    private static final String TAG_RESULT = "result";
    private static final String TAG_MESSAGE = "msg";

    public final static String TAG_ID = "id";
    public final static String TAG_PASSWORD = "pw";
    public final static String TAG_TYPE = "type"; // 개인 0, 상인 1
    //------------------------------

    String tag_json_obj = "json_obj_req";

    SharedPreferences sharedpreferences; //안드로이드 Data 저장 class
    Boolean session = false;
    String id, type ;
    public static final String my_shared_preferences = "my_shared_preferences";
    public static final String session_status = "session_status";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        conMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        {
            if (conMgr.getActiveNetworkInfo() != null
                    && conMgr.getActiveNetworkInfo().isAvailable()
                    && conMgr.getActiveNetworkInfo().isConnected()) {
            } else {
                Toast.makeText(getApplicationContext(), "인터넷 연결을 확인해주세요",
                        Toast.LENGTH_LONG).show();
            }
        }

        Context context = this;
        url= context.getString(R.string.server_ip) +"/login";

        btn_login = (Button) findViewById(R.id.btn_login);
        btn_register = (Button) findViewById(R.id.btn_register);
        txt_id = (EditText) findViewById(R.id.txt_id);
        txt_password = (EditText) findViewById(R.id.txt_password);

        sharedpreferences = getSharedPreferences(my_shared_preferences, Context.MODE_PRIVATE);
        session = sharedpreferences.getBoolean(session_status, false);
        id = sharedpreferences.getString(TAG_ID, null);
        type = sharedpreferences.getString(TAG_TYPE, null);

        if (session) {
            Intent intent = new Intent(MainActivity.this, MainScreenActivity.class);
            intent.putExtra(TAG_ID, id);
            //intent.putExtra(TAG_TYPE, type);
            finish();
            startActivity(intent);
        }


        btn_login.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                String id = txt_id.getText().toString();
                String password = txt_password.getText().toString();

                // 입력 공란 체크
                if (id.trim().length() > 0 && password.trim().length() > 0) {
                    if (conMgr.getActiveNetworkInfo() != null
                            && conMgr.getActiveNetworkInfo().isAvailable()
                            && conMgr.getActiveNetworkInfo().isConnected()) {
                        checkLogin(id, password);
                    } else {
                        Toast.makeText(getApplicationContext() ,"인터넷 연결을 확인해 주세요", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext() ,"아이디와 비밀번호를 확인해주세요", Toast.LENGTH_LONG).show();
                }
            }
        });

        btn_register.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                intent = new Intent(MainActivity.this, RegisterActivity.class);
                finish();
                startActivity(intent);
            }
        });

    }

    // 네트워크 연결했을때
    private void checkLogin(final String id, final String password) {
        //로딩창 띄우기
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        pDialog.setMessage("로그인 중 ...");
        showDialog();
        JSONObject jObj = null;

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.accumulate(TAG_ID, id);
            jsonObject.accumulate(TAG_PASSWORD, password);

            HttpAsyncTask httpTask = new HttpAsyncTask(jsonObject);
            String response = httpTask.execute(url).get();
            jObj = new JSONObject(response);
            int success = jObj.getInt(TAG_RESULT);

            if(success == 1)
            {
                // 세션 값, id, 안드로이드에 data 저장
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putBoolean(session_status, true);
                editor.putString(TAG_ID, id);
                //editor.putString(TAG_TYPE, type);
                editor.commit();
                hideDialog();

                // 로그인 후 화면 열기 id  반환
                Toast.makeText(getApplicationContext(), id + "님 환영합니다.", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(MainActivity.this, MainScreenActivity.class);
                intent.putExtra(TAG_ID, id);
                //intent.putExtra(TAG_TYPE, type);
                finish();
                startActivity(intent);
            }
            else
            {
                Toast.makeText(getApplicationContext(),jObj.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(),"로그인 에러", Toast.LENGTH_LONG).show();
            hideDialog();
        } catch (InterruptedException e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(),"로그인 에러", Toast.LENGTH_LONG).show();
            hideDialog();
        } catch (ExecutionException e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(),"로그인 에러", Toast.LENGTH_LONG).show();
            hideDialog();
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
}
