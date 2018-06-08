package www.coders.org.qr_fintech_client;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BuyRequestActivity extends AppCompatActivity {
    public static final String my_shared_preferences = "login_information";
    private static final String TAG_USER_INFO = "user_info";
    private static final String TAG_PRICE = "price";
    private static final String TAG_ORDER_LIST = "order_list";
    private String cyper_str;

    ProgressDialog pDialog;
    ArrayList<OrderObject> order_list;
    EditText money, recevier_id, password;
    Button ok_btn;
    int balance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_requset);

        money = (EditText)findViewById(R.id.send_money_text);
        recevier_id = (EditText)findViewById(R.id.send_id_text);
        password = (EditText)findViewById(R.id.send_password);

        ok_btn = (Button)findViewById(R.id.send_btn);

        SharedPreferences sp = getSharedPreferences(my_shared_preferences, Context.MODE_PRIVATE);
        final String id = sp.getString("id", null);
        final String pw = sp.getString("pw", null);

        Bundle extras = getIntent().getExtras();
        String price = extras.getString(TAG_PRICE);
        cyper_str = extras.getString(TAG_USER_INFO);
        order_list = (ArrayList<OrderObject>)extras.getSerializable(TAG_ORDER_LIST);

        money.setFocusable(false); money.setClickable(false);
        money.setText(price);
        recevier_id.setFocusable(false); recevier_id.setClickable(false);
        recevier_id.setText(cyper_str);

        ok_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(password.getText().toString().compareTo(pw) == 0)
                    all_product_payment(id,pw,order_list,false);
                else{
                    Toast.makeText(getApplicationContext(), "비밀번호가 일치하지 않습니다.", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void all_product_payment(final String id, final String password, final ArrayList<OrderObject> dataset, boolean doing) {
        //로딩창 띄우기
        if(doing == false){
            pDialog = new ProgressDialog(this);
            pDialog.setCancelable(false);
            pDialog.setMessage("결제중 ...");
            showDialog();
        }

        if (dataset.size() == 0) {
            Toast.makeText(getApplicationContext(), "상품이 결제 되었습니다", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(BuyRequestActivity.this, MainScreenActivity.class);
            finish();
            hideDialog();
            startActivity(intent);
            return;
        }

        OrderObject orderObject = dataset.get(0);

        String shop_num = orderObject.getNum();
        String pNum = orderObject.getpNum();
        String amount = Integer.toString(orderObject.getQuantity());

        /*
            Call<JsonObject> do_sell(@Field("id") String id, @Field("pw") String pw, @Field("num") String num,
                             @Field("pNum") String pNum, @Field("amount") String amount, @Field("cyper_str") String cyper_str);

         */

        NetRetrofit.getEndPoint().do_sell(id, password, shop_num, pNum, amount, cyper_str).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    Log.e("buy", "Success");
                    Log.e("buy", response.body().toString());

                    int success = 0;
                    try {
                        JSONObject jObj = new JSONObject(response.body().toString());
                        success = jObj.getInt("result");
                        if (success == 1) {
                            dataset.remove(0);
                            all_product_payment(id,password,dataset,true);
                        } else {
                            Toast.makeText(getApplicationContext(), jObj.getString("msg"), Toast.LENGTH_LONG).show();
                            hideDialog();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    Log.e("item buy", "Failed");
                    Toast.makeText(getApplicationContext(), "지금은 서버 점검중입니다.", Toast.LENGTH_LONG).show();
                    hideDialog();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e("item_buy", "Failed");
                Toast.makeText(getApplicationContext(), "지금은 서버 점검중입니다.", Toast.LENGTH_LONG).show();
                hideDialog();
            }
        });

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
