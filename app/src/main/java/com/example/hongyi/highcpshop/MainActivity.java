package com.example.hongyi.highcpshop;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class MainActivity extends AppCompatActivity {


    public final static String KEY_EXTRA_CONTACT_ID = "KEY_EXTRA_CONTACT_ID";

    private ListView listView;
    private ImageView img;
    ShopDBHelper dbHelper;
    private int status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR); getSupportActionBar().hide();
        setContentView(R.layout.activity_main);

        status = 0;
        img = (ImageView) findViewById(R.id.imageView2);
        Button button = (Button) findViewById(R.id.addNew);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, Shop_CreateOrEditActivity.class);
                intent.putExtra(KEY_EXTRA_CONTACT_ID, 0);
                startActivity(intent);
            }
        });

        Button deletebtn = (Button) findViewById(R.id.deleteOld);
        deletebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(status == 0) {
                    status = 1;
                    //img.setImageResource(R.drawable.edit);
                    for(int i=0;i<listView.getCount();i++) {
                        View tview = getViewByPosition(i, listView);//=  new View(listView.getContext());
                        ImageView imgv = tview.findViewById(R.id.imageView7);//(ImageView) view.findViewById(R.id.imageView);
                        imgv.setVisibility(View.VISIBLE);
                    }

                } else {
                    status = 0;
                    //img.setImageResource(R.drawable.title06);
                    for(int i=0;i<listView.getCount();i++) {
                        View tview = getViewByPosition(i, listView);//=  new View(listView.getContext());
                        ImageView imgv = tview.findViewById(R.id.imageView7);//(ImageView) view.findViewById(R.id.imageView);
                        imgv.setVisibility(View.INVISIBLE);
                    }
                }
            }
        });

        dbHelper = new ShopDBHelper(this);

        final Cursor cursor = dbHelper.getAllShops();

        String [] columns = new String[] {
                ShopDBHelper.Shop_COLUMN_ID,
                ShopDBHelper.Shop_COLUMN_NAME,
                ShopDBHelper.Shop_COLUMN_TEL,
                ShopDBHelper.Shop_COLUMN_ADDRESS,
                ShopDBHelper.Shop_COLUMN_INFO,
                ShopDBHelper.Shop_COLUMN_LAT,
                ShopDBHelper.Shop_COLUMN_LNG
        };
        int [] widgets = new int[] {
                R.id.shopID,
                R.id.ShopName,
                R.id.tele,
                R.id.address,
                R.id.Info,
                R.id.shopLAT,
                R.id.shopLNG
        };

        SimpleCursorAdapter cursorAdapter = new SimpleCursorAdapter(this, R.layout.shop_info,
                cursor, columns, widgets, 0);

        listView = (ListView)findViewById(R.id.listview1);
        listView.setAdapter(cursorAdapter);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> listView, View view,
                                    int position, long id) {
                Cursor itemCursor = (Cursor) MainActivity.this.listView.getItemAtPosition(position);

                final int ShopID = itemCursor.getInt(itemCursor.getColumnIndex(ShopDBHelper.Shop_COLUMN_ID));
                final String ShopName = itemCursor.getString(itemCursor.getColumnIndex(ShopDBHelper.Shop_COLUMN_NAME));
                final String lat = itemCursor.getString(itemCursor.getColumnIndex(ShopDBHelper.Shop_COLUMN_LAT));
                final String lng = itemCursor.getString(itemCursor.getColumnIndex(ShopDBHelper.Shop_COLUMN_LNG));

                final String[] list_item = {"GoogleMap上顯示位置","商品目錄管理","下單管理","歷史銷售紀錄"};
                AlertDialog.Builder dialog_list = new AlertDialog.Builder(MainActivity.this);
                dialog_list.setTitle(ShopName);
                dialog_list.setItems(list_item, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        switch (list_item[i]) {

                            case "GoogleMap上顯示位置" :
                                Intent intent = new Intent(MainActivity.this,MapsActivity.class);
                                intent.putExtra(KEY_EXTRA_CONTACT_ID, lat+","+lng+","+ ShopName);
                                startActivity(intent);
                                break;
                            case "商品目錄管理" :
                                Intent intent2 = new Intent(MainActivity.this,Main2Activity.class);
                                intent2.putExtra(KEY_EXTRA_CONTACT_ID, ShopID);
                                startActivity(intent2);
                                break;
                            case "下單管理" :
                                Intent intent3 = new Intent(MainActivity.this,Main3Activity.class);
                                intent3.putExtra(KEY_EXTRA_CONTACT_ID, ShopID);
                                startActivity(intent3);
                                break;
                            case "歷史銷售紀錄" :
                                Intent intent4 = new Intent(MainActivity.this,Main4Activity.class);
                                intent4.putExtra(KEY_EXTRA_CONTACT_ID, ShopID);
                                startActivity(intent4);

                                break;
                        }

                        //Toast.makeText(MainActivity.this,"你選的是" + list_item[i],Toast.LENGTH_SHORT).show();
                    }
                });
                if(status==0)
                    dialog_list.show();
                else {
                    //delete or edit function

                    Intent intent = new Intent(getApplicationContext(), Shop_CreateOrEditActivity.class);
                    intent.putExtra(KEY_EXTRA_CONTACT_ID, ShopID);
                    startActivity(intent);
                    status = 0;

                    for(int i=0;i<listView.getCount();i++) {
                        View tview = getViewByPosition(i, MainActivity.this.listView);//=  new View(listView.getContext());

                        ImageView imgv = tview.findViewById(R.id.imageView7);//(ImageView) view.findViewById(R.id.imageView);
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

