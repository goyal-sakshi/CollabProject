package com.example.android.fragmentdemo.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.example.android.fragmentdemo.R;
import com.example.android.fragmentdemo.adapters.OrderHistoryAdapter;
import com.example.android.fragmentdemo.data.orderHistory;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hp on 3/28/2018.
 */

public class ProfileFragment extends Fragment {

    private OrderHistoryAdapter mAdapter;
    private ChildEventListener mChildEventListener;

    public ProfileFragment(){

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_profile,container,false);

        TextView username = (TextView) rootView.findViewById(R.id.username_profile);
        final TextView balance = (TextView) rootView.findViewById(R.id.balance_profile);

        String user = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
        username.setText(user);

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference()
                .child("users").child(FirebaseAuth.getInstance().getUid()).child("balance");

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String bal = dataSnapshot.getValue(String.class);
                balance.setText(String.valueOf(bal));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        ListView listView = (ListView) rootView.findViewById(R.id.list_order_history);
        List<orderHistory> items = new ArrayList<>();
        mAdapter = new OrderHistoryAdapter(getContext(),items);
        listView.setAdapter(mAdapter);

        DatabaseReference dbReference = FirebaseDatabase.getInstance().getReference()
                .child("users").child("history");

        if(mChildEventListener == null){
            mChildEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    orderHistory items = dataSnapshot.getValue(orderHistory.class);
                    mAdapter.add(items);
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
            dbReference.addChildEventListener(mChildEventListener);
        }

        return rootView;
    }
}
