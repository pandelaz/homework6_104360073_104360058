package com.example.hongyi.highcpshop;

import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

/**
 * Created by hongyi on 2017/12/13.
 */

public class HistoryOrderInfoSimpleCursorAdapter extends SimpleCursorAdapter {

    Context conText;

    int shopID;
    int CustomerID;
    String CustomerName;
    Cursor csr;

    public HistoryOrderInfoSimpleCursorAdapter(Context context, int layout, Cursor c, String[] from, int[] to, int flags, int ShopID) {
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

        final TextView edt = (TextView) vw.findViewById(R.id.OrderQuantity);





        return vw;

    }


}
