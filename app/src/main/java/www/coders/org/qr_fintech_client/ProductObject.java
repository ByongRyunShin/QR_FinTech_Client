package www.coders.org.qr_fintech_client;

import org.json.JSONException;
import org.json.JSONObject;

public class ProductObject {
    private String num, name, price, img, init_date, del_date, owner_id, owner_shop, isDelete;
    //??
    @Override
    public String toString() {
        return name + "\t\t" + price + "\\";
    }


    public ProductObject(JSONObject jsonObject) throws JSONException {
        this.num = jsonObject.getString("num");
        this.name = jsonObject.getString("name");
        this.price = jsonObject.getString("price");

        //// 정보없는거넘어오면 안됨
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getInit_date() {
        return init_date;
    }

    public void setInit_date(String init_date) {
        this.init_date = init_date;
    }

    public String getDel_date() {
        return del_date;
    }

    public void setDel_date(String del_date) {
        this.del_date = del_date;
    }

    public String getOwner_id() {
        return owner_id;
    }

    public void setOwner_id(String owner_id) {
        this.owner_id = owner_id;
    }

    public String getOwner_shop() {
        return owner_shop;
    }

    public void setOwner_shop(String owner_shop) {
        this.owner_shop = owner_shop;
    }

    public String getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(String isDelete) {
        this.isDelete = isDelete;
    }
}
