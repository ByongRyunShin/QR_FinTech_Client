package www.coders.org.qr_fintech_client;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class ProductListAdapter extends BaseAdapter{
    Context context;
    ArrayList<ProductObject> products;
    TextView pName_textView, price_textView, name_textView;

    public ProductListAdapter(Context context, ArrayList<ProductObject> list) {
        this.context = context;
        this.products = list;
    }

    @Override
    public int getCount() { return this.products.size(); }

    @Override
    public Object getItem(int position) { return products.get(position); }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.product_list_item, null);

            pName_textView = (TextView) convertView.findViewById(R.id.pName_textView);
            price_textView = (TextView) convertView.findViewById(R.id.price_textView);
            name_textView = (TextView) convertView.findViewById(R.id.name_textView);
        }

        String temp = products.get(position).getpName();
        pName_textView.setText(temp);
        //pName_textView.setText(products.get(position).getpName());
        price_textView.setText(products.get(position).getPrice());
        name_textView.setText(products.get(position).getName());

        return convertView;
    }
}
