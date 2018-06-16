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
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import static www.coders.org.qr_fintech_client.MainActivity.*;
import static www.coders.org.qr_fintech_client.MainScreenActivity.TAG_REUSLT;
//import static www.coders.org.qr_fintech_client.MainActivity.TAG_PASSWORD;

public class ManageOrderFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    public static final String my_shared_preferences = "login_information";
    SharedPreferences sharedpreferences; //안드로이드 Data 저장 class

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String TAG_USER_INFO = "user_info";
    private static final String TAG_PRICE = "price";
    private static final String TAG_ORDER_LIST = "order_list";

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
    Button payment_button, select_button;

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
        //getActivity().setTitle("주문 관리");

        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        TextView mTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
        mTitle.setText("주문 관리");

        View layout = inflater.inflate(R.layout.fragment_manage_order, container, false);

        payment_button = (Button) layout.findViewById(R.id.payment_button);
        payment_button.setOnClickListener(mPaymentClickListener);

        select_button = (Button) layout.findViewById(R.id.select_button);
        select_button.setOnClickListener(mSelectClickListener);

        productAll_listView = (ListView) layout.findViewById(R.id.order_listView);
        total_textView = (TextView) layout.findViewById(R.id.total_textView);
        total_textView.setText("0");
        shops = new ArrayList<>();

        getShops();
        getProducts();
        connectListViewWithAdapter();
        return layout;
    }

    View.OnClickListener mSelectClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(getActivity(), ShopSelectMenu.class);
            startActivityForResult(intent, CONST.REQUEST_FILTER);
            // startActivity(intent);
        }
    };
    View.OnClickListener mPaymentClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            FragmentIntentIntegrator integrator = new FragmentIntentIntegrator(ManageOrderFragment.this);
            integrator.setCaptureActivity( qrReader.class );
            integrator.setOrientationLocked(false);
            integrator.initiateScan();
        }
    };

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

            JSONObject rShops = new JSONObject(result);
            int r = Integer.parseInt(rShops.getString("result"));
            if (r == -1) getActivity().finish();
            JSONArray rStoresJSONArray = rShops.getJSONArray("rows");
            for (int i = 0; i < rStoresJSONArray.length(); i++) {
                ShopObject shop = new ShopObject(rStoresJSONArray.getJSONObject(i));
                shops.add(shop);
            }
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null){
            if(result.getContents() != null){
                Intent intent = new Intent(getActivity(), BuyRequestActivity.class);
                intent.putExtra(TAG_PRICE, total_textView.getText().toString());
                intent.putExtra(TAG_USER_INFO, result.getContents());
                intent.putExtra(TAG_ORDER_LIST,products);

                startActivity(intent);
            }
        }else{
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

    public void setTotal(int change) {
        int total = Integer.parseInt(total_textView.getText().toString()) + change;
        total_textView.setText(Integer.toString(total));
    }

    private final class FragmentIntentIntegrator extends IntentIntegrator {

        private final Fragment fragment;

        public FragmentIntentIntegrator(Fragment fragment) {
            super(fragment.getActivity());
            this.fragment = fragment;
        }

        @Override
        protected void startActivityForResult(Intent intent, int code) {
            fragment.startActivityForResult(intent, code);
        }
    }
}
