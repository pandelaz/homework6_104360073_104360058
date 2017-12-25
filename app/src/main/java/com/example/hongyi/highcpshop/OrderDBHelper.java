package com.example.hongyi.highcpshop;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import static android.content.ContentValues.TAG;

/**
 * Created by hongyi on 2017/12/8.
 */
public class OrderDBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "HighCP.db";
    private static final int DATABASE_VERSION = 1;

    public String TABLE_NAME = "Order";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_SHOPNAME = "shopname";
    public static final String COLUMN_CUSTOMERNAME = "customername";
    public static final String COLUMN_PRODUCTNAME = "productname";
    public static final String COLUMN_QUANTITY = "quantity";
    public static final String COLUMN_MONEY = "money";
    public static final String COLUMN_TOTALMONEY = "totalmoney";
    String ShopName,ShopIDD,CustomerName;

    public OrderDBHelper(Context context, String ShopID,String customername) {
        super(context, DATABASE_NAME , null, DATABASE_VERSION);
        ShopIDD = ShopID;
        CustomerName = customername;

        ShopDBHelper s = new ShopDBHelper(context);
        Cursor res =  s.getShop(Integer.parseInt(ShopID));

        res.moveToFirst();

        ShopName = res.getString(1);

        res.close();

        TABLE_NAME = "Order"+ShopIDD+CustomerName;
        Log.d(TAG, "OrderDBHepler: Order_TABLE_NAME = " + TABLE_NAME);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        TABLE_NAME = "Order"+ShopIDD+CustomerName;
        Log.d(TAG, "onCreate: TABLE_NAME = " + TABLE_NAME);
        db.execSQL(
                "CREATE TABLE " + TABLE_NAME +
                        "(" + COLUMN_ID + " INTEGER PRIMARY KEY, " +
                        COLUMN_SHOPNAME + " TEXT, " +
                        COLUMN_CUSTOMERNAME + " TEXT, " +
                        COLUMN_PRODUCTNAME + " TEXT, " +
                        COLUMN_QUANTITY + " TEXT, " +
                        COLUMN_MONEY + " TEXT, " +
                        COLUMN_TOTALMONEY + " TEXT)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        TABLE_NAME = "Order"+ShopIDD+CustomerName;
        Log.d(TAG, "onUpgrade: TABLE_NAME = " + TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public void DeleteTable() {
        SQLiteDatabase db = this.getWritableDatabase();
        TABLE_NAME = "Order"+ShopIDD+CustomerName;
        Log.d(TAG, "DeleteTable: Order_TABLE_NAME = " + TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);

    }

    public boolean insertOrder(String customname, String productname, String quantity, String money, String totalmoney) {

        Cursor c = getAllOrders();

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(COLUMN_SHOPNAME, ShopName);
        contentValues.put(COLUMN_CUSTOMERNAME, customname);
        contentValues.put(COLUMN_PRODUCTNAME, productname);
        contentValues.put(COLUMN_QUANTITY, quantity);
        contentValues.put(COLUMN_MONEY, money);
        contentValues.put(COLUMN_TOTALMONEY, totalmoney);

        int InsertOrUpdate = 0;
        int beforeQuantity = 0;
        if(c != null) {
            if (c.getCount() > 0) {
                c.moveToFirst();

                for (int i = 1; i <= c.getCount(); i++) {
                    if (c.getString(3).equals(productname)) {
                        InsertOrUpdate = c.getInt(0);
                        beforeQuantity = Integer.parseInt(c.getString(4));
                        i = c.getCount() + 1;
                    }
                    c.moveToNext();
                }
            }
        }

        if(InsertOrUpdate == 0) {
            Log.d(TAG, "insertOrder: insert = " + TABLE_NAME);
            db.insert(TABLE_NAME, null, contentValues);
        } else {
            String newquantity = String.valueOf((beforeQuantity+Integer.parseInt(quantity)));
            String newtotalmoney = String.valueOf(Integer.parseInt(newquantity) * Integer.parseInt(money));
            updateOrder(InsertOrUpdate,customname,productname,newquantity,money,newtotalmoney);
            Log.d(TAG, "UpdateOrder: Update = " + newtotalmoney );
        }


        return true;
    }

    public int numberOfRows() {
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, TABLE_NAME);
        return numRows;
    }

    public boolean updateOrder(Integer id,String customname, String productname, String quantity, String money, String totalmoney) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(COLUMN_SHOPNAME, ShopName);
        contentValues.put(COLUMN_CUSTOMERNAME, customname);
        contentValues.put(COLUMN_PRODUCTNAME, productname);
        contentValues.put(COLUMN_QUANTITY, quantity);
        contentValues.put(COLUMN_MONEY, money);
        contentValues.put(COLUMN_TOTALMONEY, totalmoney);

        db.update(TABLE_NAME, contentValues, COLUMN_ID + " = ? ", new String[] { Integer.toString(id) } );
        return true;
    }

    public Integer deleteOrder(Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME,
                COLUMN_ID + " = ? ",
                new String[] { Integer.toString(id) });
    }


    public Cursor getOrder(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " +
                COLUMN_ID + "=?", new String[]{Integer.toString(id)});
        return res;
    }

    public Cursor getAllOrders() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = null;
        try{
             res =  db.rawQuery( "SELECT * FROM " + TABLE_NAME, null );
        }catch(android.database.sqlite.SQLiteException e) {
            onCreate(db);
        }

        return res;
    }
}
