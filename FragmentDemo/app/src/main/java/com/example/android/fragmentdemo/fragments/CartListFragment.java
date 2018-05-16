package com.example.android.fragmentdemo.fragments;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.fragmentdemo.R;
import com.example.android.fragmentdemo.adapters.CartAdapter;
import com.example.android.fragmentdemo.adapters.OrderHistoryAdapter;
import com.example.android.fragmentdemo.data.HistoryOfOrders;
import com.example.android.fragmentdemo.data.Items;
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
    private OrderHistoryAdapter mOrderHistoryAdapter;

    private CartAdapter mItemsAdapter;

    public CartListFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_cart, container, false);

        final ListView listView = (ListView) rootView.findViewById(R.id.list_view_cart);

        final List<Items> itemsList = new ArrayList<>();
        mItemsAdapter = new CartAdapter(getContext(), itemsList);
        listView.setAdapter(mItemsAdapter);

        final DatabaseReference referenceBal = FirebaseDatabase.getInstance().getReference()
                .child("users").child(FirebaseAuth.getInstance().getUid()).child("balance");

        mReference = FirebaseDatabase.getInstance().getReference()
                .child("users").child(FirebaseAuth.getInstance().getUid()).child("cart");

        final DatabaseReference referenceCart = FirebaseDatabase.getInstance().getReference()
                .child("users").child(FirebaseAuth.getInstance().getUid()).child("cart");

        final DatabaseReference reference = FirebaseDatabase.getInstance().getReference()
                .child("users").child(FirebaseAuth.getInstance().getUid());

        final DatabaseReference referenceTotal = FirebaseDatabase.getInstance().getReference()
                .child("users").child(FirebaseAuth.getInstance().getUid()).child("total");

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
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        };
        mReference.addChildEventListener(mChildEventListener);

        final TextView totalBill = (TextView) rootView.findViewById(R.id.total_to_be_paid);

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

                referenceTotal.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String total = dataSnapshot.getValue(String.class);
                        Log.v("tag taggg", "==" + total);
                        totalBill.setText(total);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        Button buyNow = (Button) rootView.findViewById(R.id.buy_now_button);
        buyNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setMessage("Confirm order?");
                builder.setPositiveButton("Buy Now", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        reference.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {

                                String bal = dataSnapshot.child("balance").getValue(String.class);
                                String tot = dataSnapshot.child("total").getValue(String.class);

                                Integer balInt = 0;
                                Integer totalInt = 0;

                                if (tot.equals("0")) {
                                    Toast.makeText(getContext(), "Cart Empty", Toast.LENGTH_SHORT).show();
                                }

                                if (bal != null) {
                                    balInt = Integer.parseInt(bal);
                                    totalInt = Integer.parseInt(tot);
                                } else {
                                    Toast.makeText(getContext(), "No Balance - Recharge in Payment", Toast.LENGTH_LONG).show();
                                }

                                if (balInt >= totalInt && totalInt > 0) {
                                    Calendar calendar = Calendar.getInstance();
                                    SimpleDateFormat mdformat = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss");
                                    final String dateTime = mdformat.format(calendar.getTime());

                                    Integer newBal = balInt - totalInt;

                                    referenceBal.setValue(String.valueOf(newBal));

                                    final DatabaseReference referenceTotal = FirebaseDatabase.getInstance().getReference()
                                            .child("users").child(FirebaseAuth.getInstance().getUid())
                                            .child("total");

                                    final DatabaseReference cartRef = FirebaseDatabase.getInstance().getReference()
                                            .child("users").child(FirebaseAuth.getInstance().getUid())
                                            .child("cart");

                                    referenceTotal.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            final String totalVal = dataSnapshot.getValue(String.class);

                                            cartRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(DataSnapshot dataSnapshot) {

                                                    HistoryOfOrders his = new HistoryOfOrders(dateTime, totalVal);
                                                    reference.child("history").push().setValue(his);
                                                    referenceTotal.setValue("0");
                                                    cartRef.removeValue();
                                                    mItemsAdapter.clear();
                                                    Toast.makeText(getContext(), "Order successful", Toast.LENGTH_SHORT).show();
                                                }

                                                @Override
                                                public void onCancelled(DatabaseError databaseError) {

                                                }
                                            });
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });
                                } else if (totalInt > balInt) {
                                    Toast.makeText(getContext(), "Insufficient balance", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

                    }
                });

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (dialogInterface != null) {
                            dialogInterface.dismiss();
                        }
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();

            }
        });

        return rootView;
    }
}
