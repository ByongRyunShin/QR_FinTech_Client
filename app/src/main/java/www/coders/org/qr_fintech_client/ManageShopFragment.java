package www.coders.org.qr_fintech_client;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public static final String PATH = R.string.server_ip+"/shop_list/";
    public  static final int STORE_OBJ = 1;
    public static final int MODE_APPLY = 1;
    public static final int MODE_MODIFY = 2;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    Button apply_button;
    TextView title_textView;
    String userid = "chulsoo@a.a", userpw = "dudgml";

    // ArrayList<String> stores;
    ArrayList<ShopObject> stores;

    public ManageShopFragment() {
        // Required empty public constructor
    }

    public ArrayList<ShopObject> getStoreList() { return stores; }
    // TODO: Rename and change types and number of parameters
    public static ManageShopFragment newInstance(String param1, String param2) {
        ManageShopFragment fragment = new ManageShopFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @SuppressLint("HandlerLeak")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        getActivity().setTitle("Manage Store Fragment");
        ProgressDialog pDialog;

        pDialog = new ProgressDialog(getContext());
        pDialog.setCancelable(false);
        pDialog.setMessage("로그인 중 ...");
        showDialog(pDialog);

        hideDialog(pDialog);

        stores = new ArrayList<>();


        View layout = inflater.inflate(R.layout.fragment_manage_shop, container, false) ;
        title_textView = (TextView) layout.findViewById(R.id.title_textView);



        apply_button = (Button) layout.findViewById(R.id.apply_button);
        apply_button.setOnClickListener(mClickListener);

        getStores();
        ListView storeList = (ListView) layout.findViewById(R.id.store_listView);

        updateList(storeList);


        return layout;

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
            if (r == -1) getActivity().finish();
            JSONArray rStoresJSONArray = rStores.getJSONArray("rows");
            for (int i = 0; i < rStoresJSONArray.length(); i++) {
                ShopObject store = new ShopObject(rStoresJSONArray.getJSONObject(i));
                stores.add(store);
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
        final ArrayAdapter<ShopObject> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, stores);
        lv.setAdapter(adapter);


        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Toast.makeText(getActivity(), (String) parent.getItemAtPosition(position), Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getActivity(), ManageShopDetail.class);
                intent.putExtra("mode", MODE_MODIFY);
                intent.putExtra("item", ((ShopObject) adapter.getItem(position)).getNum());
                intent.putExtra("id", userid);
                intent.putExtra("pw", userpw);
                startActivity(intent);
            }
        });
    }

    View.OnClickListener mClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            // todo
            Intent intent = new Intent(getActivity(), ManageShopDetail.class);
            intent.putExtra("mode", MODE_APPLY);
            intent.putExtra("id", userid);
            intent.putExtra("pw", userpw);
            startActivity(intent);
        }
    };

    private void showDialog(ProgressDialog pDialog) {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog(ProgressDialog pDialog) {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

}
