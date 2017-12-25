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
public class ProductDBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "HighCP.db";
    private static final int DATABASE_VERSION = 1;

    public String Product_TABLE_NAME = "Product";
    public static final String Product_COLUMN_ID = "_id";
    public static final String Product_COLUMN_NAME = "name";
    public static final String Product_COLUMN_MONEY = "money";
    public static final String Product_COLUMN_INFO = "info";
    String ShopIDD;

    public ProductDBHelper(Context context,String ShopID) {
        super(context, DATABASE_NAME , null, DATABASE_VERSION);
        ShopIDD = ShopID;
        Product_TABLE_NAME = "Product"+ShopID;
        Log.d(TAG, "ProductDBHepler: Product_TABLE_NAME = " + Product_TABLE_NAME);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Product_TABLE_NAME = "Product"+ShopIDD;
        Log.d(TAG, "onCreate: Product_TABLE_NAME = " + Product_TABLE_NAME);
        db.execSQL(
                "CREATE TABLE " + Product_TABLE_NAME +
                        "(" + Product_COLUMN_ID + " INTEGER PRIMARY KEY, " +
                        Product_COLUMN_NAME + " TEXT, " +
                        Product_COLUMN_MONEY + " TEXT, " +
                        Product_COLUMN_INFO + " TEXT)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Product_TABLE_NAME = "Product"+ShopIDD;
        Log.d(TAG, "onUpgrade: Product_TABLE_NAME = " + Product_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + Product_TABLE_NAME);
        onCreate(db);
    }

    public void DeleteTable() {
        SQLiteDatabase db = this.getWritableDatabase();
        Product_TABLE_NAME = "Product"+ShopIDD;
        Log.d(TAG, "DeleteTable: Product_TABLE_NAME = " + Product_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + Product_TABLE_NAME);

    }

    public boolean insertProduct(String name, String money,String info) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(Product_COLUMN_NAME, name);
        contentValues.put(Product_COLUMN_MONEY, money);
        contentValues.put(Product_COLUMN_INFO, info);

        db.insert(Product_TABLE_NAME, null, contentValues);
        return true;
    }

    public int numberOfRows() {
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, Product_TABLE_NAME);
        return numRows;
    }

    public boolean updateProduct(Integer id, String name, String money,String info) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(Product_COLUMN_NAME, name);
        contentValues.put(Product_COLUMN_MONEY, money);
        contentValues.put(Product_COLUMN_INFO, info);

        db.update(Product_TABLE_NAME, contentValues, Product_COLUMN_ID + " = ? ", new String[] { Integer.toString(id) } );
        return true;
    }

    public Integer deleteProduct(Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(Product_TABLE_NAME,
                Product_COLUMN_ID + " = ? ",
                new String[] { Integer.toString(id) });
    }


    public Cursor getProduct(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery("SELECT * FROM " + Product_TABLE_NAME + " WHERE " +
                Product_COLUMN_ID + "=?", new String[]{Integer.toString(id)});
        return res;
    }

    public Cursor getAllProducts() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = null;
        try{
             res =  db.rawQuery( "SELECT * FROM " + Product_TABLE_NAME, null );
        }catch(android.database.sqlite.SQLiteException e) {
            onCreate(db);
        }

        return res;
    }
}
