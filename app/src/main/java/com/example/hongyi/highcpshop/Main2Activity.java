package com.example.hongyi.highcpshop;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageButton;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import static android.content.ContentValues.TAG;

public class Main2Activity extends AppCompatActivity {

    public final static String KEY_EXTRA_CONTACT_ID = "KEY_EXTRA_CONTACT_ID";

    private GridView gridView;
    ProductDBHelper dbHelper;
    private int status;

    private TextView shopname,shoptele,shopaddress,shopinfo;

    int ShopID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR); getSupportActionBar().hide();
        setContentView(R.layout.activity_main2);
        status = 0;

        shopname = (TextView) findViewById(R.id.ShopName);
        shoptele = (TextView) findViewById(R.id.tele);
        shopaddress = (TextView) findViewById(R.id.address);
        shopinfo = (TextView) findViewById(R.id.Info);


        ShopID = getIntent().getIntExtra(MainActivity.KEY_EXTRA_CONTACT_ID, 0);

        ShopDBHelper shopdbHelper = new ShopDBHelper(this);
        Cursor ShopCursor = shopdbHelper.getShop(ShopID);
        ShopCursor.moveToFirst();
        shopname.setText(ShopCursor.getString(ShopCursor.getColumnIndex(ShopDBHelper.Shop_COLUMN_NAME)));
        shoptele.setText(ShopCursor.getString(ShopCursor.getColumnIndex(ShopDBHelper.Shop_COLUMN_TEL)));
        shopaddress.setText(ShopCursor.getString(ShopCursor.getColumnIndex(ShopDBHelper.Shop_COLUMN_ADDRESS)));
        shopinfo.setText(ShopCursor.getString(ShopCursor.getColumnIndex(ShopDBHelper.Shop_COLUMN_INFO)));

        //Toast.makeText(Main2Activity.this,"ShopID : " + String.valueOf(ShopID),Toast.LENGTH_SHORT).show();

        Button button = (Button) findViewById(R.id.addNew);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Main2Activity.this, Product_CreateOrEditActivity.class);
                intent.putExtra(KEY_EXTRA_CONTACT_ID, 0 + "," + String.valueOf(ShopID));
                startActivity(intent);
            }
        });

        Button deletebtn = (Button) findViewById(R.id.deleteOld);
        deletebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(status == 0) {
                    status = 1;
                    for(int i=0;i<gridView.getCount();i++) {
                        View tview = getViewByPosition(i, gridView);//=  new View(listView.getContext());
                        ImageView imgv = tview.findViewById(R.id.imageView7);//(ImageView) view.findViewById(R.id.imageView);
                        imgv.setVisibility(View.VISIBLE);
                    }
                } else {
                    status = 0;
                    for(int i=0;i<gridView.getCount();i++) {
                        View tview = getViewByPosition(i, gridView);//=  new View(listView.getContext());
                        ImageView imgv = tview.findViewById(R.id.imageView7);//(ImageView) view.findViewById(R.id.imageView);
                        imgv.setVisibility(View.INVISIBLE);
                    }
                }
            }
        });

        dbHelper = new ProductDBHelper(this,String.valueOf(ShopID));

        final Cursor cursor = dbHelper.getAllProducts();

        String [] columns = new String[] {
                ProductDBHelper.Product_COLUMN_ID,
                ProductDBHelper.Product_COLUMN_NAME,
                ProductDBHelper.Product_COLUMN_MONEY,
                ProductDBHelper.Product_COLUMN_INFO
        };
        int [] widgets = new int[] {
                R.id.ProductId,
                R.id.ProductName,
                R.id.ProductMoney,
                R.id.ProductInfo
        };

        SimpleCursorAdapter cursorAdapter = new SimpleCursorAdapter(this, R.layout.product_info,
                cursor, columns, widgets, 0);

        gridView = (GridView)findViewById(R.id.listview1);
        gridView.setAdapter(cursorAdapter);


        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> listView, View view,
                                    int position, long id) {
                Cursor itemCursor = (Cursor) Main2Activity.this.gridView.getItemAtPosition(position);
                final int ProductID = itemCursor.getInt(itemCursor.getColumnIndex(ProductDBHelper.Product_COLUMN_ID));
                if(status == 0) {
                    final String ProductName = itemCursor.getString(itemCursor.getColumnIndex(ProductDBHelper.Product_COLUMN_NAME));
                    final String Price = itemCursor.getString(itemCursor.getColumnIndex(ProductDBHelper.Product_COLUMN_MONEY));
                    showDialog(Main2Activity.this,ProductName,Price);

                } else {
                    //delete or edit function
                    Intent intent = new Intent(getApplicationContext(), Product_CreateOrEditActivity.class);
                    intent.putExtra(KEY_EXTRA_CONTACT_ID, String.valueOf(ProductID) + "," + String.valueOf(ShopID));
                    startActivity(intent);
                    status = 0;

                    for(int i=0;i<listView.getCount();i++) {
                        View tview = getViewByPosition(i, Main2Activity.this.gridView);//=  new View(listView.getContext());
                        ImageView imgv = tview.findViewById(R.id.imageView7);//(ImageView) view.findViewById(R.id.imageView);
                        imgv.setVisibility(View.INVISIBLE);
                    }
                }
            }
        });

    }



    public void showDialog(Activity activity, final String ProductName, String Price){
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.product_buy_dialog);

        TextView text = (TextView) dialog.findViewById(R.id.ProductName);
        text.setText(ProductName);
        TextView text2 = (TextView) dialog.findViewById(R.id.ProductMoney);
        text2.setText(Price);

        Button dialogOKButton = (Button) dialog.findViewById(R.id.OKBtn);
        dialogOKButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                TextView price = (TextView) dialog.findViewById(R.id.ProductMoney);
                TextView Totalprice = (TextView) dialog.findViewById(R.id.TotalPrice);
                EditText edt2 = (EditText) dialog.findViewById(R.id.editText2); //customername
                EditText edt = (EditText) dialog.findViewById(R.id.editText);

                OrderDBHelper OrderdbHelper = new OrderDBHelper(Main2Activity.this,String.valueOf(ShopID),edt2.getText().toString());
                OrderdbHelper.insertOrder(edt2.getText().toString(), ProductName,edt.getText().toString(),price.getText().toString(),Totalprice.getText().toString());

                CustomerDBHelper CustomerdbHelper = new CustomerDBHelper(Main2Activity.this,String.valueOf(ShopID));
                CustomerdbHelper.insertCustomer(edt2.getText().toString());

                Toast.makeText(Main2Activity.this,"購買成功",Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });

        Button dialogCanelButton = (Button) dialog.findViewById(R.id.Cancelbtn);
        dialogCanelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Main2Activity.this,"取消購買",Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });

        AppCompatImageButton plusbtn = (AppCompatImageButton) dialog.findViewById(R.id.imageButton);
        plusbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               EditText edt = (EditText) dialog.findViewById(R.id.editText);
               int num = Integer.parseInt(edt.getText().toString());
               if( num != 99) {
                   num++;
                   edt.setText(String.valueOf(num));

                   TextView price = (TextView) dialog.findViewById(R.id.ProductMoney);
                   int num2 = Integer.parseInt(price.getText().toString());
                   int num3 = num2 * num;

                   TextView Totalprice = (TextView) dialog.findViewById(R.id.TotalPrice);

                   Totalprice.setText(String.valueOf(num3));

               }
            }
        });

        AppCompatImageButton minusbtn = (AppCompatImageButton) dialog.findViewById(R.id.imageButton2);
        minusbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText edt = (EditText) dialog.findViewById(R.id.editText);
                int num = Integer.parseInt(edt.getText().toString());
                if(num != 0) {
                    num--;
                    edt.setText(String.valueOf(num));

                    TextView price = (TextView) dialog.findViewById(R.id.ProductMoney);
                    int num2 = Integer.parseInt(price.getText().toString());
                    int num3 = num2 * num;

                    TextView Totalprice = (TextView) dialog.findViewById(R.id.TotalPrice);

                    Totalprice.setText(String.valueOf(num3));
                }
            }
        });

        EditText edt = (EditText) dialog.findViewById(R.id.editText);

        edt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                EditText edt = (EditText) dialog.findViewById(R.id.editText);


                    try {
                        int num = Integer.parseInt(edt.getText().toString());


                        TextView price = (TextView) dialog.findViewById(R.id.ProductMoney);
                        int num2 = Integer.parseInt(price.getText().toString());
                        int num3 = num2 * num;

                        TextView Totalprice = (TextView) dialog.findViewById(R.id.TotalPrice);

                        Totalprice.setText(String.valueOf(num3));
                    }
                    catch (NumberFormatException e) {
                        Log.d(TAG, "afterTextChanged: " + e);
                    }

            }
        });

        dialog.show();

    }

    public View getViewByPosition(int pos, GridView listView) {
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
