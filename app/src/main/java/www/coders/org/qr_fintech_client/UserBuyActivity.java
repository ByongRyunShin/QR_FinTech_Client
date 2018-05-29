package www.coders.org.qr_fintech_client;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserBuyActivity extends AppCompatActivity {
    ProgressDialog pDialog;
    private static final String TAG_RESULT = "item_code";
    public static final String my_shared_preferences = "login_information";
    int amount;
    SharedPreferences sharedpreferences; //안드로이드 Data 저장 class

    private String id, password, name;
    private int price;
    public final static String TAG_ID = "id";
    public final static String TAG_PASSWORD = "pw";

    private TextView item_name, item_price, item_count, total_price;
    private Button plus, minus, cancel, add_shoplist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_buy);

        sharedpreferences = getSharedPreferences(my_shared_preferences, Context.MODE_PRIVATE);
        id = sharedpreferences.getString(TAG_ID, null);
        password = sharedpreferences.getString(TAG_PASSWORD, null);

        plus = (Button) findViewById(R.id.buy_increase);
        minus = (Button) findViewById(R.id.buy_decrease);
        cancel = (Button) findViewById(R.id.user_buy_cancel_button);
        add_shoplist = (Button) findViewById(R.id.add_shoplist_button);
        item_name = (TextView) findViewById(R.id.user_buy_item_name);
        item_price = (TextView) findViewById(R.id.user_buy_item_price);
        item_count = (TextView) findViewById(R.id.item_count);
        total_price = (TextView) findViewById(R.id.buy_item_total_price);

        final String item_code;

        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                item_code= null;
            } else {
                item_code= extras.getString(TAG_RESULT);
            }
        } else {
            item_code= (String) savedInstanceState.getSerializable(TAG_RESULT);
        }

        product_detail("a@a","a","17","15");

        //// plus 카운트 추가할것
        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                amount = Integer.parseInt(item_count.getText().toString());
                amount += 1;
                item_count.setText(Integer.toString(amount));
                int sum_price = amount * price;
                total_price.setText(Integer.toString(sum_price));
            }
        });

        minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                int count = Integer.parseInt(item_count.getText().toString());
                count -= 1;
                item_count.setText(Integer.toString(count));
                int sum_price = count* price;
                total_price.setText(Integer.toString(sum_price));
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserBuyActivity.super.onBackPressed();
                finish();
            }
        });

        add_shoplist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                add_shopList(id,password,item_code,Integer.toString(amount));
            }
        });

    }

    private void product_detail(final String id, final String password, String num, String pNum) {
        //로딩창 띄우기
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        pDialog.setMessage("상품 조회 중 ...");
        showDialog();

        NetRetrofit.getEndPoint().do_product_detail(id,password,num,pNum).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    Log.e("buy", "Success");
                    Log.e("response", response.body().toString());

                    int success = 0;
                    try {
                        JSONObject jObj = new JSONObject(response.body().toString());
                        success = jObj.getInt("result");
                        if(success == 1)
                        {
                            name = jObj.getJSONArray("rows").getJSONObject(0).getString("name");
                            price = jObj.getJSONArray("rows").getJSONObject(0).getInt("price");

                            item_name.setText(name);
                            item_price.setText(Integer.toString(price));
                            total_price.setText(Integer.toString(price));

                            hideDialog();
                        }
                        else
                        {
                            Toast.makeText(getApplicationContext(), jObj.getString("msg"), Toast.LENGTH_LONG).show();
                            hideDialog();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                } else {
                    Log.e("login", "Failed");
                    Toast.makeText(getApplicationContext() ,"지금은 서버 점검중입니다.", Toast.LENGTH_LONG).show();
                    hideDialog();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e("login", "Failed");
                Toast.makeText(getApplicationContext() ,"지금은 서버 점검중입니다.", Toast.LENGTH_LONG).show();
                hideDialog();
            }
        });
    }

    private void add_shopList(final String id, final String password, String item_code, String amount) {
        //로딩창 띄우기
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        pDialog.setMessage("장바구니에 추가중 ...");
        showDialog();

        NetRetrofit.getEndPoint().do_buy(id,password,item_code,amount).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    Log.e("buy", "Success");
                    Log.e("buy", response.body().toString());

                    int success = 0;
                    try {
                        JSONObject jObj = new JSONObject(response.body().toString());
                        success = jObj.getInt("result");
                        if(success == 1)
                        {
                            hideDialog();
                            Toast.makeText(getApplicationContext(), "상품이 추가 되었습니다", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(UserBuyActivity.this, MainScreenActivity.class);
                            finish();
                            startActivity(intent);
                        }
                        else
                        {
                            Toast.makeText(getApplicationContext(), jObj.getString("msg"), Toast.LENGTH_LONG).show();
                            hideDialog();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                } else {
                    Log.e("add_shoplist", "Failed");
                    Toast.makeText(getApplicationContext() ,"지금은 서버 점검중입니다.", Toast.LENGTH_LONG).show();
                    hideDialog();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e("add_shoplist", "Failed");
                Toast.makeText(getApplicationContext() ,"지금은 서버 점검중입니다.", Toast.LENGTH_LONG).show();
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
