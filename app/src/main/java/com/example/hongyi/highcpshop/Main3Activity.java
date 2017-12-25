package com.example.hongyi.highcpshop;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class Main3Activity extends AppCompatActivity {

    public final static String KEY_EXTRA_CONTACT_ID = "KEY_EXTRA_CONTACT_ID";

    private ListView listView;
    CustomerDBHelper dbHelper;
    private int status;
    int ShopID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR); getSupportActionBar().hide();
        setContentView(R.layout.activity_main3);

        ShopID = getIntent().getIntExtra(MainActivity.KEY_EXTRA_CONTACT_ID, 0);

        ShopDBHelper s = new ShopDBHelper(this);

        Cursor c = s.getShop(ShopID);
        c.moveToFirst();
        String shopname = c.getString(1);

        TextView t = (TextView) findViewById(R.id.shopName);

        t.setText(shopname);


        status = 0;


        dbHelper = new CustomerDBHelper(this,String.valueOf(ShopID));

        final Cursor cursor = dbHelper.getAllCustomers();

        String [] columns = new String[] {
                CustomerDBHelper.COLUMN_ID,
                CustomerDBHelper.COLUMN_CUSTOMERNAME
        };
        int [] widgets = new int[] {
                R.id.OrderID,
                R.id.CustomName
        };

        CustomSimpleCursorAdapter cursorAdapter = new CustomSimpleCursorAdapter(this, R.layout.order_customer,
                cursor, columns, widgets, 0,ShopID);

        listView = (ListView)findViewById(R.id.listview1);
        listView.setAdapter(cursorAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> listView, View view,
                                    int position, long id) {
                Cursor itemCursor = (Cursor) Main3Activity.this.listView.getItemAtPosition(position);

                final int CustomerID = itemCursor.getInt(itemCursor.getColumnIndex(CustomerDBHelper.COLUMN_ID));

                final String CustomerName = itemCursor.getString(itemCursor.getColumnIndex(CustomerDBHelper.COLUMN_CUSTOMERNAME));

                if(status==0) {
                    //comfirm
                    //Toast.makeText(Main3Activity.this,"comfirm",Toast.LENGTH_SHORT).show();
                } else {
                    //delete or edit function
                    status = 0;

                    for(int i=0;i<listView.getCount();i++) {
                        View tview = getViewByPosition(i, Main3Activity.this.listView);//=  new View(listView.getContext());
                        ImageView imgv = tview.findViewById(R.id.imageView12);//(ImageView) view.findViewById(R.id.imageView);
                        imgv.setVisibility(View.INVISIBLE);

                    }

                }
            }

        });

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
