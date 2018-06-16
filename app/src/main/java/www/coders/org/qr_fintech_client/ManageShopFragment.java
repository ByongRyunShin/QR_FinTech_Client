package www.coders.org.qr_fintech_client;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;


public class ManageShopFragment extends Fragment {
    private String PATH, selectedNum;
    ShopListAdapter adapter;
    Button apply_button;
    TextView title_textView;
    ListView storeList;
    // ArrayList<String> shops;
    public static final String my_shared_preferences = "login_information";
    ArrayList<ShopObject> shops;

    public ManageShopFragment() {
        // Required empty public constructor
    }

    public ArrayList<ShopObject> getStoreList() { return shops; }
    public static ManageShopFragment newInstance(String param1, String param2) {
        ManageShopFragment fragment = new ManageShopFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @SuppressLint("HandlerLeak")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PATH = getContext().getString(R.string.server_ip) + "/shop_list";
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        SharedPreferences sp = getActivity().getSharedPreferences(my_shared_preferences, Context.MODE_PRIVATE);
        String username = sp.getString("name", null);
        //getActivity().setTitle();

        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        TextView mTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
        mTitle.setText(username + "의 상점 목록");

        shops = new ArrayList<>();
        View layout = inflater.inflate(R.layout.fragment_manage_shop, container, false) ;
        //title_textView = (TextView) layout.findViewById(R.id.title_textView);
        apply_button = (Button) layout.findViewById(R.id.apply_button);
        apply_button.setOnClickListener(mCreateClickListener);
        readShops();
        storeList = (ListView) layout.findViewById(R.id.store_listView);
        connectListViewWithAdapter();
        return layout;

    }
    void readShops()
    {
        shops = new ArrayList<>();
        JSONObject jsonObject = new JSONObject();
        try {
            SharedPreferences sp = getActivity().getSharedPreferences(my_shared_preferences, Context.MODE_PRIVATE);

            String userid = sp.getString("id", null);
            String userpw = sp.getString("pw", null);
            jsonObject.accumulate("id", userid);// 아이디 비번 받아와야함
            jsonObject.accumulate("pw", userpw);
            HttpAsyncTask httpTask = new HttpAsyncTask(jsonObject);
            String result = httpTask.execute(PATH).get();

            Log.e("hihi3333",result);

            JSONObject rStores = new JSONObject(result);
            int r = Integer.parseInt(rStores.getString("result"));
            if (r == -1) getActivity().finish();
            JSONArray rStoresJSONArray = rStores.getJSONArray("rows");
            for (int i = 0; i < rStoresJSONArray.length(); i++) {
                ShopObject store = new ShopObject(rStoresJSONArray.getJSONObject(i));
                shops.add(store);
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

    public void connectListViewWithAdapter() {
        adapter = new ShopListAdapter(getActivity(), shops);
        storeList.setAdapter(adapter);

        storeList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getContext(), ManageShopDetail.class);
                intent.putExtra("mode", CONST.MODE_UPDATE);
                intent.putExtra("item", ((ShopObject) adapter.getItem(position)).getNum());
                startActivityForResult(intent, CONST.REQUEST_UPDATE);
            }
        });
    }

    View.OnClickListener mCreateClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            // todo
            Intent intent = new Intent(getContext(), ManageShopDetail.class);
            intent.putExtra("mode", CONST.MODE_CREATE);
            startActivityForResult(intent, CONST.REQUEST_UPDATE);
        }
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (resultCode) {
            case CONST.RESULT_UPDATED:
                readShops();
                connectListViewWithAdapter();
                break;
        }

    }
}