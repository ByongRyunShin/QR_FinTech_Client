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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import static www.coders.org.qr_fintech_client.MainActivity.*;
//import static www.coders.org.qr_fintech_client.MainActivity.TAG_PASSWORD;

public class ManageProductFragment extends Fragment {
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
    ProductListAdapter adapter;
    ArrayList<ProductObject> products;
    ArrayList<ShopObject> shops;
    Button select_button, create_button;
    String id, password;
    String userid = "chulsoo@a.a", userpw = "dudgml";
    String selectedNum;


    // 선택된 상점 리스트를 만들어서 empty면 모든상점 출력하고  아니면 리스트안에 있는 상점들의 물건 출력하게 바꿀예정....
    // productdetail에 상점 선택기능 추가
    // 물건추가클릭하거나 디테일에서 나올때 수정해야함




    public ManageProductFragment() {
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


        View layout = inflater.inflate(R.layout.fragment_manage_product, container, false);
        productAll_listView = (ListView) layout.findViewById(R.id.product_all_listView);

        create_button = (Button) layout.findViewById(R.id.create_button);
        create_button.setOnClickListener(mCreateClickListener);

        select_button = (Button) layout.findViewById(R.id.select_button);
        select_button.setOnClickListener(mFilterClickListener);
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
                ProductObject product = new ProductObject(rProductsJSONArray.getJSONObject(i));
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
            jsonObject.accumulate("id", userid);// 아이디 비번 받아와야함
            jsonObject.accumulate("pw", userpw);
            HttpAsyncTask httpTask = new HttpAsyncTask(jsonObject);
            String result = httpTask.execute(PATH_PRODUCT_ALL).get();

            JSONObject rProducts = new JSONObject(result);
            int r = Integer.parseInt(rProducts.getString("result"));
            if (r == -1) getActivity().finish();
            JSONArray rProductsJSONArray = rProducts.getJSONArray("rows");
            for (int i = 0; i < rProductsJSONArray.length(); i++) {
                ProductObject product = new ProductObject(rProductsJSONArray.getJSONObject(i));
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
        adapter = new ProductListAdapter(getActivity(), products);
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

                intent.putExtra("id", userid);
                intent.putExtra("pw", userpw);
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
    View.OnClickListener mCreateClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(getContext(), ManageProductDetail.class);
            intent.putExtra("mode", CONST.MODE_CREATE);
            intent.putExtra("id", userid);
            intent.putExtra("pw", userpw);
            intent.putExtra("num", selectedNum);
            if (selectedNum.compareTo(CONST.UNSELECTED) != 0) intent.putExtra("name", getShopNameByNum(selectedNum));
            startActivityForResult(intent, CONST.REQUEST_UPDATE);
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
}
