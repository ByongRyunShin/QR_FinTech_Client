package www.coders.org.qr_fintech_client;

public class StoreObject {
    private String name, place, about;
    private int balance;

    public StoreObject(String name, String place, String about, int balance) {
        this.name = name;
        this.place = place;
        this.about = about;
        this.balance = balance;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
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
