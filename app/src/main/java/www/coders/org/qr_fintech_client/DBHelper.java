package www.coders.org.qr_fintech_client;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {

    private Context context;
    private static final int dbVersion = 1;
    private static final String dbName = "DGBDB";

    private static DBHelper INSTANCE;
    private static SQLiteDatabase mDb;

    public static DBHelper getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new DBHelper(context.getApplicationContext());
            mDb = INSTANCE.getWritableDatabase();
        }

        return INSTANCE;
    }

    public void open() {
        if (mDb.isOpen() == false) {
            INSTANCE.onOpen(mDb);
        }
    }

    public DBHelper(Context context) {
        super(context, dbName, null, dbVersion);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        StringBuffer sb = new StringBuffer();
        sb.append(" CREATE TABLE SHOPPING_LIST ( ");
        sb.append(" _ID INTEGER PRIMARY KEY AUTOINCREMENT, ");
        sb.append(" ITEM_NUM INTEGER, ");
        sb.append(" ITEM_NAME TEXT, ");
        sb.append(" PRICE INTEGER, ");
        sb.append(" INIT_DATE TEXT, ");
        sb.append(" OWNER_ID TEXT, ");
        sb.append(" OWNER_SHOP INTEGER, ");
        sb.append(" COUNT INTEGER, ");
        sb.append(" USER_ID TEXT, ");
        sb.append(" IMG_NAME TEXT, ");
        sb.append(" BUY_DATE TEXT ) ");
        // SQLite Database로 쿼리 실행
        db.execSQL(sb.toString());
        Log.e("db", "Table 생성완료");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void addShoppingList(CartObject shopping_item) {
        SQLiteDatabase db = getWritableDatabase();
        StringBuffer sb = new StringBuffer();
        sb.append(" INSERT INTO SHOPPING_LIST ( ");
        sb.append(" ITEM_NUM, ITEM_NAME, PRICE, INIT_DATE, OWNER_ID, OWNER_SHOP, COUNT, USER_ID, IMG_NAME, BUY_DATE ) ");
        sb.append(" VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ? ) ");

        ItemObject item = shopping_item.getItem();
        db.execSQL(sb.toString(),
                new Object[]{item.getItem_num(),
                        item.getItem_name(),
                        item.getPrice(),
                        item.getInit_date(),
                        item.getOwner_id(),
                        item.getOwner_shop(),
                        shopping_item.getCount(),
                        shopping_item.getUser_id(),
                        item.getImage_name(),
                        shopping_item.getBuy_date()});
    }

    public void removeItem(String date){
        SQLiteDatabase db = getWritableDatabase();
        String s = " DELETE FROM SHOPPING_LIST WHERE BUY_DATE = '" + date + "'";
        db.execSQL(s);
    }

    public void testDb() {
        SQLiteDatabase db = getReadableDatabase();
    }

    public List getShoppingList(String user_id) {
        StringBuffer sb = new StringBuffer();
        sb.append(" SELECT * FROM SHOPPING_LIST WHERE USER_ID = '"); // 읽기 전용 DB 객체를 만든다.
        sb.append(user_id);
        sb.append("' ");

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(sb.toString(), null);
        List shoppingList = new ArrayList();
        CartObject cartObject = null; // moveToNext 다음에 데이터가 있으면 true 없으면 false
        while (cursor.moveToNext()) {
            int item_num = cursor.getInt(1);
            String item_name = cursor.getString(2);
            int price = cursor.getInt(3);
            String init_date  = cursor.getString(4);
            String owner_id  = cursor.getString(5);
            int owner_shop  = cursor.getInt(6);
            String image_name = cursor.getString(9);
            ItemObject item = new ItemObject(item_num,item_name,price,init_date,owner_id,owner_shop,image_name);

            int count = cursor.getInt(7);
            String buy_date = cursor.getString(10);
            cartObject = new CartObject(item,count,user_id,buy_date);
            shoppingList.add(cartObject);
        }

        return shoppingList;
    }

}
