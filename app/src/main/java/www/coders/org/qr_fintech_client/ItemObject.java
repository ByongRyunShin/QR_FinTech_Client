package www.coders.org.qr_fintech_client;

public class ItemObject {
    private int item_num;
    private String item_name;
    private int price;
    private String init_date;
    private String owner_id;
    private int owner_shop;
    private String image_name;

    public ItemObject(int item_num, String item_name, int price, String init_date, String owner_id, int owner_shop, String image_name) {
        this.item_num = item_num;
        this.item_name = item_name;
        this.price = price;
        this.init_date = init_date;
        this.owner_id = owner_id;
        this.owner_shop = owner_shop;
        this.image_name = image_name;
    }

    public int getItem_num() {
        return item_num;
    }

    public void setItem_num(int item_num) {
        this.item_num = item_num;
    }

    public String getItem_name() {
        return item_name;
    }

    public void setItem_name(String item_name) {
        this.item_name = item_name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getInit_date() {
        return init_date;
    }

    public void setInit_date(String init_date) {
        this.init_date = init_date;
    }

    public String getOwner_id() {
        return owner_id;
    }

    public void setOwner_id(String owner_id) {
        this.owner_id = owner_id;
    }

    public int getOwner_shop() {
        return owner_shop;
    }

    public void setOwner_shop(int owner_shop) {
        this.owner_shop = owner_shop;
    }


    public String getImage_name() {
        return image_name;
    }

    public void setImage_name(String image_name) {
        this.image_name = image_name;
    }
}
