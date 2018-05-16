package com.example.android.fragmentdemo.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.android.fragmentdemo.R;
import com.example.android.fragmentdemo.data.Items;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

/**
 * Created by hp on 3/28/2018.
 */

public class CartAdapter extends ArrayAdapter<Items> {

    public CartAdapter(@NonNull Context context, List<Items> itemIds) {
        super(context, 0, itemIds);
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_cart, parent, false);
        }

        final Items currentItem = getItem(position);

        TextView itemNameTextView = (TextView) convertView.findViewById(R.id.item_name_cart);
        itemNameTextView.setText(currentItem.getName());

        TextView itemPriceTextView = (TextView) convertView.findViewById(R.id.item_price_cart);
        itemPriceTextView.setText(currentItem.getPrice());

        TextView itemQuantityTextView = (TextView) convertView.findViewById(R.id.item_quantity_cart);
        itemQuantityTextView.setText(currentItem.getQuantity());

        ImageView itemImageView = (ImageView) convertView.findViewById(R.id.item_img_cart);
        boolean isPhoto = currentItem.getImageUrl() != null;
        if (isPhoto) {
            itemImageView.setVisibility(View.VISIBLE);
            Glide.with(itemImageView.getContext())
                    .load(currentItem.getImageUrl())
                    .into(itemImageView);
        }


        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference()
                .child("users").child(FirebaseAuth.getInstance().getUid())
                .child("cart").child(currentItem.getName()).child("quantity");

        final DatabaseReference referenceCart = FirebaseDatabase.getInstance().getReference()
                .child("users").child(FirebaseAuth.getInstance().getUid()).child("cart");

        final DatabaseReference referenceTotal = FirebaseDatabase.getInstance().getReference()
                .child("users").child(FirebaseAuth.getInstance().getUid()).child("total");

        Button decrementQuantityButton = (Button) convertView.findViewById(R.id.decrement_qty_button);
        decrementQuantityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String currentQuantity = dataSnapshot.getValue(String.class);
                        Integer currentQ = Integer.parseInt(currentQuantity);
                        if (currentQ > 1) {
                            currentQ = currentQ - 1;
                        }
                        databaseReference.setValue(String.valueOf(currentQ));
                        currentItem.setQuantity(String.valueOf(currentQ));

                        referenceCart.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                long tot = 0;
                                for (DataSnapshot ds : dataSnapshot.getChildren()) {

                                    Integer quan = Integer.parseInt(String.valueOf(ds.child("quantity").getValue()));
                                    Integer pri = Integer.parseInt(String.valueOf(ds.child("price").getValue()));
                                    tot += quan * pri;
                                }
                                referenceTotal.setValue(String.valueOf(tot));
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

                        notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });
        Button IncrementQuantityButton = (Button) convertView.findViewById(R.id.increment_qty_button);
        IncrementQuantityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String currentQuantity = dataSnapshot.getValue(String.class);
                        Integer currentQ = Integer.parseInt(currentQuantity);

                        currentQ = currentQ + 1;

                        databaseReference.setValue(String.valueOf(currentQ));
                        currentItem.setQuantity(String.valueOf(currentQ));

                        referenceCart.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                long tot = 0;
                                for (DataSnapshot ds : dataSnapshot.getChildren()) {

                                    Integer quan = Integer.parseInt(String.valueOf(ds.child("quantity").getValue()));
                                    Integer pri = Integer.parseInt(String.valueOf(ds.child("price").getValue()));
                                    tot += quan * pri;
                                }
                                referenceTotal.setValue(String.valueOf(tot));
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

                        notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });

        final DatabaseReference dbReference = FirebaseDatabase.getInstance().getReference()
                .child("users").child(FirebaseAuth.getInstance().getUid())
                .child("cart");

        TextView removeFromCart = (TextView) convertView.findViewById(R.id.remove_item);
        removeFromCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dbReference.child(currentItem.getName()).removeValue();
                CartAdapter.this.remove(currentItem);
                CartAdapter.this.notifyDataSetChanged();
            }
        });

        return convertView;
    }
}
