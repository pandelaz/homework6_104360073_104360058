package com.example.hongyi.highcpshop;

import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

/**
 * Created by hongyi on 2017/12/13.
 */

public class OrderInfoSimpleCursorAdapter extends SimpleCursorAdapter {

    Context conText;

    int shopID;

    String CustomerName;
    Cursor csr;

    TextView tv;

    public OrderInfoSimpleCursorAdapter(Context context, int layout, Cursor c, String[] from, int[] to, int flags, int ShopID,TextView t) {
        super(context, layout, c, from, to, flags);
        conText = context;
        shopID = ShopID;
        csr = c;
        tv = t;
    }

    @Override
    public void bindView(View view, final Context context, Cursor cursor) {
        // TODO Auto-generated method stub
        super.bindView(view, context, cursor);
    }

    @Override
    public View getView(final int position, View  convertView, ViewGroup parent) {

        final View vw = super.getView(position,convertView,parent);

        final EditText edt = (EditText) vw.findViewById(R.id.OrderQuantity);
        final TextView tvw = (TextView) vw.findViewById(R.id.OrderPrice);

        ImageButton minusbtn = (ImageButton) vw.findViewById(R.id.imageButton2);
        minusbtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                //Toast.makeText(conText,"minus"+String.valueOf(position),Toast.LENGTH_SHORT).show();

                int num = Integer.parseInt(edt.getText().toString());
                int num2 = Integer.parseInt(tv.getText().toString());
                int num3 = Integer.parseInt(tvw.getText().toString());
                if(num != 0) {
                    num--;
                    num2 = num2-num3;
                    edt.setText(String.valueOf(num));
                    tv.setText(String.valueOf(num2));
                }

            }
        });

        ImageButton plusbtn = (ImageButton) vw.findViewById(R.id.imageButton);
        plusbtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                //Toast.makeText(conText,"plus"+String.valueOf(position),Toast.LENGTH_SHORT).show();

                int num = Integer.parseInt(edt.getText().toString());
                int num2 = Integer.parseInt(tv.getText().toString());
                int num3 = Integer.parseInt(tvw.getText().toString());
                if(num != 99) {
                    num++;
                    num2 = num2+num3;
                    edt.setText(String.valueOf(num));
                    tv.setText(String.valueOf(num2));
                }
            }
        });


        return vw;

    }


}
