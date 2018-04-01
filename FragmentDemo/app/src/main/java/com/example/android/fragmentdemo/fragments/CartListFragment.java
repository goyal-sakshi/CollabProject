package com.example.android.fragmentdemo.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.fragmentdemo.R;
import com.example.android.fragmentdemo.adapters.CartAdapter;
import com.example.android.fragmentdemo.data.Items;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hp on 3/28/2018.
 */

public class CartListFragment extends android.support.v4.app.Fragment {

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mReference;
    private ChildEventListener mChildEventListener;

    private CartAdapter mItemsAdapter;

    public CartListFragment(){

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_cart, container, false);


        ListView listView = (ListView) rootView.findViewById(R.id.list_view_cart);
        TextView total = (TextView) rootView.findViewById(R.id.total_to_be_paid);

        final List<Items> items = new ArrayList<>();
        mItemsAdapter = new CartAdapter(getContext(),items);
        listView.setAdapter(mItemsAdapter);

        Toast.makeText(getContext(), "Your balance: ", Toast.LENGTH_SHORT).show();

        mReference = FirebaseDatabase.getInstance().getReference()
                .child("users").child(FirebaseAuth.getInstance().getUid()).child("cart");

        if (mChildEventListener == null) {
            mChildEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    Items items = dataSnapshot.getValue(Items.class);
                    mItemsAdapter.add(items);
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {
                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            };
            mReference.addChildEventListener(mChildEventListener);
        }

        Button buyNow = (Button) rootView.findViewById(R.id.buy_now_button);
        buyNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseReference dbReference = FirebaseDatabase.getInstance().getReference()
                        .child("users").child(FirebaseAuth.getInstance().getUid())
                        .child("cart").child("quantity");
            }
        });

        return rootView;
    }
}
