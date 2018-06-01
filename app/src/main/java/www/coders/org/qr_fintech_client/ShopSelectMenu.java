package www.coders.org.qr_fintech_client;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class ShopSelectMenu extends AppCompatActivity {

    ListView shop_listView;
    ArrayList<ShopObject> shops;
    private String PATH;
    String userid = "chulsoo@a.a", userpw = "dudgml";
    String num, name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_select_menu);
        PATH = getString(R.string.server_ip) + "/shop_list";

        shop_listView = findViewById(R.id.shop_listView);
        shops = new ArrayList<>();
        getStores();
        final ArrayAdapter<ShopObject> adapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1, shops);
        shop_listView.setAdapter(adapter);


        shop_listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                num = ((ShopObject) adapter.getItem(position)).getNum();
                name = ((ShopObject) adapter.getItem(position)).getName();
                Intent intent = new Intent();
                intent.putExtra("num", num);
                intent.putExtra("name", name);
                setResult(CONST.RESULT_FILTER_SELECTED, intent);
                finish();
            }
        });
    }

    void getStores()
    {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.accumulate("id", userid);// 아이디 비번 받아와야함
            jsonObject.accumulate("pw", userpw);
            HttpAsyncTask httpTask = new HttpAsyncTask(jsonObject);
            String result = httpTask.execute(PATH).get();

            Log.e("hihi3333",result);

            JSONObject rStores = new JSONObject(result);
            int r = Integer.parseInt(rStores.getString("result"));
            if (r == -1) finish();
            JSONArray rStoresJSONArray = rStores.getJSONArray("rows");
            for (int i = 0; i < rStoresJSONArray.length(); i++) {
                ShopObject shop = new ShopObject(rStoresJSONArray.getJSONObject(i));
                shops.add(shop);
            }
            Log.e("hihi222",rStoresJSONArray.getJSONObject(0).getString("name"));
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }


    public void onBackPressed() {
        Intent intent = new Intent();
        setResult(CONST.RESULT_FILTER_UNSELECTED, intent);
        finish();
    }

}
