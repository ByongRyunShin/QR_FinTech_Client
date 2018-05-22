package www.coders.org.qr_fintech_client;

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
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class ManageProductFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String PATH;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public static final int MODE_APPLY = 1;
    public static final int MODE_MODIFY = 2;

    String userid = "chulsoo@a.a", userpw = "dudgml";

    ArrayList<ShopObject> stores;

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

        PATH = getContext().getString(R.string.server_ip) + "/shop_list";
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        getActivity().setTitle("Manage Item Fragment");

        stores = new ArrayList<>();


        View layout = inflater.inflate(R.layout.fragment_manage_product, container, false) ;

        getStores();
        ListView storeList = (ListView) layout.findViewById(R.id.store22_listView);

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
                Intent intent = new Intent(getActivity(), ManageProductDetail.class);
                intent.putExtra("num", ((ShopObject) adapter.getItem(position)).getNum());
                intent.putExtra("name", ((ShopObject) adapter.getItem(position)).getName());
                intent.putExtra("id", userid);
                intent.putExtra("pw", userpw);
                startActivity(intent);
            }
        });

    }


}
