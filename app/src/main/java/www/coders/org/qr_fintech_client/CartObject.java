package www.coders.org.qr_fintech_client;

public class CartObject {
    private ItemObject item;
    private int count;
    private String user_id;
    private String buy_date;

    public CartObject(ItemObject item, int count, String user_id, String buy_date) {
        this.item = item;
        this.count = count;
        this.user_id = user_id;
        this.buy_date = buy_date;
    }

    public void setItem(ItemObject item) {
        this.item = item;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public void setBuy_date(String buy_date) {
        this.buy_date = buy_date;
    }

    public ItemObject getItem() {
        return item;
    }

    public int getCount() {
        return count;
    }

    public String getBuy_date() {
        return buy_date;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

}
