package www.coders.org.qr_fintech_client;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class ShopListAdpater extends RecyclerView.Adapter<ShopListAdpater.ViewHolder> {

    private Context mContext;
    private List<ShoppingListObject> mDataSet;
    private int selectedPosition = -1;
    private TextView cancel, payment, delete;

    public ShopListAdpater(Context context, List<ShoppingListObject> dataSet) {
        mContext = context;
        mDataSet = dataSet;
    }

    @Override public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.shopping_list_item, parent, false);
        return new ViewHolder(itemView);
    }

    @Override public void onBindViewHolder(ViewHolder holder, final int position) {
        Picasso.get().load(R.drawable.eximg).into(holder.image);
        holder.name.setText(mDataSet.get(position).getItem().getItem_name());
        holder.date.setText(mDataSet.get(position).getBuy_date());

        int total_price = mDataSet.get(position).getItem().getPrice() * mDataSet.get(position).getCount();
        holder.price.setText(Integer.toString(total_price));
        holder.count.setText( Integer.toString(mDataSet.get(position).getCount()));

        cancel = (TextView) ((Activity) mContext).findViewById(R.id.shopping_list_cancel);
        payment = (TextView) ((Activity) mContext).findViewById(R.id.shopping_item_payment);
        delete = (TextView) ((Activity) mContext).findViewById(R.id.shopping_list_del);

        if(selectedPosition == -1){
            cancel.setVisibility(View.GONE);
            payment.setVisibility(View.GONE);
            delete.setVisibility(View.GONE);
        }

        if(selectedPosition == position){
            holder.genre.setBackgroundResource(R.color.colorPoint);
            cancel.setVisibility(View.VISIBLE);
            payment.setVisibility(View.VISIBLE);
            delete.setVisibility(View.VISIBLE);
        }else{
            holder.genre.setBackgroundResource(R.color.colorWhite);
        }

        holder.genre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedPosition = position;
                notifyDataSetChanged();
            }
        });
    }

    @Override public int getItemCount() {
        return mDataSet.size();
    }

    public void remove(int position) {
        if(position == -1) return;
        mDataSet.remove(position);
        notifyItemRemoved(position);
    }

    public void add(ShoppingListObject shoppingListObject, int position) {
        mDataSet.add(position, shoppingListObject);
        notifyItemInserted(position);
    }

    public ShoppingListObject getItem(int position){
        if(position != -1) return mDataSet.get(position);
        else return null;
    }

    public int getSelectedPosition() {
        return selectedPosition;
    }

    public void setSelectedPosition(int selectedPosition) {
        notifyDataSetChanged();
        this.selectedPosition = selectedPosition;
    }

    public List<ShoppingListObject> getmDataSet(){
        return mDataSet;
    }
    static class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView image;
        public TextView name, price, count, date;
        public LinearLayout genre;

        public ViewHolder(View itemView) {
            super(itemView);
            image = (ImageView) itemView.findViewById(R.id.shopping_list_item_img);
            name = (TextView) itemView.findViewById(R.id.shopping_list_item_name);
            price = (TextView) itemView.findViewById(R.id.shopping_list_item_price);
            count = (TextView) itemView.findViewById(R.id.shopping_list_item_count);
            date = (TextView) itemView.findViewById(R.id.shopping_list_item_date);
            genre = (LinearLayout)itemView.findViewById(R.id.shopping_item_genre);
        }
    }


}

