package www.coders.org.qr_fintech_client;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MyXAxisValueFormatter implements IAxisValueFormatter {

    private String[] mValues;

    public MyXAxisValueFormatter(String[] values){
        this.mValues = values;
    }

    @Override
    public String getFormattedValue(float value, AxisBase axis){
        return mValues[(int)value];
    }

    /*
    @Override
    public String getFormattedValue(float unixSeconds, AxisBase axis) {
        Date date = new Date((long) unixSeconds);
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd", Locale.ENGLISH);
        return sdf.format(date);
    }*/
}