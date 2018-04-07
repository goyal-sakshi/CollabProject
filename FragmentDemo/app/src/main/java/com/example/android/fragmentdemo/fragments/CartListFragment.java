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
import com.example.android.fragmentdemo.data.orderHistory;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by hp on 3/28/2018.
 */

public class CartListFragment extends android.support.v4.app.Fragment {

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mReference;
    private ChildEventListener mChildEventListener;

    private CartAdapter mItemsAdapter;

    public CartListFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_cart, container, false);


        ListView listView = (ListView) rootView.findViewById(R.id.list_view_cart);

        final List<Items> items = new ArrayList<>();
        mItemsAdapter = new CartAdapter(getContext(), items);
        listView.setAdapter(mItemsAdapter);

        final DatabaseReference referenceBal = FirebaseDatabase.getInstance().getReference()
                .child("users").child(FirebaseAuth.getInstance().getUid()).child("balance");

        mReference = FirebaseDatabase.getInstance().getReference()
                .child("users").child(FirebaseAuth.getInstance().getUid()).child("cart");

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

        final DatabaseReference referenceCart = FirebaseDatabase.getInstance().getReference()
                .child("users").child(FirebaseAuth.getInstance().getUid()).child("cart");

        final DatabaseReference reference = FirebaseDatabase.getInstance().getReference()
                .child("users").child(FirebaseAuth.getInstance().getUid());

        //"EEE, d MMM yyyy HH:mm:ss"

        final TextView totalBill = (TextView) rootView.findViewById(R.id.total_to_be_paid);

        referenceCart.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                long tot = 0;
                for(DataSnapshot ds:dataSnapshot.getChildren()){

                    Integer quan = Integer.parseInt(String.valueOf(ds.child("quantity").getValue()));
                    Integer pri = Integer.parseInt(String.valueOf(ds.child("price").getValue()));
                    tot += quan * pri;
                }
                reference.child("total").setValue(String.valueOf(tot));
                totalBill.setText(String.valueOf(tot));
                mItemsAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        //Log.v("Dateee", mdformat.format(calendar.getTime()));

        Button buyNow = (Button) rootView.findViewById(R.id.buy_now_button);
        buyNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                reference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        String bal = dataSnapshot.child("balance").getValue(String.class);
                        String tot = dataSnapshot.child("total").getValue(String.class);

                        Integer balInt = Integer.parseInt(bal);
                        Integer totalInt = Integer.parseInt(tot);

                        if(balInt>=totalInt){
                            Calendar calendar = Calendar.getInstance();
                            SimpleDateFormat mdformat = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss");
                            final String dateTime = mdformat.format(calendar.getTime());

                            DatabaseReference referenceTotal = FirebaseDatabase.getInstance().getReference()
                                    .child("users").child(FirebaseAuth.getInstance().getUid())
                                    .child("total");

                            referenceTotal.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    String totalVal = dataSnapshot.getValue(String.class);

                                    orderHistory his = new orderHistory(dateTime, totalVal);

                                    reference.child("history").push().setValue(his);
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                        }
                        else {
                            Toast.makeText(getContext(), "Insufficient balance", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });



            }
        });

        return rootView;
    }
}
