package www.coders.org.qr_fintech_client;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ProductObject {

    private String num, name, price, img, init_date, del_date, owner_id, pNum, isDelete, pName;

    public ProductObject(String pName, String price, String name) {
        this.pName = pName;
        this.price = price;
        this.name = name;
    }
    public ProductObject(JSONObject jsonObject) throws JSONException {
        this.pNum = jsonObject.getString("num");
        this.num = jsonObject.getString("owner_shop");
        this.pName = jsonObject.getString("name");
        this.price = jsonObject.getString("price");
        //    this.name = jsonObject.getString("name");

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

    public String getpNum() {
        return pNum;
    }

    public void setpNum(String pNum) {
        this.pNum = pNum;
    }

    public String getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(String isDelete) {
        this.isDelete = isDelete;
    }

    public String getpName() {
        return pName;
    }

    public void setpName(String pName) {
        this.pName = pName;
    }

}
