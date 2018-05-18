package www.coders.org.qr_fintech_client;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.*;

import java.io.IOException;
import java.util.*;
import android.widget.*;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.concurrent.ExecutionException;

public class ManageStoreFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public static final String SERVER = "http://49.173.132.147:9999";
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    Button apply_button;
    TextView tv;

    ArrayList<String> stores;
    ArrayList<StoreObject> storeList;
    Handler handler;
    public ManageStoreFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static ManageStoreFragment newInstance(String param1, String param2) {
        ManageStoreFragment fragment = new ManageStoreFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                int num = ((StoreObject) msg.obj).getNum();
                String name = ((StoreObject) msg.obj).getName();
                //super.handleMessage(msg);
                //String result = (String) msg.obj;


            }
        };
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        getActivity().setTitle("Manage Store Fragment");
        stores = new ArrayList<>();
        stores.add("addd");
        stores.add("fdsffds");
        stores.add("errrr");


        View layout = inflater.inflate(R.layout.fragment_manage_store, container, false) ;




        tv = layout.findViewById(R.id.title_textView);
        Button bt = layout.findViewById(R.id.apply_button);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), "Send", Toast.LENGTH_LONG).show();

                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.accumulate("id", "nj298@naver.com");
                    jsonObject.accumulate("pw", "1231234");
                    final String str = jsonObject.toString();

                    /*
                    Thread t = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            HttpClient client = new DefaultHttpClient();
                            String postUrl = "http://192.168.0.26:3000/mobile/user";




                            HttpPost post = new HttpPost(postUrl);
                            List params = new ArrayList();
                            params.add(new BasicNameValuePair("id", "nj298@naver.com"));
                            params.add(new BasicNameValuePair("pw", "1231234"));
                            UrlEncodedFormEntity ent = new UrlEncodedFormEntity(params, HTTP.UTF_8);
                            post.setEntity(ent);
                            HttpResponse responsePost = client.execute(post);
                            HttpEntity resEntity = responsePost.getEntity();
                            if(resEntity != null) {
                                String result = EntityUtils.toString(resEntity);
                                Message ms = new Message();
                                ms.obj = result;
                                handler.handleMessage(ms);
                            }



                        }
                    });
                    t.start();

*/

                    HttpAsyncTask httpTask = new HttpAsyncTask(jsonObject);
                    String result = httpTask.POST(SERVER, jsonObject);
                    tv.setText(result);
                } catch (JSONException e) {
                    e.printStackTrace();
                } /*catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }*/
            }
        });
















        final ListView storeList = (ListView) layout.findViewById(R.id.store_listView);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, stores);

        storeList.setAdapter(adapter);


        apply_button = (Button) layout.findViewById(R.id.apply_button);
        apply_button.setOnClickListener(mClickListener);

        storeList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                //Toast.makeText(getActivity(), (String) parent.getItemAtPosition(position), Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getActivity(), ManageStoreDetail.class);
                startActivity(intent);
            }
        });

        return layout;

    }

    View.OnClickListener mClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            // todo
            Intent intent = new Intent(getActivity(), ManageStoreDetail.class);
            startActivity(intent);
        }
    };



}
