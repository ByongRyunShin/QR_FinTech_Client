package www.coders.org.qr_fintech_client;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import java.util.ArrayList;

public class OrderListAdapter extends BaseAdapter {
    Context context;
    ArrayList<OrderObject> products;
    TextView pName_textView, price_textView;
    Button decrease_button, increase_button;
    EditText quantity_editText;
    static ManageOrderFragment main;

    public OrderListAdapter(Context context, ArrayList<OrderObject> list, ManageOrderFragment main) {
        this.context = context;
        this.products = list;
        this.main = main;
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
            convertView = LayoutInflater.from(context).inflate(R.layout.order_list_item, null);
        }
        final OrderObject order = (OrderObject) getItem(position);
        final TextView pName_textView = (TextView) convertView.findViewById(R.id.pName_textView);
        final TextView price_textView = (TextView) convertView.findViewById(R.id.price_textView);
        final EditText quantity_editText = (EditText) convertView.findViewById(R.id.quantity_editText);
        final Button increase_button = (Button) convertView.findViewById(R.id.increase_button);
        final Button decrease_button = (Button) convertView.findViewById(R.id.decrease_button);

        pName_textView.setText(order.getpName());
        //pName_textView.setText(products.get(position).getpName());
        price_textView.setText(order.getPrice());

        increase_button.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                int q = order.getQuantity();
                if (q < 100000) quantity_editText.setText(Integer.toString(q + 1));
            }
        });

        decrease_button.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {

                int q = order.getQuantity();
                if (q > 0) quantity_editText.setText(Integer.toString(q - 1));
            }
        });

        quantity_editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().equals("")) return;
                int before = order.getQuantity();
                int after = Integer.parseInt(s.toString());
                order.setQuantity(after);
                int change = (after - before) * Integer.parseInt(order.getPrice());
                main.setTotal(change);
            }
        });
        return convertView;
    }
}
