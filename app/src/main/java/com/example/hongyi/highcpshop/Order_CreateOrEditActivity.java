package com.example.hongyi.highcpshop;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by hongyi on 2017/12/8.
 */

public class Order_CreateOrEditActivity extends AppCompatActivity implements View.OnClickListener {

    private ListView listView;
    private OrderDBHelper dbHelper;

    Button saveButton;
    LinearLayout buttonLayout;
    Button deleteButton;
    Button okButton;
    TextView totaltotal;
    String ShopID,CustomerName;

    int CustomerID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR); getSupportActionBar().hide();
        String tmp[] = getIntent().getStringExtra(MainActivity.KEY_EXTRA_CONTACT_ID).split(",");

        CustomerID = Integer.parseInt(tmp[0]);
        CustomerName = tmp[1];
        ShopID = tmp[2];

        //Toast.makeText(Order_CreateOrEditActivity.this,"ID : " + String.valueOf(ShopID) + "," +
        //        CustomerName + "," + String.valueOf(CustomerID),Toast.LENGTH_SHORT).show();

        setContentView(R.layout.order_activity_edit);

        totaltotal = (TextView) findViewById(R.id.totoaltotoal);

        TextView tvw = (TextView) findViewById(R.id.CustomerName);
        tvw.setText(CustomerName);
        saveButton = (Button) findViewById(R.id.saveButton);
        saveButton.setOnClickListener(this);
        buttonLayout = (LinearLayout) findViewById(R.id.buttonLayout);
        deleteButton = (Button) findViewById(R.id.deleteButton);
        deleteButton.setOnClickListener(this);
        okButton = (Button) findViewById(R.id.okButton);
        okButton.setOnClickListener(this);

        //Log.d("test : ","-1");
        dbHelper = new OrderDBHelper(this,ShopID,CustomerName);
        //Log.d("test : ","0");
        final Cursor rs = dbHelper.getAllOrders();
        Cursor rss = rs;
        //Log.d("test : ","1");
        String [] columns = new String[] {
                OrderDBHelper.COLUMN_ID,
                OrderDBHelper.COLUMN_SHOPNAME,
                OrderDBHelper.COLUMN_CUSTOMERNAME,
                OrderDBHelper.COLUMN_PRODUCTNAME,
                OrderDBHelper.COLUMN_QUANTITY,
                OrderDBHelper.COLUMN_MONEY,
                OrderDBHelper.COLUMN_TOTALMONEY
        };
        int [] widgets = new int[] {
                R.id.OrderID,
                R.id.ShopName,
                R.id.CustomName,
                R.id.ProductName,
                R.id.OrderQuantity,
                R.id.OrderPrice,
                R.id.OrderTotalPrice
        };
        //Log.d("test : ","2");
        OrderInfoSimpleCursorAdapter cursorAdapter = new OrderInfoSimpleCursorAdapter(this, R.layout.order_info,
                rs, columns, widgets, 0,Integer.parseInt(ShopID),totaltotal);
        //Log.d("test : ","3");
        listView = (ListView)findViewById(R.id.listview);
        listView.setAdapter(cursorAdapter);

        if(rss.getCount()>0) {
            rss.moveToFirst();
            int toprice = 0;
            for(int i=0;i<rss.getCount();i++) {

                int tmpp =Integer.parseInt(rss.getString(6));

                toprice = toprice + tmpp;

                rss.moveToNext();
            }

            totaltotal.setText(String.valueOf(toprice));


        }



    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.saveButton:

                for(int i=0;i<listView.getCount();i++) {
                    View vw = getViewByPosition(i,listView);

                    TextView t1 = (TextView) vw.findViewById(R.id.OrderID);
                    int s1 = Integer.parseInt(t1.getText().toString());

                    TextView t2 = (TextView) vw.findViewById(R.id.ShopName);
                    String s2 = t2.getText().toString();

                    TextView t3 = (TextView) vw.findViewById(R.id.CustomName);
                    String s3 = t3.getText().toString();

                    TextView t4 = (TextView) vw.findViewById(R.id.ProductName);
                    String s4 = t4.getText().toString();

                    TextView t5 = (TextView) vw.findViewById(R.id.OrderQuantity);
                    String s5 = t5.getText().toString();

                    TextView t6 = (TextView) vw.findViewById(R.id.OrderPrice);
                    String s6 = t6.getText().toString();

                    String s7 = String.valueOf(Integer.parseInt(s5) * Integer.parseInt(s6));

                    dbHelper.updateOrder(s1,s3,s4,s5,s6,s7);

                }

                Toast.makeText(Order_CreateOrEditActivity.this,"更改完成",Toast.LENGTH_SHORT).show();

                return;
            case R.id.deleteButton:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage(R.string.deletePerson)
                        .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dbHelper.DeleteTable();

                                CustomerDBHelper cpr = new CustomerDBHelper(getApplicationContext(),ShopID);
                                cpr.deleteCustomer(CustomerID);

                                Toast.makeText(getApplicationContext(), "Deleted Successfully", Toast.LENGTH_SHORT).show();

                                Intent intent = new Intent(getApplicationContext(), Main3Activity.class);
                                intent.putExtra(Main3Activity.KEY_EXTRA_CONTACT_ID, Integer.parseInt(ShopID));
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                            }
                        })
                        .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // User cancelled the dialog
                            }
                        });
                AlertDialog d = builder.create();
                d.setTitle("Delete Shop?");
                d.show();
                break;

            case R.id.okButton:

                HistoryCustomerDBHelper hCustomerDBHelper = new HistoryCustomerDBHelper(getApplicationContext(),ShopID);
                CustomerDBHelper cpr = new CustomerDBHelper(getApplicationContext(),ShopID);
                hCustomerDBHelper.insertCustomer(CustomerName);

                OrderDBHelper odbhlpr = new OrderDBHelper(getApplicationContext(), ShopID, CustomerName );
                HistoryOrderDBHelper hodbhlpr = new HistoryOrderDBHelper(getApplicationContext(), ShopID, CustomerName);

                Cursor cc = odbhlpr.getAllOrders();

                if(cc.getCount()>0) {
                    cc.moveToFirst();
                    for(int j=0;j<cc.getCount();j++) {
                        Log.d("test:",cc.getString(1)); //shopname
                        Log.d("test:",cc.getString(2)); //customername
                        Log.d("test:",cc.getString(3)); //productname
                        Log.d("test:",cc.getString(4)); //price
                        Log.d("test:",cc.getString(5)); //quantity
                        Log.d("test:",cc.getString(6)); //totalprice

                        hodbhlpr.insertOrder(cc.getString(2),cc.getString(3),cc.getString(4),cc.getString(5),cc.getString(6));
                        cc.moveToNext();
                    }
                }

                odbhlpr.DeleteTable();

                cpr.deleteCustomer(CustomerID);
                Intent intent = new Intent(getApplicationContext(), Main3Activity.class);
                intent.putExtra(Main3Activity.KEY_EXTRA_CONTACT_ID, Integer.parseInt(ShopID));
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);

                Toast.makeText(Order_CreateOrEditActivity.this,"訂單已完成",Toast.LENGTH_SHORT).show();

                return;
        }
    }

    public View getViewByPosition(int pos, ListView listView) {
        final int firstListItemPosition = listView.getFirstVisiblePosition();
        final int lastListItemPosition = firstListItemPosition + listView.getChildCount() - 1;

        if (pos < firstListItemPosition || pos > lastListItemPosition ) {
            return listView.getAdapter().getView(pos, null, listView);
        } else {
            final int childIndex = pos - firstListItemPosition;
            return listView.getChildAt(childIndex);
        }
    }

}

