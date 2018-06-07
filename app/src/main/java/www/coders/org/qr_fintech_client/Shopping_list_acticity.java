package www.coders.org.qr_fintech_client;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import jp.wasabeef.recyclerview.animators.BaseItemAnimator;
import jp.wasabeef.recyclerview.animators.FadeInAnimator;
import jp.wasabeef.recyclerview.animators.SlideInLeftAnimator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Shopping_list_acticity extends AppCompatActivity {
    ProgressDialog pDialog;
    private BaseItemAnimator fadeIn = new FadeInAnimator();
    public static final String my_shared_preferences = "login_information";
    SharedPreferences sharedpreferences; //안드로이드 Data 저장 class
    public final static String TAG_ID = "id";
    public final static String TAG_PASSWORD = "pw";
    private String id, password;
    private TextView cancel, payment, delete, sum_of_price;
    private Button allPayment;
    private DBHelper db;
    private ShopListAdpater adapter;
    private int sum = 0;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shop_list_animator);

        db = DBHelper.getInstance(this);

        sharedpreferences = getSharedPreferences(my_shared_preferences, Context.MODE_PRIVATE);
        id = sharedpreferences.getString(TAG_ID, null);
        password = sharedpreferences.getString(TAG_PASSWORD, null);

        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        cancel = (TextView) findViewById(R.id.shopping_list_cancel);
        payment = (TextView) findViewById(R.id.shopping_item_payment);
        delete = (TextView) findViewById(R.id.shopping_list_del);
        sum_of_price = (TextView) findViewById(R.id.shopping_total_price);

        allPayment = (Button)findViewById(R.id.shopping_item_allpayment);
        cancel.setVisibility(View.GONE);
        payment.setVisibility(View.GONE);
        delete.setVisibility(View.GONE);

        final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        /*
        if (getIntent().getBooleanExtra("GRID", true)) {
            recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        } else {
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
        }*/

        recyclerView.setItemAnimator(new SlideInLeftAnimator());

        List shoppingList = DBHelper.getInstance(this).getShoppingList(id);
        adapter = new ShopListAdpater(Shopping_list_acticity.this,shoppingList);
        recyclerView.setAdapter(adapter);
        recyclerView.setItemAnimator(fadeIn);
        recyclerView.getItemAnimator().setAddDuration(500);
        recyclerView.getItemAnimator().setRemoveDuration(500);

        for(int i = 0; i < adapter.getItemCount(); i++){
            int price = adapter.getmDataSet().get(i).getItem().getPrice();
            int count = adapter.getmDataSet().get(i).getCount();

            int all_price = price*count;
            sum += all_price;
        }
        sum_of_price.setText(Integer.toString(sum));

        delete.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                int selectedItem = adapter.getSelectedPosition();
                ShoppingListObject shoppingListObject = adapter.getItem(selectedItem);
                db.removeItem(shoppingListObject.getBuy_date());
                adapter.remove(selectedItem);
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                adapter.setSelectedPosition(-1);
            }
        });

        payment.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                int selectedItem = adapter.getSelectedPosition();
                ShoppingListObject shoppingListObject = adapter.getItem(selectedItem);
                product_payment(id,password,shoppingListObject,selectedItem);

            }
        });

        allPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<ShoppingListObject> dataset = adapter.getmDataSet();
                all_product_payment(id,password,dataset,false);
            }
        });
    }

    private void all_product_payment(final String id, final String password, final List<ShoppingListObject> dataset, boolean doing) {
        //로딩창 띄우기
        if(doing == false){
            pDialog = new ProgressDialog(this);
            pDialog.setCancelable(false);
            pDialog.setMessage("결제중 ...");
            showDialog();
        }

        if (dataset.size() == 0) {
            Toast.makeText(getApplicationContext(), "상품이 결제 되었습니다", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(Shopping_list_acticity.this, MainScreenActivity.class);
            finish();
            hideDialog();
            startActivity(intent);
            return;
        }

        ShoppingListObject shoppingListObject = dataset.get(0);
        String item_code = Integer.toString(shoppingListObject.getItem().getItem_num());
        String amount = Integer.toString(shoppingListObject.getCount());
        final String date = shoppingListObject.getBuy_date();

        NetRetrofit.getEndPoint().do_buy(id, password, item_code, amount).enqueue(new Callback<JsonObject>() {
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
                            db.removeItem(date);
                            adapter.remove(0);
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

    private void product_payment(final String id, final String password, ShoppingListObject shoppingListObject, final int selectedItem) {
        //로딩창 띄우기
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        pDialog.setMessage("결제중 ...");
        showDialog();

        String item_code = Integer.toString(shoppingListObject.getItem().getItem_num());
        String amount = Integer.toString(shoppingListObject.getCount());
        final String date = shoppingListObject.getBuy_date();

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
                            Toast.makeText(getApplicationContext(), "상품이 결제 되었습니다", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(Shopping_list_acticity.this, MainScreenActivity.class);
                            db.removeItem(date);
                            adapter.remove(selectedItem);
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
                    Log.e("item buy", "Failed");
                    Toast.makeText(getApplicationContext() ,"지금은 서버 점검중입니다.", Toast.LENGTH_LONG).show();
                    hideDialog();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e("item_buy", "Failed");
                Toast.makeText(getApplicationContext() ,"지금은 서버 점검중입니다.", Toast.LENGTH_LONG).show();
                hideDialog();
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Shopping_list_acticity.this, MainScreenActivity.class);
        finish();
        startActivity(intent);
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