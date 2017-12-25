package com.example.hongyi.highcpshop;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

/**
 * Created by hongyi on 2017/12/8.
 */

public class Product_CreateOrEditActivity extends AppCompatActivity implements View.OnClickListener {


    private ProductDBHelper dbHelper;
    EditText nameEditText;
    EditText moneyEditText;
    EditText infoEditText;

    Button saveButton;
    LinearLayout buttonLayout;
    Button editButton, deleteButton;

    String ShopID;

    int ProductID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR); getSupportActionBar().hide();
        String tmp[] = getIntent().getStringExtra(MainActivity.KEY_EXTRA_CONTACT_ID).split(",");
        ProductID = Integer.parseInt(tmp[0]);
        ShopID = tmp[1];

        //Toast.makeText(Product_CreateOrEditActivity.this,"ShopID : " + String.valueOf(ShopID),Toast.LENGTH_SHORT).show();


        setContentView(R.layout.product_activity_edit);
        nameEditText = (EditText) findViewById(R.id.editTextName);
        moneyEditText = (EditText) findViewById(R.id.editTextMoney);
        infoEditText = (EditText) findViewById(R.id.editTextInfo);

        saveButton = (Button) findViewById(R.id.saveButton);
        saveButton.setOnClickListener(this);
        buttonLayout = (LinearLayout) findViewById(R.id.buttonLayout);
        //editButton = (Button) findViewById(R.id.editButton);
        //editButton.setOnClickListener(this);
        deleteButton = (Button) findViewById(R.id.deleteButton);
        deleteButton.setOnClickListener(this);

        dbHelper = new ProductDBHelper(this,ShopID);

        if (ProductID > 0) {
            //saveButton.setVisibility(View.GONE);
            buttonLayout.setVisibility(View.VISIBLE);

            Cursor rs = dbHelper.getProduct(ProductID);
            rs.moveToFirst();

            String ProductName = rs.getString(rs.getColumnIndex(ProductDBHelper.Product_COLUMN_NAME));

            String ProductMoney = rs.getString(rs.getColumnIndex(ProductDBHelper.Product_COLUMN_MONEY));

            String ProductInfo = rs.getString(rs.getColumnIndex(ProductDBHelper.Product_COLUMN_INFO));

            if (!rs.isClosed()) {
                rs.close();
            }


            nameEditText.setText(ProductName);
            nameEditText.setEnabled(true);
            nameEditText.setFocusableInTouchMode(true);
            nameEditText.setClickable(true);
            //nameEditText.setFocusable(false);
            //nameEditText.setClickable(false);

            moneyEditText.setText(ProductMoney);
            moneyEditText.setEnabled(true);
            moneyEditText.setFocusableInTouchMode(true);
            moneyEditText.setClickable(true);
            //moneyEditText.setFocusable(false);
            //moneyEditText.setClickable(false);

            infoEditText.setText(ProductInfo);
            infoEditText.setEnabled(true);
            infoEditText.setFocusableInTouchMode(true);
            infoEditText.setClickable(true);
            //infoEditText.setFocusable(false);
            //infoEditText.setClickable(false);

        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.saveButton:
                persistProduct();
                return;
                /*
            case R.id.editButton:
                saveButton.setVisibility(View.VISIBLE);
                buttonLayout.setVisibility(View.GONE);

                nameEditText.setEnabled(true);
                nameEditText.setFocusableInTouchMode(true);
                nameEditText.setClickable(true);

                moneyEditText.setEnabled(true);
                moneyEditText.setFocusableInTouchMode(true);
                moneyEditText.setClickable(true);

                infoEditText.setEnabled(true);
                infoEditText.setFocusableInTouchMode(true);
                infoEditText.setClickable(true);

                return;
                */
            case R.id.deleteButton:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage(R.string.deletePerson)
                        .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dbHelper.deleteProduct(ProductID);
                                Toast.makeText(getApplicationContext(), "Deleted Successfully", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getApplicationContext(), Main2Activity.class);
                                intent.putExtra(Main2Activity.KEY_EXTRA_CONTACT_ID, Integer.parseInt(ShopID));
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
                return;
        }
    }

    public void persistProduct() {
        if(ProductID > 0) {
            if(dbHelper.updateProduct(
                    ProductID,
                    nameEditText.getText().toString(),
                    moneyEditText.getText().toString(),
                    infoEditText.getText().toString()
                    ))
            {
                Toast.makeText(getApplicationContext(), "Shop Update Successful", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), Main2Activity.class);
                intent.putExtra(Main2Activity.KEY_EXTRA_CONTACT_ID, Integer.parseInt(ShopID));
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
            else {
                Toast.makeText(getApplicationContext(), "Shop Update Failed", Toast.LENGTH_SHORT).show();
            }
        }
        else {
            if(dbHelper.insertProduct(
                    nameEditText.getText().toString(),
                    moneyEditText.getText().toString(),
                    infoEditText.getText().toString()
                    ))
            {
                Toast.makeText(getApplicationContext(), "Shop Inserted", Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(getApplicationContext(), "Could not Insert Shop", Toast.LENGTH_SHORT).show();
            }
            Intent intent = new Intent(getApplicationContext(), Main2Activity.class);
            intent.putExtra(Main2Activity.KEY_EXTRA_CONTACT_ID, Integer.parseInt(ShopID));
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
    }

}

