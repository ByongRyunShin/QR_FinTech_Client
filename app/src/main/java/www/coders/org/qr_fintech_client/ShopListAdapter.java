package www.coders.org.qr_fintech_client;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.text.NumberFormat;
import java.util.ArrayList;

public class ShopListAdapter extends BaseAdapter{
    Context context;
    ArrayList<ShopObject> shops;

    public ShopListAdapter(Context context, ArrayList<ShopObject> list) {
        this.context = context;
        this.shops = list;
    }

    @Override
    public int getCount() { return this.shops.size(); }

    @Override
    public Object getItem(int position) { return shops.get(position); }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.shop_list_item, null);

        }

        final TextView name_textView = (TextView) convertView.findViewById(R.id.name_textView);
        final TextView balance_textView = (TextView) convertView.findViewById(R.id.balance_textView);
        final TextView about_textView = (TextView) convertView.findViewById(R.id.about_textView);


        name_textView.setText(shops.get(position).getName());
        balance_textView.setText(shops.get(position).getBalance() + " 원");
        about_textView.setText(shops.get(position).getAbout());

        return convertView;
    }

}
