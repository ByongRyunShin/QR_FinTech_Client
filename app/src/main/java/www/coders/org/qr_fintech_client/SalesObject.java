package www.coders.org.qr_fintech_client;

import org.json.JSONException;
import org.json.JSONObject;

public class SalesObject {
    String sNum;
    String num;
    String buyer;
    String price;
    String quantity;
    String date;
    String pNum;
    String pName;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    String name;

    public SalesObject(JSONObject jsonObject) throws JSONException {
        sNum = jsonObject.getString("num");
        num = jsonObject.getString("shop_id");
        buyer = jsonObject.getString("buyer_id");
        price = jsonObject.getString("price");
        quantity = jsonObject.getString("quantity");
        pNum = jsonObject.getString("pNum");
        setDate(jsonObject.getString("date"));
        //// 정보없는거넘어오면 안됨
    }

    public String getpName() {
        return pName;
    }

    public void setpName(String name) {
        this.pName = name;
    }

    public String getsNum() {
        return sNum;
    }

    public void setsNum(String sNum) {
        this.sNum = sNum;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getBuyer() {
        return buyer;
    }

    public void setBuyer(String buyer) {
        this.buyer = buyer;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        String YY, MM, DD,TT, TH, TM;
        String newDate;

        //2018-05-29T10:25:04.000Z

        YY = date.split("-")[0];
        MM = date.split("-")[1];
        DD = date.split("-")[2].split("T")[0];

        TT = date.split("T")[1];
        TH = TT.split(":")[0];
        TM = TT.split(":")[1];

        //2018.05.29 10:25 | 받음
        newDate = YY + "." + MM + "." + DD + " " + TH + ":" + TM  + " | ";
        this.date = newDate;
    }

    public String getpNum() {
        return pNum;
    }

    public void setpNum(String pNum) {
        this.pNum = pNum;
    }
}
