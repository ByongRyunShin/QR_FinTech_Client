package www.coders.org.qr_fintech_client;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class ChartActivity extends AppCompatActivity {


    private LineChart mChart;
    private Thread thread;
    public static final String my_shared_preferences = "login_information";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart);

        mChart = (LineChart)findViewById(R.id.linechart) ;

     //   mChart.setOnChartGestureListener(false);
      //  mChart.setOnChartValueSelectedListener(ChartActivity.this);

        mChart.setDragEnabled(true);
        mChart.setScaleEnabled(false);

        ArrayList<Entry> yValues = new ArrayList<>();
        ArrayList<String> labels = new ArrayList<String>();

        SharedPreferences sp = this.getSharedPreferences(my_shared_preferences, Context.MODE_PRIVATE);

        String loginID = sp.getString("id", null);
        String password = sp.getString("pw", null);




        //잔액 가져오기

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

                for(int i = 0; i < rows.length(); i++) {

                    String from_user_id = rows.getJSONObject(i).getString("from_user_id");
                    String to_user_id = rows.getJSONObject(i).getString("to_user_id");
                    String date = rows.getJSONObject(i).getString("time");
                    String bal = rows.getJSONObject(i).getString("to_result"); //수정하기
                    String value = rows.getJSONObject(i).getString("value"); //수정하기
                    String from_val = rows.getJSONObject(i).getString("from_result");

                    //Float newDate = Float.parseFloat(makeDate(date));

                    if(from_val.equals("-1")){ // 충전 (채움)

                    }
                    else{ //받음 | 보냄

                        if(from_user_id.equals(loginID)){ //보냄
                            bal = from_val;
                        }
                        //받음
                    }
                   labels.add(makeDate(date));
                    yValues.add(new Entry(i, Float.parseFloat(bal)));
                }

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

        LineDataSet set = new LineDataSet(yValues, "잔액");

        set.setFillAlpha(110);
        set.setDrawCircles(true);
        /////테마 설정 ////////////
        set.setAxisDependency(YAxis.AxisDependency.LEFT);
        set.setColor(ColorTemplate.getHoloBlue());
        set.setCircleColor(Color.BLUE);
        set.setLineWidth(3f);
        set.setCircleRadius(4f);
        set.setFillAlpha(65);
        set.setFillColor(ColorTemplate.getHoloBlue());
        set.enableDashedLine(10f, 5f, 0f);
        set.setHighlightEnabled(false);
       // set.enableDashedHighlightLine(10f, 5f, 0f);
       // set.setHighLightColor(Color.RED);
        set.setValueTextSize(9f);
        set.setDrawValues(true);
        set.setDrawFilled(true);
        set.setValueTextColor(Color.BLACK);

        ////////////////////////////

        MyYAxisValueFormatter myYAxisValueFormatter = new MyYAxisValueFormatter();


        XAxis xAxis = mChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTextSize(10f);
        xAxis.setDrawGridLines(false);

        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.setValueFormatter(myYAxisValueFormatter);
        leftAxis.setDrawGridLines(false);
        leftAxis.setAxisMinimum(0);
        YAxis rightAxis = mChart.getAxisRight();
        rightAxis.setEnabled(false);

      //  LineData data = new LineData(labels, set);

        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(set);
        mChart.animateXY(2000, 2000);

        mChart.getDescription().setText("");



        LineData data = new LineData(dataSets);
        mChart.setData(data);
        ///여기서부터 주석처리

        /*
        mChart.animateX(1000);

        XAxis xAxis = mChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTextSize(10f);
        xAxis.setDrawGridLines(false);

        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.setDrawGridLines(false);

        YAxis rightAxis = mChart.getAxisRight();
        rightAxis.setEnabled(false);

        LineData data = new LineData();
        mChart.setData(data);
        */
        //////////////



    //    feedMultiple();

    }

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
        newDate = MM +"/"+ DD;

        return newDate;
    }


    private void addEntry(){
        LineData data = mChart.getData();

        if(data != null){
            ILineDataSet set = data.getDataSetByIndex(0);

            if(set == null){
                set = createSet();
                data.addDataSet(set);
            }

            data.addEntry(new Entry(set.getEntryCount(), 30f), 0);
           // data.addEntry(new Entry(20f, 40f), 1);
           // data.addEntry(new Entry(20f, 40f), 2);

            //   data.addEntry(new Entry(set.getEntryCount(), (float)(Math.random()*40 ) + 30f), 0);
            data.notifyDataChanged();

            mChart.notifyDataSetChanged();
            mChart.setVisibleXRangeMaximum(10);
            mChart.moveViewToX(data.getEntryCount());
        }
    }

    private LineDataSet createSet(){
        LineDataSet set = new LineDataSet(null, "잔액");
        set.setAxisDependency(YAxis.AxisDependency.LEFT);
        set.setColor(ColorTemplate.getHoloBlue());
        set.setCircleColor(Color.WHITE);
        set.setLineWidth(2f);
        set.setCircleRadius(4f);
        set.setFillAlpha(65);
        set.setFillColor(ColorTemplate.getHoloBlue());
        set.setHighLightColor(Color.rgb(244,117,117));
        set.setValueTextColor(Color.WHITE);
        set.setValueTextSize(9f);
        set.setDrawValues(false);
        return set;
    }

    private void feedMultiple(){
        if(thread != null){
            thread.interrupt();
        }

        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                addEntry();
            }
        };

        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while(true){
                    runOnUiThread(runnable);

                    try{
                        thread.sleep(100);
                    }
                    catch (InterruptedException ie){
                        ie.printStackTrace();
                    }
                }
            }
        });

        thread.start();

    }


    @Override
    protected void onPause(){
        super.onPause();
        if(thread != null)
            thread.interrupt();
    }


}

