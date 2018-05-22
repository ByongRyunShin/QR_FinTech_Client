package www.coders.org.qr_fintech_client;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

public class ManageProductSuperDetail extends AppCompatActivity {


    Intent intent;
    Button delete_button;
    Button modify_button;
    int mode;
    String num, pnum, userid, userpw, delete_button_str;
    EditText name_editText;
    EditText place_editText;
    EditText price_editText;
    EditText about_editText;
    Button product_button;

    private String PATH_READ;
    private String PATH_APPLY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_product_super_detail);

        intent = getIntent();
        num = intent.getStringExtra("num");
        pnum = intent.getStringExtra("pnum");
        userid = intent.getStringExtra("id");
        userpw = intent.getStringExtra("pw");
        place_editText = findViewById(R.id.place_editText);
        place_editText.setText(intent.getStringExtra("name"));

        Context context = this;


        PATH_READ = context.getString(R.string.server_ip) + "/product_insert";
        PATH_APPLY = context.getString(R.string.server_ip) + "/product_detail";
        readProductInfo();
    }




    private void readProductInfo()
    {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.accumulate("id", userid);// 아이디 비번 받아와야함
            jsonObject.accumulate("pw", userpw);
            jsonObject.accumulate("num", num);
            jsonObject.accumulate("pnum", pnum);
            HttpAsyncTask httpTask = new HttpAsyncTask(jsonObject);
            String result = httpTask.execute(PATH_READ).get();

            // Log.e("hihi3333",result);

            JSONObject store = new JSONObject(result);
            int r = Integer.parseInt(store.getString("result"));
            switch (r)
            {
                case 1:
                    name_editText.setText(store.getString("name"));
                    about_editText.setText(store.getString("about"));
                    price_editText.setText(store.getString("price"));
                    break;
                case -1:
                    Toast.makeText(getApplicationContext(), "로그인 되어있지 않습니다.", Toast.LENGTH_LONG).show();
                    finish();
                case -2:
                    Toast.makeText(getApplicationContext(), "조회 불가능 : 잘못된 상점번호입니다.", Toast.LENGTH_LONG).show();
                    finish();
                case -3:
                    Toast.makeText(getApplicationContext(), "조회 불가능 : 잘못된 물건번호입니다.", Toast.LENGTH_LONG).show();
                    finish();
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
