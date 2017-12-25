package com.example.hongyi.highcpshop;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.widget.ListView;
import android.widget.TextView;

public class Main4Activity extends AppCompatActivity {

    private ListView listView;
    HistoryCustomerDBHelper dbHelper;
    private int status;
    int ShopID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR); getSupportActionBar().hide();
        setContentView(R.layout.activity_main4);

        ShopID = getIntent().getIntExtra(MainActivity.KEY_EXTRA_CONTACT_ID, 0);

        ShopDBHelper s = new ShopDBHelper(this);

        Cursor c = s.getShop(ShopID);
        c.moveToFirst();
        String shopname = c.getString(1);

        TextView t = (TextView) findViewById(R.id.shopName);

        t.setText(shopname);


        status = 0;


        dbHelper = new HistoryCustomerDBHelper(this,String.valueOf(ShopID));

        final Cursor cursor = dbHelper.getAllCustomers();

        String [] columns = new String[] {
                HistoryCustomerDBHelper.COLUMN_ID,
                HistoryCustomerDBHelper.COLUMN_CUSTOMERNAME
        };
        int [] widgets = new int[] {
                R.id.OrderID,
                R.id.CustomName
        };

        HistoryCustomSimpleCursorAdapter cursorAdapter = new HistoryCustomSimpleCursorAdapter(this, R.layout.historyorder_customer,
                cursor, columns, widgets, 0,ShopID);

        listView = (ListView)findViewById(R.id.listview1);
        listView.setAdapter(cursorAdapter);
    }
}
