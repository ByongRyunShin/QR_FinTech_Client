package www.coders.org.qr_fintech_client;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

public class ManageShopDetail extends AppCompatActivity {
    Intent intent;
    Button delete_button;
    Button modify_button;
    int mode;
    String num, userid, userpw, delete_button_str;
    EditText name_editText;
    EditText balance_editText;
    EditText place_editText;
    EditText about_editText;
    Button product_button;
    private String PATH_READ;
    private String PATH_APPLY;
    private String PATH_DELETE ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_shop_detail);
        name_editText = findViewById(R.id.name_editText);
        balance_editText = findViewById(R.id.balance_editText);
        place_editText = findViewById(R.id.place_exitText);
        about_editText = findViewById(R.id.about_editText);

        delete_button = (Button) findViewById(R.id.cancel_button);
        delete_button.setOnClickListener(mDeleteClickListener);

        modify_button = (Button) findViewById(R.id.modify_button);
        modify_button.setOnClickListener(mModifyClickListener);

        product_button = (Button) findViewById(R.id.product_button);
        product_button.setOnClickListener(mProductClickListener);


        Context context = this;
        PATH_READ = context.getString(R.string.server_ip) + "/shop_detail";
        PATH_APPLY = context.getString(R.string.server_ip) + "/shop_insert";
        PATH_DELETE = context.getString(R.string.server_ip) + "/shop_delete";

        intent = getIntent();
        mode = intent.getIntExtra("mode", 0);
        userid = intent.getStringExtra("id");
        userpw = intent.getStringExtra("pw");

        switch (mode)
        {
            case ManageShopFragment.MODE_MODIFY:
                num = intent.getStringExtra("item");
                delete_button_str = "삭제";
                readStoreInfo();
                break;

            case ManageShopFragment.MODE_APPLY:
                delete_button_str = "취소";
                delete_button.setText(delete_button_str);
                break;

        }

    }

    View.OnClickListener mProductClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(getApplicationContext(), ManageProductDetail.class);
            intent.putExtra("num", num);
            intent.putExtra("name", name_editText.getText().toString());// 이름을 넘겨받게 수정해야함
            intent.putExtra("id", userid);
            intent.putExtra("pw", userpw);
            startActivity(intent);
        }
    };

    View.OnClickListener mDeleteClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            new AlertDialog.Builder(ManageShopDetail.this)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle("Closing Activity")
                    .setMessage(delete_button_str + "하시겠습니까?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (mode == ManageShopFragment.MODE_MODIFY)
                            {
                                deleteStoreInfo();
                            }
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener(){
                        @Override
                        public void onClick(DialogInterface dialog, int which){
                            //finish();
                        }
                    })
                    .show();
        }
    };

    View.OnClickListener mModifyClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (mode)
            {
                case ManageShopFragment.MODE_APPLY:
                    applyStoreInfo();
                    break;
                case ManageShopFragment.MODE_MODIFY:
                    modifyStoreInfo();
                    break;
            }
            finish();
        }
    };

    public void onBackPressed() {
        // TODO Auto-generated method stub
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Closing Activity")
                .setMessage("변경된 내용을 저장하시겠습니까??")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (mode)
                        {
                            case ManageShopFragment.MODE_APPLY:
                                applyStoreInfo();
                                break;
                            case ManageShopFragment.MODE_MODIFY:
                                modifyStoreInfo();
                                break;
                        }
                        finish();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which){
                        finish();
                    }
                })
                .show();
    }

    private int applyStoreInfo()
    {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.accumulate("id", userid);// 아이디 비번 받아와야함
            jsonObject.accumulate("pw", userpw);
            jsonObject.accumulate("name", name_editText.getText().toString());
            jsonObject.accumulate("about", about_editText.getText().toString());
            HttpAsyncTask httpTask = new HttpAsyncTask(jsonObject);
            String result = httpTask.execute(PATH_APPLY).get();

            // Log.e("hihi3333",result);

            JSONObject store = new JSONObject(result);
            int r = Integer.parseInt(store.getString("result"));
            switch (r)
            {
                case 1:
                    Toast.makeText(getApplicationContext(), "등록 완료!", Toast.LENGTH_LONG).show();
                    finish();
                case -1:
                    Toast.makeText(getApplicationContext(), "로그인 되어있지 않습니다.", Toast.LENGTH_LONG).show();
                    finish();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return -3;
    }

    private void modifyStoreInfo()
    {
        Toast.makeText(getApplicationContext(), "수정은 불가능.", Toast.LENGTH_LONG).show();
        // 방법으로는 수정 불가능.
        finish();
    }

    private void deleteStoreInfo()
    {
        Log.e("hihi","delete");
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.accumulate("id", userid);// 아이디 비번 받아와야함
            jsonObject.accumulate("pw", userpw);
            jsonObject.accumulate("num", num);
            HttpAsyncTask httpTask = new HttpAsyncTask(jsonObject);
            String result = httpTask.execute(PATH_DELETE).get();

            // Log.e("hihi3333",result);

            JSONObject store = new JSONObject(result);
            int r = Integer.parseInt(store.getString("result"));

            switch (r)
            {
                case 1:
                    Toast.makeText(getApplicationContext(), "삭제 완료!", Toast.LENGTH_LONG).show();
                    finish();
                case -1:
                    Toast.makeText(getApplicationContext(), "로그인 되어있지 않습니다.", Toast.LENGTH_LONG).show();
                    finish();
                case -2:
                    Toast.makeText(getApplicationContext(), "삭제 불가능 : 잔액이 남아있습니다.", Toast.LENGTH_LONG).show();
                    break;
                case -3:
                    Toast.makeText(getApplicationContext(), "삭제 불가능 : 잘못된 상점번호입니다.", Toast.LENGTH_LONG).show();
                    break;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    private void readStoreInfo()
    {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.accumulate("id", userid);// 아이디 비번 받아와야함
            jsonObject.accumulate("pw", userpw);
            jsonObject.accumulate("num", num);
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
                    balance_editText.setText(store.getString("balance"));
                    break;
                case -1:
                    Toast.makeText(getApplicationContext(), "로그인 되어있지 않습니다.", Toast.LENGTH_LONG).show();
                    finish();
                case -2:
                    Toast.makeText(getApplicationContext(), "조회 불가능 : 잘못된 상점번호입니다.", Toast.LENGTH_LONG).show();
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