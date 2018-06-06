package www.coders.org.qr_fintech_client;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import static www.coders.org.qr_fintech_client.MainActivity.*;
//import static www.coders.org.qr_fintech_client.MainActivity.TAG_PASSWORD;

public class ManageOrderFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    public static final String my_shared_preferences = "login_information";
    SharedPreferences sharedpreferences; //안드로이드 Data 저장 class

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String PATH_SHOP;
    private String PATH_PRODUCT;
    private String PATH_PRODUCT_ALL;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    ListView productAll_listView;
    OrderListAdapter adapter;
    ArrayList<OrderObject> products;
    ArrayList<ShopObject> shops;
    String id, password;
    String selectedNum;
    TextView total_textView;
    View layout;
    public ManageOrderFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static ManageProductFragment newInstance(String param1, String param2) {
        ManageProductFragment fragment = new ManageProductFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        sharedpreferences = getActivity().getSharedPreferences(my_shared_preferences, Context.MODE_PRIVATE);
        id = sharedpreferences.getString(TAG_ID, null);
        password = sharedpreferences.getString(TAG_PASSWORD, null);

        PATH_PRODUCT_ALL = getContext().getString(R.string.server_ip) + "/product_list_all";
        PATH_PRODUCT = getContext().getString(R.string.server_ip) + "/product_list";
        PATH_SHOP = getContext().getString(R.string.server_ip) +"/shop_list";
        selectedNum = CONST.UNSELECTED;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        getActivity().setTitle("Manage Item Fragment");


        layout = inflater.inflate(R.layout.fragment_manage_order, container, false);
        productAll_listView = (ListView) layout.findViewById(R.id.order_listView);
        total_textView = (TextView) layout.findViewById(R.id.total_textView);
        total_textView.setText("0");
        shops = new ArrayList<>();

        //  num = ALL_SHOPS;

        getShops();
        getProducts();
        connectListViewWithAdapter();
        return layout;
    }

    void getShops() {
        JSONObject jsonObject = new JSONObject();
        try {
            SharedPreferences sp = getActivity().getSharedPreferences(my_shared_preferences, Context.MODE_PRIVATE);

            String userid = sp.getString("id", null);
            String userpw = sp.getString("pw", null);

            jsonObject.accumulate("id", userid);// 아이디 비번 받아와야함
            jsonObject.accumulate("pw", userpw);
            HttpAsyncTask httpTask = new HttpAsyncTask(jsonObject);
            String result = httpTask.execute(PATH_SHOP).get();

            Log.e("hihi3333",result);

            JSONObject rShops = new JSONObject(result);
            int r = Integer.parseInt(rShops.getString("result"));
            if (r == -1) getActivity().finish();
            JSONArray rStoresJSONArray = rShops.getJSONArray("rows");
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

    String getShopNameByNum(String num) {
        for (int i = 0; i < shops.size(); i++) {
            if (shops.get(i).getNum().compareTo(num) == 0) return shops.get(i).getName();
        }
        return "name";
    }

    void getProducts(String num)
    {
        products = new ArrayList<>();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.accumulate("num", num);// 아이디 비번 받아와야함
            HttpAsyncTask httpTask = new HttpAsyncTask(jsonObject);
            String result = httpTask.execute(PATH_PRODUCT).get();

            JSONObject rProducts = new JSONObject(result);
            int r = Integer.parseInt(rProducts.getString("result"));
            if (r == -1) getActivity().finish();
            JSONArray rProductsJSONArray = rProducts.getJSONArray("rows");
            for (int i = 0; i < rProductsJSONArray.length(); i++) {
                OrderObject product = new OrderObject(rProductsJSONArray.getJSONObject(i));
                product.setName(getShopNameByNum(num));
                products.add(product);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    void getProducts()
    {
        products = new ArrayList<>();
        JSONObject jsonObject = new JSONObject();
        try {

            SharedPreferences sp = getActivity().getSharedPreferences(my_shared_preferences, Context.MODE_PRIVATE);

            String userid = sp.getString("id", null);
            String userpw = sp.getString("pw", null);

            jsonObject.accumulate("id", userid);// 아이디 비번 받아와야함
            jsonObject.accumulate("pw", userpw);
            HttpAsyncTask httpTask = new HttpAsyncTask(jsonObject);
            String result = httpTask.execute(PATH_PRODUCT_ALL).get();

            JSONObject rProducts = new JSONObject(result);
            int r = Integer.parseInt(rProducts.getString("result"));
            if (r == -1) getActivity().finish();
            JSONArray rProductsJSONArray = rProducts.getJSONArray("rows");
            for (int i = 0; i < rProductsJSONArray.length(); i++) {
                OrderObject product = new OrderObject(rProductsJSONArray.getJSONObject(i));
                product.setName(getShopNameByNum(rProductsJSONArray.getJSONObject(i).getString("owner_shop")));
                products.add(product);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    public void connectListViewWithAdapter () {
        adapter = new OrderListAdapter(getActivity(), products, this);

        productAll_listView.setAdapter(adapter);

        productAll_listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(getContext(), ManageProductDetail.class);
                intent.putExtra("mode", CONST.MODE_UPDATE);
                String num = ((ProductObject) adapter.getItem(position)).getNum();
                intent.putExtra("num", num);
                intent.putExtra("pNum", ((ProductObject) adapter.getItem(position)).getpNum());
                intent.putExtra("name", getShopNameByNum(num));
                startActivityForResult(intent, CONST.REQUEST_UPDATE);

            }
        });
    }

    View.OnClickListener mFilterClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(getActivity(), ShopSelectMenu.class);
            startActivityForResult(intent, CONST.REQUEST_FILTER);
            // startActivity(intent);
        }
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (resultCode) {
            case CONST.RESULT_FILTER_SELECTED: case CONST.RESULT_UPDATED:
                selectedNum = data.getStringExtra("num");
                getProducts(selectedNum);
                break;
            case CONST.RESULT_FILTER_UNSELECTED:
                getProducts();
                selectedNum = CONST.UNSELECTED;
                break;
        }
        connectListViewWithAdapter();
    }

    public void setTotal(int change) {
        //if (total_textView == null)
        //    total_textView = (TextView) layout.findViewById(R.id.total_textView);
        int total = Integer.parseInt(total_textView.getText().toString()) + change;
        total_textView.setText(Integer.toString(total));
    }
}
