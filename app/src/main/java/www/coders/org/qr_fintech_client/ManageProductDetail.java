package www.coders.org.qr_fintech_client;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class ManageProductDetail extends AppCompatActivity {

    Button delete_button;
    public static final int MODE_APPLY = 1;
    public static final int MODE_MODIFY = 2;

    private static final String PATH = R.string.server_ip + "/product_list";
    String userid = "chulsoo@a.a", userpw = "dudgml";
    ArrayList<ProductObject> products;

    String num, name;
    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_product_detail);
        products = new ArrayList<>();
        intent = getIntent();
        num = intent.getStringExtra("num");
        userid = intent.getStringExtra("id");
        userpw = intent.getStringExtra("pw");
        name = intent.getStringExtra("name");

        getProducts();
        ListView productList = (ListView) findViewById(R.id.item_listView);


        updateList(productList);
    }

    void getProducts()
    {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.accumulate("id", userid);// 아이디 비번 받아와야함
            jsonObject.accumulate("pw", userpw);
            jsonObject.accumulate("num", num);
            HttpAsyncTask httpTask = new HttpAsyncTask(jsonObject);
            String result = httpTask.execute(PATH).get();

            Log.e("hihi3333",result);

            JSONObject rStores = new JSONObject(result);
            int r = Integer.parseInt(rStores.getString("result"));
            if (r == -1) finish();
            JSONArray rStoresJSONArray = rStores.getJSONArray("rows");
            for (int i = 0; i < rStoresJSONArray.length(); i++) {
                ProductObject product = new ProductObject(rStoresJSONArray.getJSONObject(i));
//                if (product.getIsDelete().compareTo("1") == 0) products.add(product);
                products.add(product);
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
    public void updateList (ListView lv) {
        final ArrayAdapter<ProductObject> adapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1, products);
        lv.setAdapter(adapter);



        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Toast.makeText(getActivity(), (String) parent.getItemAtPosition(position), Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getApplicationContext(), ManageProductSuperDetail.class);
                intent.putExtra("mode", MODE_MODIFY);
                intent.putExtra("pnum", ((ProductObject) adapter.getItem(position)).getNum());
                intent.putExtra("num", num);
                intent.putExtra("id", userid);
                intent.putExtra("pw", userpw);
                intent.putExtra("name", name);
                startActivity(intent);
            }
        });

    }
}
