package www.coders.org.qr_fintech_client;

import java.text.SimpleDateFormat;
import java.util.Date;

class MyItem {

    private String FromID;
    private String date;
    private String state;
    private String money;
    private String balance;
    private String ToID;

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getFromID() {
        return FromID;
    }

    public void setFromID(String fromID) {
        FromID = fromID;
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

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getToID() {
        return ToID;
    }

    public void setToID(String toID) {
        ToID = toID;
    }
}
