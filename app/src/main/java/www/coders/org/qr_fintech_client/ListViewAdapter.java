package www.coders.org.qr_fintech_client;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class ListViewAdapter extends BaseAdapter{

    ArrayList<MyItem> mItems = new ArrayList<>();

    @Override
    public int getCount() {
        return mItems.size();
    }

    @Override
    public MyItem getItem(int position) {
        return mItems.get(position);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Context context = parent.getContext();

        /* 'listview_custom' Layout을 inflate하여 convertView 참조 획득 */
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.send_list_items, parent, false);
        }

        /* 'listview_custom'에 정의된 위젯에 대한 참조 획득 */
        TextView from_id = (TextView) convertView.findViewById(R.id.send_from_id_item) ;
        TextView to_id = (TextView) convertView.findViewById(R.id.send_to_id_item) ;
        TextView date = (TextView) convertView.findViewById(R.id.send_date_item);
        TextView state = (TextView) convertView.findViewById(R.id.send_state_item);
        TextView balance = (TextView) convertView.findViewById(R.id.send_balance_item) ;

        /* 각 리스트에 뿌려줄 아이템을 받아오는데 mMyItem 재활용 */
        MyItem myItem = getItem(position);

        if(myItem.getState().equals("보냄")){ //-일때는 빨간색
            to_id.setTextColor(Color.parseColor("#f96b6b"));
        }
        else {
            to_id.setTextColor(Color.parseColor("#FF2D8FD6"));
        }
        /* 각 위젯에 세팅된 아이템을 뿌려준다 */
        from_id.setText(myItem.getFromID());
        to_id.setText(myItem.getToID());
        date.setText(myItem.getDate());
        state.setText(myItem.getState());
        balance.setText(myItem.getBalance());



        /* (위젯에 대한 이벤트리스너를 지정하고 싶다면 여기에 작성하면된다..)  */


        return convertView;
    }

    /* 아이템 데이터 추가를 위한 함수. 자신이 원하는대로 작성 */
    public void addItem(String from_id, String to_id, String date, String state, String balance) {

        MyItem mItem = new MyItem();

        /* MyItem에 아이템을 setting한다. */
        mItem.setFromID(from_id);
        mItem.setToID(to_id);
        mItem.setDate(date);
        mItem.setState(state);
        mItem.setBalance(balance);

        /* mItems에 MyItem을 추가한다. */
        mItems.add(mItem);

    }

}
