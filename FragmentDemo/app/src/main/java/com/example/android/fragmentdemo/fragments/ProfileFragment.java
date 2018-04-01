package com.example.android.fragmentdemo.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.fragmentdemo.R;

/**
 * Created by hp on 3/28/2018.
 */

public class ProfileFragment extends Fragment {

    public ProfileFragment(){

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_profile,container,false);

     /*   final List<Items> items = new ArrayList<Items>();
        items.add(new Items("lays1", (long) 20));
        items.add(new Items("lays2", (long) 20));

        ListView listView = (ListView) rootView.findViewById(R.id.list_order_history);
        OrderHistoryAdapter adapter = new OrderHistoryAdapter(getContext(), items);
        listView.setAdapter(adapter);*/

        return rootView;
    }
}
