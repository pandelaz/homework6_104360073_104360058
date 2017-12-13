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
public class ShopDBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "HighCPShop.db";
    private static final int DATABASE_VERSION = 1;

    public static final String Shop_TABLE_NAME = "Shop";
    public static final String Shop_COLUMN_ID = "_id";
    public static final String Shop_COLUMN_NAME = "name";
    public static final String Shop_COLUMN_TEL = "tel";
    public static final String Shop_COLUMN_ADDRESS = "address";
    public static final String Shop_COLUMN_INFO = "info";
    public static final String Shop_COLUMN_LAT = "lat";
    public static final String Shop_COLUMN_LNG = "lng";

    Context ctt;

    public ShopDBHelper(Context context) {
        super(context, DATABASE_NAME , null, DATABASE_VERSION);
        Log.d(TAG, "ShopDBHepler: Shop_TABLE_NAME = " + Shop_TABLE_NAME);
        ctt = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //Log.d(TAG, "ShopDBHepler: Shop_TABLE_NAME = " + Shop_TABLE_NAME);
        db.execSQL(
                "CREATE TABLE " + Shop_TABLE_NAME +
                        "(" + Shop_COLUMN_ID + " INTEGER PRIMARY KEY, " +
                        Shop_COLUMN_NAME + " TEXT, " +
                        Shop_COLUMN_TEL + " TEXT, " +
                        Shop_COLUMN_ADDRESS + " TEXT, " +
                        Shop_COLUMN_INFO + " TEXT, " +
                        Shop_COLUMN_LAT + " TEXT, " +
                        Shop_COLUMN_LNG + " TEXT)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //Log.d(TAG, "ShopDBHepler: Shop_TABLE_NAME = " + Shop_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + Shop_TABLE_NAME);
        onCreate(db);
    }

    public boolean insertShop(String name, String tel,String address, String info, String lat, String lng) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(Shop_COLUMN_NAME, name);
        contentValues.put(Shop_COLUMN_TEL, tel);
        contentValues.put(Shop_COLUMN_ADDRESS, address);
        contentValues.put(Shop_COLUMN_INFO, info);
        contentValues.put(Shop_COLUMN_LAT, lat);
        contentValues.put(Shop_COLUMN_LNG, lng);

        db.insert(Shop_TABLE_NAME, null, contentValues);
        return true;
    }

    public int numberOfRows() {
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, Shop_TABLE_NAME);
        return numRows;
    }

    public boolean updateShop(Integer id, String name, String tel,String address, String info, String lat, String lng) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(Shop_COLUMN_NAME, name);
        contentValues.put(Shop_COLUMN_TEL, tel);
        contentValues.put(Shop_COLUMN_ADDRESS, address);
        contentValues.put(Shop_COLUMN_INFO, info);
        contentValues.put(Shop_COLUMN_LAT, lat);
        contentValues.put(Shop_COLUMN_LNG, lng);

        db.update(Shop_TABLE_NAME, contentValues, Shop_COLUMN_ID + " = ? ", new String[] { Integer.toString(id) } );
        return true;
    }

    public Integer deleteShop(Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();
        Log.d(TAG, "ShopDBHepler: deleteshop = " + String.valueOf(id));
/*
        ProductDBHelper pdbhlpr = new ProductDBHelper(ctt,String.valueOf(id));
        pdbhlpr.DeleteTable();

        CustomerDBHelper cdbhlpr = new CustomerDBHelper(ctt,String.valueOf(id));

        Cursor c = cdbhlpr.getAllCustomers();

        if(c.getCount()>0) {
            c.moveToFirst();

            for (int i = 0; i < c.getCount(); i++) {
                OrderDBHelper odbhlpr = new OrderDBHelper(ctt, String.valueOf(id), c.getString(1));
                odbhlpr.DeleteTable();
                c.moveToNext();
            }

        }

        cdbhlpr.DeleteTable();
*/
        return db.delete(Shop_TABLE_NAME,
                Shop_COLUMN_ID + " = ? ",
                new String[] { Integer.toString(id) });
    }

    public Cursor getShop(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery("SELECT * FROM " + Shop_TABLE_NAME + " WHERE " +
                Shop_COLUMN_ID + "=?", new String[]{Integer.toString(id)});
        return res;
    }

    public Cursor getAllShops() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "SELECT * FROM " + Shop_TABLE_NAME, null );
        return res;
    }
}
