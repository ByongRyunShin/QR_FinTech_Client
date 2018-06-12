package www.coders.org.qr_fintech_client;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.OnChartGestureListener;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.concurrent.ExecutionException;

public class MainScreenActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static final String my_shared_preferences = "login_information";
    SharedPreferences sharedpreferences;

    ImageView userimg;

    private ManageShopFragment manageStoreFragment;
    private ManageProductFragment manageItemFragment;
    private SendMoneyFragment sendMoneyFragment;
    private SendListFragment sendListFragment;
    private SalesListFragment salseListFragment;
    private ManageOrderFragment manageOrderFragment;


    private Calendar calendar;
    private Integer expenseTotal, incomeTotal;

    private LineChart mChart;

    private Thread thread;
   // public static final String my_shared_preferences = "login_information";
    private Integer indexSize;

    private Boolean isFabOpen = false;

    private FloatingActionButton floatingButton, buyButton, sellButton, topupButton;

    private Animation fab_open,fab_close,rotate_forward,rotate_backward;

    ///
    private String id, type, name, img_name;
    private TextView textView_id, textView_name, balance_text, expense_text, income_text;
    private ImageView user_image;
    public final static String TAG_ID = "id";
    public final static String TAG_TYPE = "type";// 개인 0, 상인 1
    public final static String TAG_USER_NAME = "name";
    final static String TAG_REUSLT = "item_code";
    public final static String TAG_USER_IMG= "img";

    ///

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /*
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true); //커스터마이징 하기 위해 필요
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(true); // 뒤로가기 버튼, 디폴트로 true만 해도 백버튼이 생김
      //  actionBar.setHomeAsUpIndicator(R.drawable.button_back); //뒤로가기 버튼을 본인이 만든 아이콘으로 하기 위해 필요
*/

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //////////////////////////////////////////////////////////////////////////////////////
        /*
        mWebView=(WebView)findViewById(R.id.WebView);
        mWebView.setWebViewClient((new WebViewClient()));
        mWebSetting = mWebView.getSettings();
        mWebSetting.setJavaScriptEnabled(true);

        mWebView.loadUrl("http://192.168.0.26:3000/about_mobile");
        */

        sharedpreferences = getSharedPreferences(my_shared_preferences, Context.MODE_PRIVATE);
        id = sharedpreferences.getString(TAG_ID, null);
        name = sharedpreferences.getString(TAG_USER_NAME, null);
        type = sharedpreferences.getString(TAG_TYPE, null);
        img_name = sharedpreferences.getString(TAG_USER_IMG, null);

        View headerLayout = navigationView.getHeaderView(0);
        textView_id = (TextView)headerLayout.findViewById(R.id.nav_header_id);
        textView_name = (TextView)headerLayout.findViewById(R.id.nav_header_name);
        user_image = (ImageView)headerLayout.findViewById(R.id.UserImage);

        balance_text = (TextView)findViewById(R.id.balance_main);
        expense_text = (TextView)findViewById(R.id.expense_main);
        income_text = (TextView)findViewById(R.id.income_main);

        calendar = Calendar.getInstance(); // 오늘 날짜 받아오기.

        String img_url = CONST.IMG_URL + img_name;
        Picasso.get().load(img_url).into(user_image);
        textView_id.setText(id);
        textView_name.setText(name + "님");

        /////////////////////////////////////////////////////////////////////////////////////

        manageStoreFragment = new ManageShopFragment();
        manageItemFragment = new ManageProductFragment();
        sendMoneyFragment = new SendMoneyFragment();
        sendListFragment = new SendListFragment();
        salseListFragment = new SalesListFragment();
        manageOrderFragment = new ManageOrderFragment();

        fab_open = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_open);
        fab_close = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_close);
        rotate_forward = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate_forward);
        rotate_backward = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate_backward);


        floatingButton = (FloatingActionButton)findViewById(R.id.fab);
        buyButton = (FloatingActionButton)findViewById(R.id.fab1); //내 QR 보여주기
        sellButton = (FloatingActionButton)findViewById(R.id.fab2); //물건 QR 찍기
        topupButton = (FloatingActionButton)findViewById(R.id.fab3); //잔액 충전하기


        floatingButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.fab:
                        if (!isFabOpen) {
                            topupButton.startAnimation(fab_open);
                            sellButton.startAnimation(fab_open);
                            buyButton.startAnimation(fab_open);
                            floatingButton.startAnimation(rotate_forward);
                            topupButton.setClickable(true);
                            sellButton.setClickable(true);
                            buyButton.setClickable(true);
                            isFabOpen = true;
                        } else {
                            topupButton.startAnimation(fab_close);
                            sellButton.startAnimation(fab_close);
                            buyButton.startAnimation(fab_close);
                            floatingButton.startAnimation(rotate_backward);
                            topupButton.setClickable(false);
                            sellButton.setClickable(false);
                            buyButton.setClickable(false);
                            isFabOpen = false;
                        }
                        break;
                }
            }
        });

        buyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainScreenActivity.this, UserShowQRActivity.class));
            }
        });

        sellButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //IntentIntegrator integrator = new IntentIntegrator(MainScreenActivity.this);
                //integrator.setCaptureActivity( qrReader.class );
                //integrator.setOrientationLocked(false);
                //integrator.initiateScan();
                Intent intent = new Intent(MainScreenActivity.this, UserBuyActivity.class);
                startActivity(intent);
                finish();
            }
        });

        topupButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                startActivity(new Intent(MainScreenActivity.this, TopUpActivity.class));
            }
        });


        ////////chart data ///////////

        mChart = (LineChart)findViewById(R.id.linechart) ;

        //   mChart.setOnChartGestureListener(false);
        //  mChart.setOnChartValueSelectedListener(ChartActivity.this);

        mChart.setDragEnabled(true);
        mChart.setScaleEnabled(false);

        ArrayList<Entry> yValues = new ArrayList<>();
        //  ArrayList<String> labels = new ArrayList<String>();

        String[] labels = new String[1000];


        SharedPreferences sp = this.getSharedPreferences(my_shared_preferences, Context.MODE_PRIVATE);

        String loginID = sp.getString("id", null);
        String password = sp.getString("pw", null);


        // 잔액 받아오기

        JSONObject jsonObject = new JSONObject();
        try {
            //로그인 아이디 받아오기

            jsonObject.accumulate("id", loginID);
            jsonObject.accumulate("pw", password);
            HttpAsyncTask httpTask = new HttpAsyncTask(jsonObject);

            String result = httpTask.execute(this.getString(R.string.server_ip) + "/user/").get();

            JSONObject json = new JSONObject(result);

            String result1 = json.getString("result");

            if (result1.equals("1")) {


                String balance1 = json.getString("balance");

                String formattedBal = moneyFormatToWon(balance1);

                balance_text.setText(formattedBal + "원");

            } else {

                String msg = json.getString("msg");
                Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();

            }


        } catch (JSONException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();

        }


        //일일 수입, 일일 지출 받아오기

        JSONObject jsonObject2 = new JSONObject();
        try {

            //  jsonObject.accumulate("id", id_text.getText());
            jsonObject2.accumulate("id", loginID);
            jsonObject2.accumulate("pw", password);
            HttpAsyncTask httpTask = new HttpAsyncTask(jsonObject2);

            String result = httpTask.execute(this.getString(R.string.server_ip) + "/send/list/" + loginID).get();

            JSONObject json = new JSONObject(result);

            JSONArray rows = json.getJSONArray("rows");

            String result1 = json.getString("result");


            if (result1.equals("1")) {

                ListViewAdapter mAdapter = new ListViewAdapter();

                indexSize = rows.length() - 1;

                //0으로 초기화 해주기//
                expenseTotal = 0;
                incomeTotal = 0;

                for(int i = 0; i < rows.length(); i++) {

                    String from_user_id = rows.getJSONObject(i).getString("from_user_id");
                    String to_user_id = rows.getJSONObject(i).getString("to_user_id");
                    String date = rows.getJSONObject(i).getString("time");
                    String bal = rows.getJSONObject(i).getString("to_result"); //수정하기
                    String value = rows.getJSONObject(i).getString("value"); //수정하기
                    String from_val = rows.getJSONObject(i).getString("from_result");


                    String tmp_val = moneyFormatToWon(value);


                    Integer value_tmp;
                    value_tmp = Integer.parseInt(value);

                    if(from_val.equals("-1")){ // 충전 (채움)
                                //신경 쓰지 말기!
                    }
                    else { //받음 | 보냄

                        if (from_user_id.equals(loginID)) { //보냄 (지출)
                            bal = from_val;

                            if(isSameDate(date)) { //참일 때 -> 일일 지출 내역에 추가
                                expenseTotal += value_tmp;
                            }

                        } else if (to_user_id.equals(loginID)) {//받음 (수입)
                            if(isSameDate(date)) { //참일 때 -> 일일 수입 내역에 추가
                                incomeTotal += value_tmp;

                            }
                        }
                    }


                    labels[i] = makeDate(date);
                    //labels.add(makeDate(date));
                    yValues.add(new Entry(i, Float.parseFloat(bal)));
                }

                expense_text.setText("-" + moneyFormatToWon(Integer.toString(expenseTotal)) + "원");
                income_text.setText("+" + moneyFormatToWon(Integer.toString(incomeTotal)) + "원");

            } else {

                String msg = json.getString("msg");
                Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();

            }


        } catch (JSONException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();

        }


        /// chart data
        LineDataSet set = new LineDataSet(yValues, null);

        set.setFillAlpha(110);
        set.setDrawCircles(true);
        /////테마 설정 ////////////
        set.setAxisDependency(YAxis.AxisDependency.LEFT);
        set.setColor(ColorTemplate.getHoloBlue());
        set.setCircleColor(ColorTemplate.getHoloBlue());
        set.setLineWidth(3f);
        set.setCircleRadius(5f);
        set.setFillAlpha(65);
        set.setFillColor(ColorTemplate.getHoloBlue());
        //  set.enableDashedLine(10f, 5f, 0f);
        set.setHighlightEnabled(false);
        // set.enableDashedHighlightLine(10f, 5f, 0f);
        // set.setHighLightColor(Color.RED);
        set.setValueTextSize(9f);
        set.setDrawValues(true);
        set.setDrawFilled(true);
        set.setValueTextColor(Color.BLACK);
        //set.setDrawCubic(true);

        set.setMode(LineDataSet.Mode.CUBIC_BEZIER); //곡선으로 그려지게
        ////////////////////////////

        MyYAxisValueFormatter myYAxisValueFormatter = new MyYAxisValueFormatter();
        MyXAxisValueFormatter myXAxisValueFormatter = new MyXAxisValueFormatter(labels);

        XAxis xAxis = mChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTextSize(10f);
        xAxis.setDrawGridLines(false);
        xAxis.setGranularity(1); // minimum axis-step (interval) is 1
        xAxis.setValueFormatter(myXAxisValueFormatter);


        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.setValueFormatter(myYAxisValueFormatter);
        leftAxis.setDrawGridLines(false);
        leftAxis.setAxisMinimum(0);
        leftAxis.setEnabled(false);
        YAxis rightAxis = mChart.getAxisRight();
        rightAxis.setEnabled(false);




        //   LineData data = new LineData(labels, set);

        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(set);


        mChart.animateXY(2000, 2500); //animation 효과 , x , y

        mChart.getDescription().setText(""); //description label 글씨 없애기



        LineData data = new LineData(dataSets);
        mChart.setData(data);
        mChart.getLegend().setEnabled(false);
        mChart.setVisibleXRangeMaximum(5); //최대 5개만 화면에 보내게
        mChart.moveViewToX(indexSize);

        ////////chart data ///////////

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        // QR코드/ 바코드를 스캔한 결과
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null){
            Intent intent = new Intent(MainScreenActivity.this, UserBuyActivity.class);
            intent.putExtra(TAG_REUSLT, result.getContents());
            startActivity(intent);

            Toast.makeText(getApplicationContext(), result.getContents(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_screen, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        if (id == R.id.nav_manageStore) {
            transaction.replace(R.id.container, manageStoreFragment);
        } else if (id == R.id.nav_manageItem) {
            transaction.replace(R.id.container, manageItemFragment);
        } else if (id == R.id.nav_sendMoney) {
            transaction.replace(R.id.container, sendMoneyFragment);
        } else if (id == R.id.nav_sendList) {
            transaction.replace(R.id.container, sendListFragment);
        } else if (id == R.id.nav_salseList) {
            transaction.replace(R.id.container, salseListFragment);
        } else if (id == R.id.nav_manageOrder) {
            transaction.replace(R.id.container, manageOrderFragment);
        }else if (id == R.id.nav_shoppingList) {
            Intent intent = new Intent(MainScreenActivity.this, CartActicity.class);
            finish();
            startActivity(intent);
        }


        transaction.addToBackStack(null);
        transaction.commit();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    //string -> date 형태로
    private String makeDate(String date) {

        String newDate;

        String YY, MM, DD,TT, TH, TM;

        //2018-05-29T10:25:04.000Z

        YY = date.split("-")[0];
        MM = date.split("-")[1];
        DD = date.split("-")[2].split("T")[0];

        TT = date.split("T")[1];
        TH = TT.split(":")[0];
        TM = TT.split(":")[1];

        //2018.05.29 10:25 | 받음
        //newDate = YY + "." + MM + "." + DD + " " + TH + ":" + TM  + " | ";
        //newDate = MM +"/"+ DD + " " + TH + ":" + TM  ;
        newDate = MM +"/"+ DD ;

        return newDate;
    }

    //일일 수입, 일일 지출을 구하기 위해 사용
    private boolean isSameDate(String date){

        String YY, MM, DD;

        YY = date.split("-")[0];
        MM = date.split("-")[1];
        DD = date.split("-")[2].split("T")[0];

       if(Calendar.YEAR == Integer.parseInt(YY) && (Calendar.MONTH+1) == Integer.parseInt(MM) && Calendar.DATE == Integer.parseInt(DD))
            return true;
        else return false;
    }

    //천의 자리마다 콤마 표시하는 메소드
    public static String moneyFormatToWon(String inputMoney) {
        NumberFormat nf = NumberFormat.getNumberInstance();
        Log.d("Money Value", inputMoney);
        String formattedMoney = (String)nf.format(Long.parseLong(inputMoney));
        return formattedMoney;
    }

}