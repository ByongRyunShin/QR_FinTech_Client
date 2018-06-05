package www.coders.org.qr_fintech_client;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class SalesListAdapter extends BaseAdapter{

    Context context;
    ArrayList<SalesObject> list_itemArrayList;
    TextView price_textView, quantity_textView, total_textView, name_textView, buyer_textView, date_textView, pName_textView;

    public SalesListAdapter(Context context, ArrayList<SalesObject> list_itemArrayList) {
        this.context = context;
        this.list_itemArrayList = list_itemArrayList;
    }


    @Override
    public int getCount() {
        return this.list_itemArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return list_itemArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.fragment_sales_list, null);
            pName_textView = (TextView) convertView.findViewById(R.id.pName_textView);
            price_textView = (TextView) convertView.findViewById(R.id.price_textView);
            quantity_textView = (TextView) convertView.findViewById(R.id.quantity_textView);
            total_textView = (TextView) convertView.findViewById(R.id.total_textView);
            name_textView = (TextView) convertView.findViewById(R.id.name_textView);
            buyer_textView = (TextView) convertView.findViewById(R.id.buyer_textView);
            date_textView = (TextView) convertView.findViewById(R.id.date_textView);
        }

        String price = list_itemArrayList.get(position).getPrice();
        String quantity = list_itemArrayList.get(position).getAmount();
        String total = Integer.toString(Integer.parseInt(price) * Integer.parseInt(quantity));

        //pName_textView.setText(list_itemArrayList.get(position).getpName());
        pName_textView.setText(list_itemArrayList.get(position).getpNum());

        price_textView.setText(price);
        quantity_textView.setText(quantity);
        total_textView.setText(total);

        //name_textView.setText(list_itemArrayList.get(position).getName());
        name_textView.setText(list_itemArrayList.get(position).getNum());

        buyer_textView.setText(list_itemArrayList.get(position).getBuyer());
        date_textView.setText(list_itemArrayList.get(position).getDate());
        return convertView;
    }
}
