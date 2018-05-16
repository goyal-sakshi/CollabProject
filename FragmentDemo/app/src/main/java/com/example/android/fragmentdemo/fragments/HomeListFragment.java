package com.example.android.fragmentdemo.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.android.fragmentdemo.R;
import com.example.android.fragmentdemo.adapters.ItemsAdapter;
import com.example.android.fragmentdemo.data.Items;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hp on 3/27/2018.
 */

public class HomeListFragment extends Fragment {

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mItemDatabaseReference;
    private ChildEventListener mChildEventListener;

    private FirebaseStorage mFirebaseStorage;
    private StorageReference mStorageReference;

    private ItemsAdapter mItemsAdapter;
    private Context mContext;

    public HomeListFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_home_list, container, false);
        mContext = getContext();

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mFirebaseStorage = FirebaseStorage.getInstance();

        mItemDatabaseReference = mFirebaseDatabase.getReference().child("items");
        mStorageReference = mFirebaseStorage.getReference().child("item_images");


        ListView listView = (ListView) rootView.findViewById(R.id.list_view_home);
        List<Items> items = new ArrayList<>();
        mItemsAdapter = new ItemsAdapter(getContext(), items);
        listView.setAdapter(mItemsAdapter);

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
            mItemDatabaseReference.addChildEventListener(mChildEventListener);
        }

        return rootView;
    }
}
