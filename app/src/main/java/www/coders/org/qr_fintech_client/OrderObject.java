package www.coders.org.qr_fintech_client;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class OrderObject extends ProductObject implements Serializable {

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

    public void setQuantity(int q) {
        if (q >= 0 && q <= 100000) quantity = q;
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
