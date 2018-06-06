package www.coders.org.qr_fintech_client;

import org.json.JSONException;
import org.json.JSONObject;

public class OrderObject extends ProductObject {

    int quantity;

    public OrderObject(String pName, String price, String name, int quantity) {
        super(pName, price, name);
        this.quantity = quantity;
    }

    public OrderObject(JSONObject jsonObject) throws JSONException {
        super(jsonObject);
        quantity = 0;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        if (quantity > 0 && quantity < 100000) this.quantity = quantity;
    }

    public int increaseQuantity() {
        if (quantity < 100000) quantity++;
        return quantity;
    }

    public int decreaseQuantity() {
        if (quantity > 0) quantity--;
        return quantity;
    }
}
