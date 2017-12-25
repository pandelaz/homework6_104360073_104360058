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
public class CustomerDBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "HighCP.db";
    private static final int DATABASE_VERSION = 1;

    public String TABLE_NAME = "Customer";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_CUSTOMERNAME = "customername";
    String ShopName,ShopIDD;//,CustomerName;

    public CustomerDBHelper(Context context, String ShopID) {
        super(context, DATABASE_NAME , null, DATABASE_VERSION);
        ShopIDD = ShopID;
        //CustomerName = Customer;

        ShopDBHelper s = new ShopDBHelper(context);
        Cursor res =  s.getShop(Integer.parseInt(ShopID));
        res.moveToFirst();
        ShopName = res.getString(1);

        TABLE_NAME = "Customer"+ShopIDD;
        Log.d(TAG, "CustomerDBHepler: TABLE_NAME = " + TABLE_NAME);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        TABLE_NAME = "Customer"+ShopIDD;
        Log.d(TAG, "onCreate: TABLE_NAME = " + TABLE_NAME);
        db.execSQL(
                "CREATE TABLE " + TABLE_NAME +
                        "(" + COLUMN_ID + " INTEGER PRIMARY KEY, " +
                        COLUMN_CUSTOMERNAME + " TEXT)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        TABLE_NAME = "Customer"+ShopIDD;
        Log.d(TAG, "onUpgrade: TABLE_NAME = " + TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public void DeleteTable() {
        SQLiteDatabase db = this.getWritableDatabase();
        TABLE_NAME = "Customer"+ShopIDD;
        Log.d(TAG, "DeleteTable: TABLE_NAME = " + TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);

    }

    public boolean insertCustomer(String customname) {

        Cursor c = getAllCustomers();
        int DontInsert = 0;

        if(c != null) {
            if (c.getCount() > 0) {

                c.moveToFirst();

                for (int i = 0; i < c.getCount(); i++) {
                    if (customname.equals(c.getString(1))) {
                        DontInsert = 1;
                        i = c.getCount() + 1;
                    }
                    c.moveToNext();
                }
            }
        }

        if(DontInsert == 0) {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues contentValues = new ContentValues();

            contentValues.put(COLUMN_CUSTOMERNAME, customname);

            Log.d(TAG, "insertCustomer: insert = " + TABLE_NAME);
            db.insert(TABLE_NAME, null, contentValues);
        }

        return true;
    }

    public int numberOfRows() {
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, TABLE_NAME);
        return numRows;
    }

    public boolean updateCustomer(Integer id,String customname) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(COLUMN_CUSTOMERNAME, customname);

        db.update(TABLE_NAME, contentValues, COLUMN_ID + " = ? ", new String[] { Integer.toString(id) } );
        return true;
    }

    public Integer deleteCustomer(Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();

        Log.d("deleteCustomer",String.valueOf(id));

        return db.delete(TABLE_NAME,
                COLUMN_ID + " = ? ",
                new String[] { Integer.toString(id) });
    }

    public Cursor getCustomer(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " +
                COLUMN_ID + "=?", new String[]{Integer.toString(id)});
        return res;
    }

    public Cursor getAllCustomers() {
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
