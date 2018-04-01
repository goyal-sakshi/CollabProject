package com.example.android.fragmentdemo.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.android.fragmentdemo.R;
import com.example.android.fragmentdemo.data.Items;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

/**
 * Created by hp on 3/27/2018.
 */

public class ItemsAdapter extends ArrayAdapter<Items>{

    public ItemsAdapter(@NonNull Context context, List<Items> itemIds) {
        super(context,0,itemIds);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_home, parent, false);
        }

        final Items currentItem = getItem(position);

        TextView itemNameTextView = (TextView) convertView.findViewById(R.id.item_name_text_view);
        itemNameTextView.setText(currentItem.getName());

        TextView itemPriceTextView = (TextView) convertView.findViewById(R.id.item_price_text_view);
        itemPriceTextView.setText( currentItem.getPrice());

        ImageView itemImageView =(ImageView) convertView.findViewById(R.id.item_img_view);
        boolean isPhoto = currentItem.getImageUrl() != null;
        if(isPhoto){
            itemImageView.setVisibility(View.VISIBLE);
            Glide.with(itemImageView.getContext())
                    .load(currentItem.getImageUrl())
                    .into(itemImageView);
        }

        TextView addToCartButton = (TextView) convertView.findViewById(R.id.add_to_cart_btn);
        addToCartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference()
                        .child("users").child(FirebaseAuth.getInstance().getUid()).child("cart");

                Items items = new Items(currentItem.getPrice(), "1", currentItem.getName(),currentItem.getImageUrl());
                databaseReference.child(currentItem.getName()).setValue(items);

                Toast.makeText(getContext(), items.getName() + " added to yor CART", Toast.LENGTH_SHORT).show();
            }
        });

        return convertView;
    }
}
