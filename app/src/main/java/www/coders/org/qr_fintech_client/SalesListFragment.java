package www.coders.org.qr_fintech_client;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class SalesListFragment extends Fragment {

    ListView sales_listView;
    SalesListAdapter adapter;
    ArrayList<SalesObject> salesList;
    public static String PATH;
    public static final String my_shared_preferences = "login_information";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sales_list, container, false);
        PATH = getContext().getString(R.string.server_ip) + "/buy_sell_list";
        sales_listView = (ListView) view.findViewById(R.id.sales_listView);
        salesList = new ArrayList<>();
        readSalesList();
        connectListViewWithAdapter();
        return view;
    }

    void readSalesList() {
        salesList = new ArrayList<>();
        JSONObject jsonObject = new JSONObject();
        try {
            SharedPreferences sp = getActivity().getSharedPreferences(my_shared_preferences, Context.MODE_PRIVATE);
            String userid = sp.getString("id", null);
            String userpw = sp.getString("pw", null);
            jsonObject.accumulate("id", userid);// 아이디 비번 받아와야함
            jsonObject.accumulate("pw", userpw);
            HttpAsyncTask httpTask = new HttpAsyncTask(jsonObject);
            String result = httpTask.execute(PATH).get();

            JSONObject rSalesList = new JSONObject(result);
            int r = Integer.parseInt(rSalesList.getString("result"));
            if (r == -1) getActivity().finish();
            JSONArray rSalesListJSONArray = rSalesList.getJSONArray("rows");
            for (int i = 0; i < rSalesListJSONArray.length(); i++) {
                SalesObject sales = new SalesObject(rSalesListJSONArray.getJSONObject(i));
                salesList.add(sales);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    void connectListViewWithAdapter() {
        adapter = new SalesListAdapter(getActivity(), salesList);
        sales_listView.setAdapter(adapter);

        sales_listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
/*
                Intent intent = new Intent(getContext(), ManageProductDetail.class);
                intent.putExtra("mode", CONST.MODE_UPDATE);
                String num = ((ProductObject) adapter.getItem(position)).getNum();
                intent.putExtra("num", num);
                intent.putExtra("pNum", ((ProductObject) adapter.getItem(position)).getpNum());
                intent.putExtra("name", getShopNameByNum(num));

                intent.putExtra("id", userid);
                intent.putExtra("pw", userpw);
                startActivityForResult(intent, CONST.REQUEST_UPDATE);
*/
            }
        });
    }


}
