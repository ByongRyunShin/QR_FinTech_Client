package www.coders.org.qr_fintech_client;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
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
    String selectedNum;
    TextView mTitle;

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

        PATH_PRODUCT_ALL = getContext().getString(R.string.server_ip) + "/product_list_all";
        PATH_PRODUCT = getContext().getString(R.string.server_ip) + "/product_list";
        PATH_SHOP = getContext().getString(R.string.server_ip) +"/shop_list";
        selectedNum = CONST.UNSELECTED;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View layout = inflater.inflate(R.layout.fragment_manage_product, container, false);
        productAll_listView = (ListView) layout.findViewById(R.id.product_all_listView);

        create_button = (Button) layout.findViewById(R.id.create_button);
        create_button.setOnClickListener(mCreateClickListener);

        select_button = (Button) layout.findViewById(R.id.select_button);
        select_button.setOnClickListener(mSelectClickListener);
        shops = new ArrayList<>();

        //  num = ALL_SHOPS;
        selectAllOfShops();
        readShops();
        readProducts();
        connectListViewWithAdapter();

        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        mTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
        mTitle.setText(getShopNameByNum(selectedNum) + "상점의 물건목록");

        return layout;
    }

    void readShops() {
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
            if (shops.get(i).getNum().equals(num)) return shops.get(i).getName();
        }
        return "전체";
    }

    boolean setShopSelect(String num) {
        boolean result = false;
        for (int i = 0; i < shops.size(); i++) {
            if (shops.get(i).getNum().equals(num)) {
                shops.get(i).setSelected(true);
                result = true;
            }
            else shops.get(i).setSelected(false);
        }
        return result;
    }
    void selectAllOfShops() {
        for (int i = 0; i < shops.size(); i++) {
            shops.get(i).setSelected(true);
        }
    }
    int getShopIndexByNum(String num) {
        for (int i = 0; i < shops.size(); i++) {
            if (shops.get(i).getNum().equals(num)) return i;
        }
        return -1;
    }

    void readProducts(String num)
    {

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
                int idx = getShopIndexByNum(num);
                if (idx >= 0 && shops.get(idx).isSelected()) {
                    product.setName(shops.get(getShopIndexByNum(num)).getName());
                    products.add(product);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    void readProducts()
    {
        products = new ArrayList<>();
        int len = shops.size();
        for (int i = 0; i < len; i++) {
            readProducts(shops.get(i).getNum());
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
                startActivityForResult(intent, CONST.REQUEST_UPDATE);

            }
        });
    }

    View.OnClickListener mSelectClickListener = new View.OnClickListener() {
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
                products = new ArrayList<>();
                setShopSelect(selectedNum);
                readProducts(selectedNum);
                break;
            case CONST.RESULT_FILTER_UNSELECTED:
                selectAllOfShops();
                readProducts();
                selectedNum = CONST.UNSELECTED;
                break;
        }
        connectListViewWithAdapter();
        mTitle.setText(getShopNameByNum(selectedNum) + "상점의 물건목록");
    }
}
