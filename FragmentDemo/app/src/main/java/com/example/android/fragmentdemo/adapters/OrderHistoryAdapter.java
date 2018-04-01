package com.example.android.fragmentdemo.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.android.fragmentdemo.R;
import com.example.android.fragmentdemo.data.Items;

import java.util.List;

/**
 * Created by hp on 3/28/2018.
 */

public class OrderHistoryAdapter extends ArrayAdapter<Items> {


    public OrderHistoryAdapter(@NonNull Context context, List<Items> itemIds) {
        super(context,0,itemIds);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_order_history, parent, false);
        }

        Items currentItem = getItem(position);

        TextView itemNameTextView = (TextView) convertView.findViewById(R.id.item_name_order_history);
        itemNameTextView.setText(currentItem.getName());

        TextView itemPriceTextView = (TextView) convertView.findViewById(R.id.item_price_order_history);
        itemPriceTextView.setText(currentItem.getPrice());

        return convertView;
    }
}
