package com.example.hongyi.highcpshop;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by hongyi on 2017/12/8.
 */

public class Shop_CreateOrEditActivity extends AppCompatActivity implements View.OnClickListener {


    private ShopDBHelper dbHelper ;
    EditText nameEditText;
    EditText telEditText;
    EditText addressEditText;
    EditText infoEditText;

    TextView ShopLATTextView,ShopLNGTextView;

    Button saveButton;
    LinearLayout buttonLayout;
    Button editButton, deleteButton;
    String OldAddress;
    int ShopID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR); getSupportActionBar().hide();

        ShopID = getIntent().getIntExtra(MainActivity.KEY_EXTRA_CONTACT_ID, 0);
        setContentView(R.layout.activity_edit);
        nameEditText = (EditText) findViewById(R.id.editTextName);
        telEditText = (EditText) findViewById(R.id.editTextTele);
        addressEditText = (EditText) findViewById(R.id.editTextAddress);
        infoEditText = (EditText) findViewById(R.id.editTextInfo);

        ShopLATTextView = (TextView) findViewById(R.id.textViewLAT);
        ShopLNGTextView = (TextView) findViewById(R.id.textViewLNG);

        saveButton = (Button) findViewById(R.id.saveButton);
        saveButton.setOnClickListener(this);
        buttonLayout = (LinearLayout) findViewById(R.id.buttonLayout);

        deleteButton = (Button) findViewById(R.id.deleteButton);
        deleteButton.setOnClickListener(this);

        dbHelper = new ShopDBHelper(this);

        if (ShopID > 0) {

            buttonLayout.setVisibility(View.VISIBLE);

            Cursor rs = dbHelper.getShop(ShopID);
            rs.moveToFirst();
            String ShopName = rs.getString(rs.getColumnIndex(ShopDBHelper.Shop_COLUMN_NAME));

            String ShopTel = rs.getString(rs.getColumnIndex(ShopDBHelper.Shop_COLUMN_TEL));

            String ShopAddress = rs.getString(rs.getColumnIndex(ShopDBHelper.Shop_COLUMN_ADDRESS));

            String ShopInfo = rs.getString(rs.getColumnIndex(ShopDBHelper.Shop_COLUMN_INFO));

            String ShopLAT = rs.getString(rs.getColumnIndex(ShopDBHelper.Shop_COLUMN_LAT));

            String ShopLNG = rs.getString(rs.getColumnIndex(ShopDBHelper.Shop_COLUMN_LNG));

            if (!rs.isClosed()) {
                rs.close();
            }


            nameEditText.setText(ShopName);
            nameEditText.setEnabled(true);
            nameEditText.setFocusableInTouchMode(true);
            nameEditText.setClickable(true);

            telEditText.setText(ShopTel);
            telEditText.setEnabled(true);
            telEditText.setFocusableInTouchMode(true);
            telEditText.setClickable(true);

            OldAddress = ShopAddress;
            addressEditText.setText(ShopAddress);
            addressEditText.setEnabled(true);
            addressEditText.setFocusableInTouchMode(true);
            addressEditText.setClickable(true);

            infoEditText.setText(ShopInfo);
            infoEditText.setEnabled(true);
            infoEditText.setFocusableInTouchMode(true);
            infoEditText.setClickable(true);

            ShopLATTextView.setText(ShopLAT);
            ShopLNGTextView.setText(ShopLNG);


        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.saveButton:
                if(OldAddress != null) {
                    if (OldAddress.equals(addressEditText.getText().toString()))
                        persistShop();
                    else
                        new GetCoordinates().execute(addressEditText.getText().toString().replace(" ", "+"));
                } else {
                    new GetCoordinates().execute(addressEditText.getText().toString().replace(" ", "+"));
                }
                return;
            case R.id.deleteButton:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage(R.string.deletePerson)
                        .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dbHelper.deleteShop(ShopID);
                                Toast.makeText(getApplicationContext(), "Deleted Successfully", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
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

    public void persistShop() {
        if(ShopID > 0) {
            if(dbHelper.updateShop(
                    ShopID,
                    nameEditText.getText().toString(),
                    telEditText.getText().toString(),
                    addressEditText.getText().toString(),
                    infoEditText.getText().toString(),
                    ShopLATTextView.getText().toString(),
                    ShopLNGTextView.getText().toString()
                    ))
            {
                Toast.makeText(getApplicationContext(), "Shop Update Successful", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
            else {
                Toast.makeText(getApplicationContext(), "Shop Update Failed", Toast.LENGTH_SHORT).show();
            }
        }
        else {
            if(dbHelper.insertShop(
                    nameEditText.getText().toString(),
                    telEditText.getText().toString(),
                    addressEditText.getText().toString(),
                    infoEditText.getText().toString(),
                    ShopLATTextView.getText().toString(),
                    ShopLNGTextView.getText().toString()
                    ))
            {
                Toast.makeText(getApplicationContext(), "Shop Inserted", Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(getApplicationContext(), "Could not Insert Shop", Toast.LENGTH_SHORT).show();
            }
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
    }

    private class GetCoordinates extends AsyncTask<String,Void,String> {
        ProgressDialog dialog = new ProgressDialog(Shop_CreateOrEditActivity.this);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog.setMessage("Please wait....");
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            String response;
            try{
                String address = strings[0];
                HttpDataHandler http = new HttpDataHandler();
                String url = String.format("https://maps.googleapis.com/maps/api/geocode/json?address=%s&key=AIzaSyDLoZl3HFqGVD7vvIoES2mjM8YRQESHSXs",address);
                response = http.getHTTPData(url);
                return response;
            }
            catch (Exception ex)
            {

            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            try{
                JSONObject jsonObject = new JSONObject(s);

                String lat = ((JSONArray)jsonObject.get("results")).getJSONObject(0).getJSONObject("geometry")
                        .getJSONObject("location").get("lat").toString();
                String lng = ((JSONArray)jsonObject.get("results")).getJSONObject(0).getJSONObject("geometry")
                        .getJSONObject("location").get("lng").toString();
                Toast.makeText(getApplicationContext(), String.format("Coordinates : %s / %s ",lat,lng), Toast.LENGTH_SHORT).show();
                if(lat != null && lng != null) {
                    ShopLATTextView.setText(lat);
                    ShopLNGTextView.setText(lng);
                    persistShop();
                }

                //txtCoord.setText(String.format("Coordinates : %s / %s ",lat,lng));

                if(dialog.isShowing())
                    dialog.dismiss();

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

}

