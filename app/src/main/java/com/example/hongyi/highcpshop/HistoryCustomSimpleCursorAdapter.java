package com.example.hongyi.highcpshop;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import static com.example.hongyi.highcpshop.Main3Activity.KEY_EXTRA_CONTACT_ID;

/**
 * Created by hongyi on 2017/12/13.
 */

public class HistoryCustomSimpleCursorAdapter extends SimpleCursorAdapter {

    Context conText;

    int shopID;
    int CustomerID;
    String CustomerName;
    Cursor csr;

    public HistoryCustomSimpleCursorAdapter(Context context, int layout, Cursor c, String[] from, int[] to, int flags, int ShopID) {
        super(context, layout, c, from, to, flags);
        conText = context;
        shopID = ShopID;
        csr = c;
    }

    @Override
    public void bindView(View view, final Context context, Cursor cursor) {
        // TODO Auto-generated method stub
        super.bindView(view, context, cursor);
    }

    @Override
    public View getView(final int position, View  convertView, ViewGroup parent) {

        final View vw = super.getView(position,convertView,parent);

        Button btn = (Button) vw.findViewById(R.id.button2);
        btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //Toast.makeText(conText,String.valueOf(position),Toast.LENGTH_SHORT).show();
                TextView tvw = (TextView) vw.findViewById(R.id.CustomName);

                CustomerName = tvw.getText().toString();
                TextView tvww = (TextView) vw.findViewById(R.id.OrderID);
                CustomerID = Integer.parseInt(tvww.getText().toString());

                Intent intent = new Intent(conText, HistoryOrder_CreateOrEditActivity.class);
                intent.putExtra(KEY_EXTRA_CONTACT_ID, String.valueOf(CustomerID) + "," + CustomerName+","+shopID);
                conText.startActivity(intent);
            }
        });

        return vw;

    }

    public void setItem(int customerID,String customerName) {

        CustomerID = customerID;
        CustomerName = customerName;

    }

}
