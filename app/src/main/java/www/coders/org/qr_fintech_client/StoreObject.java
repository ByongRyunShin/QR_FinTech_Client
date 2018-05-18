package www.coders.org.qr_fintech_client;

public class StoreObject {
    private int num;
    private String name, marketplace, about;
    private int balance;

    public StoreObject(int num, String name, String marketplace, String about, int balance) {
        this.num = num;
        this.name = name;
        this.marketplace = marketplace;
        this.about = about;
        this.balance = balance;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMarketplace() {
        return marketplace;
    }

    public void setMarketplace(String marketplace) {
        this.marketplace = marketplace;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }
}
