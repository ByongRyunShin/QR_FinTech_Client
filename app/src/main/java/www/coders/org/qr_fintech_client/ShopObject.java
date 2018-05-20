package www.coders.org.qr_fintech_client;
import org.json.JSONException;
import org.json.JSONObject;

public class ShopObject {
    /*
    private int num;
    private String name, marketplace, about;
    private int balance;
    */
    String num, userId, name, about, marketplace, img, balance, isDelete;

    @Override
    public String toString() {
        return name + " / " + num;
    }


    public ShopObject(JSONObject jsonObject) throws JSONException {
        this.num = jsonObject.getString("num");
        this.name = jsonObject.getString("name");
        this.about = jsonObject.getString("about");

        //// 정보없는거넘어오면 안됨
    }

    public ShopObject(String num, String userId, String name, String about, String marketplace, String img, String balance, String isDelete) {
        this.num = num;
        this.userId = userId;
        this.name = name;
        this.about = about;
        this.marketplace = marketplace;
        this.img = img;
        this.balance = balance;
        this.isDelete = isDelete;
    }


    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public String getMarketplace() {
        return marketplace;
    }

    public void setMarketplace(String marketplace) {
        this.marketplace = marketplace;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(String isDelete) {
        this.isDelete = isDelete;
    }
}
