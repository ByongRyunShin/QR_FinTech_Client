package www.coders.org.qr_fintech_client;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

public class ManageProductDetail extends AppCompatActivity {


    Intent intent;
    Button delete_button;
    Button modify_button;
    Button select_button;
    int mode;
    String num, pNum, delete_button_str, name;
    EditText name_editText;
    EditText place_editText;
    EditText price_editText;
    EditText about_editText;
    Button product_button;
    public static final String my_shared_preferences = "login_information";

    private String PATH_READ, PATH_CREATE, PATH_DELETE, PATH_UPDATE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_product_detail);


        PATH_READ = getString(R.string.server_ip) + "/product_detail";
        PATH_CREATE = getString(R.string.server_ip) + "/product_insert";
        PATH_DELETE = getString(R.string.server_ip) + "/product_delete";
        PATH_UPDATE = getString(R.string.server_ip) + "/product_update";


        delete_button = (Button) findViewById(R.id.delete_button);
        delete_button.setOnClickListener(mDeleteClickListener);

        modify_button = (Button) findViewById(R.id.modify_button);
        modify_button.setOnClickListener(mModifyClickListener);

        select_button = (Button) findViewById(R.id.select_button);
        select_button.setOnClickListener(mSelectClickListener);

        name_editText = (EditText) findViewById(R.id.name_editText);
        about_editText = (EditText) findViewById(R.id.about_editText);
        price_editText = (EditText) findViewById(R.id.price_editText);
        place_editText = (EditText) findViewById(R.id.place_editText);

        intent = getIntent();
        mode = intent.getIntExtra("mode", 0);

        switch (mode) {
            case CONST.MODE_CREATE:
                num = intent.getStringExtra("num");
                if (num.compareTo(CONST.UNSELECTED) == 0) break;
                String name = intent.getStringExtra("name");
                place_editText.setText(name);
                delete_button_str = "취소";
                break;
            case CONST.MODE_UPDATE:
                num = intent.getStringExtra("num");
                pNum = intent.getStringExtra("pNum");
                place_editText = (EditText) findViewById(R.id.place_editText);
                place_editText.setText(intent.getStringExtra("name"));
                delete_button_str = "삭제";
                readProductInfo();
                break;
        }
        Toast.makeText(getApplicationContext(), "test1.", Toast.LENGTH_LONG).show();
    }

    private String findShopNumByName(int name) {
        return "num";
    }


    View.OnClickListener mDeleteClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            new AlertDialog.Builder(ManageProductDetail.this)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle("Closing Activity")
                    .setMessage(delete_button_str + "하시겠습니까?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (mode == CONST.MODE_UPDATE) deleteProductInfo();
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

    View.OnClickListener mSelectClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            Intent intent = new Intent(ManageProductDetail.this, ShopSelectMenu.class);
            startActivityForResult(intent, CONST.REQUEST_FILTER);
            Toast.makeText(getApplicationContext(), "test2.", Toast.LENGTH_LONG).show();
        }
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == CONST.RESULT_FILTER_SELECTED) {
            num = data.getStringExtra("num");
            name = data.getStringExtra("name");
            place_editText.setText(name);
        }
        else if (resultCode == CONST.RESULT_FILTER_UNSELECTED) {
        }
        Toast.makeText(getApplicationContext(), "test4.", Toast.LENGTH_LONG).show();
    }


    View.OnClickListener mModifyClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {

            Toast.makeText(getApplicationContext(), "test000.", Toast.LENGTH_LONG).show();
            switch (mode)
            {
                case CONST.MODE_CREATE:
                    createProductInfo();
                    break;
                case CONST.MODE_UPDATE:
                    updateProductInfo();
                    break;
            }
            finishWithResult();
        }
    };

    private void createProductInfo() {

        Toast.makeText(getApplicationContext(), "test5.", Toast.LENGTH_LONG).show();
        JSONObject jsonObject = new JSONObject();
        try {

            SharedPreferences sp = getSharedPreferences(my_shared_preferences, Context.MODE_PRIVATE);

            String userid = sp.getString("id", null);
            String userpw = sp.getString("pw", null);
            jsonObject.accumulate("id", userid);// 아이디 비번 받아와야함
            jsonObject.accumulate("pw", userpw);
            jsonObject.accumulate("num", num);
            jsonObject.accumulate("pName", name_editText.getText().toString());
            jsonObject.accumulate("price", price_editText.getText().toString());
            HttpAsyncTask httpTask = new HttpAsyncTask(jsonObject);
            String result = httpTask.execute(PATH_CREATE).get();

            JSONObject store = new JSONObject(result);
            int r = Integer.parseInt(store.getString("result"));
            switch (r)
            {
                case 1:
                    Toast.makeText(getApplicationContext(), "등록 완료!", Toast.LENGTH_LONG).show();
                    finishWithResult();
                    break;
                case -1:
                    Toast.makeText(getApplicationContext(), "로그인 되어있지 않습니다.", Toast.LENGTH_LONG).show();
                    finish();
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

    private void readProductInfo() {
        JSONObject jsonObject = new JSONObject();
        try {

            SharedPreferences sp = getSharedPreferences(my_shared_preferences, Context.MODE_PRIVATE);

            String userid = sp.getString("id", null);
            String userpw = sp.getString("pw", null);
            jsonObject.accumulate("id", userid);// 아이디 비번 받아와야함
            jsonObject.accumulate("pw", userpw);
            jsonObject.accumulate("num", num);
            jsonObject.accumulate("pNum", pNum);
            HttpAsyncTask httpTask = new HttpAsyncTask(jsonObject);
            String result = httpTask.execute(PATH_READ).get();


            // Log.e("hihi3333",result);
            JSONObject rProducts = new JSONObject(result);
            JSONArray rProductsJSONArray = rProducts.getJSONArray("rows");
            int r = Integer.parseInt(rProducts.getString("result"));
            Log.e("tab",rProductsJSONArray.getJSONObject(0).toString());
            switch (r)
            {
                case 1:
                    name_editText.setText(rProductsJSONArray.getJSONObject(0).getString("name"));
                    //about_editText.setText(rProductsJSONArray.getJSONObject(0).getString("about"));
                    price_editText.setText(rProductsJSONArray.getJSONObject(0).getString("price"));
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

    private int updateProductInfo() {
        JSONObject jsonObject = new JSONObject();
        try {

            SharedPreferences sp = getSharedPreferences(my_shared_preferences, Context.MODE_PRIVATE);

            String userid = sp.getString("id", null);
            String userpw = sp.getString("pw", null);
            jsonObject.accumulate("id", userid);// 아이디 비번 받아와야함
            jsonObject.accumulate("pw", userpw);
            jsonObject.accumulate("num", num);
            jsonObject.accumulate("pNum", pNum);
            jsonObject.accumulate("price", price_editText.getText().toString());
            HttpAsyncTask httpTask = new HttpAsyncTask(jsonObject);
            String result = httpTask.execute(PATH_UPDATE).get();

            // Log.e("hihi3333",result);

            JSONObject store = new JSONObject(result);
            int r = Integer.parseInt(store.getString("result"));
            switch (r) {
                case 1:
                    Toast.makeText(getApplicationContext(), "수정 완료!", Toast.LENGTH_LONG).show();
                    finishWithResult();
                    break;
                case -1:
                    Toast.makeText(getApplicationContext(), "로그인 되어있지 않습니다.", Toast.LENGTH_LONG).show();
                    finish();
                    break;
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

    private void deleteProductInfo() {
        Log.e("hihi","delete");
        JSONObject jsonObject = new JSONObject();
        try {

            SharedPreferences sp = getSharedPreferences(my_shared_preferences, Context.MODE_PRIVATE);

            String userid = sp.getString("id", null);
            String userpw = sp.getString("pw", null);
            jsonObject.accumulate("id", userid);// 아이디 비번 받아와야함
            jsonObject.accumulate("pw", userpw);
            jsonObject.accumulate("num", num);
            jsonObject.accumulate("pNum", pNum);
            HttpAsyncTask httpTask = new HttpAsyncTask(jsonObject);
            String result = httpTask.execute(PATH_DELETE).get();

            JSONObject store = new JSONObject(result);
            int r = Integer.parseInt(store.getString("result"));

            switch (r) {
                case 1:
                    Toast.makeText(getApplicationContext(), "삭제 완료!", Toast.LENGTH_LONG).show();
                    finishWithResult();
                    break;
                case -1:
                    Toast.makeText(getApplicationContext(), "로그인 되어있지 않습니다.", Toast.LENGTH_LONG).show();
                    finish();
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

    void finishWithResult() {
        Intent intent = new Intent();
        intent.putExtra("num", num);
        setResult(CONST.RESULT_UPDATED, intent);
        finish();
    }
}
