package com.example.android.fragmentdemo.fragments;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.fragmentdemo.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by hp on 3/28/2018.
 */

public class PaymentFragment extends Fragment {

    public PaymentFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_payment, container, false);

        final EditText rechargeAmount = (EditText) rootView.findViewById(R.id.recharge_amount);
        final TextView balance = (TextView) rootView.findViewById(R.id.balance_payment);

        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference()
                .child("users").child(FirebaseAuth.getInstance().getUid()).child("balance");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String bal = dataSnapshot.getValue(String.class);
                balance.setText(String.valueOf(bal));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        TextView rechargeButton = (TextView) rootView.findViewById(R.id.recharge_button);
        rechargeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String amount = rechargeAmount.getText().toString();

                if (amount.isEmpty()) {
                    Toast.makeText(getContext(), "Enter Amount", Toast.LENGTH_SHORT).show();
                } else if (Integer.parseInt(amount) < 50) {
                    Toast.makeText(getContext(), "Amount too low", Toast.LENGTH_SHORT).show();
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setMessage("Confirm recharge?");
                    builder.setPositiveButton("Recharge Now", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {

                                    Integer intAmt = 0;

                                    if (dataSnapshot.getValue(String.class) != null) {
                                        String amt = dataSnapshot.getValue(String.class);
                                        intAmt = Integer.parseInt(amt);
                                    }

                                    Integer intAmount = Integer.parseInt(amount);
                                    Integer newAmt = intAmt + intAmount;

                                    if (intAmount >= 50) {
                                        databaseReference.setValue(String.valueOf(newAmt));
                                        Toast.makeText(getContext(), "Recharge successful", Toast.LENGTH_SHORT).show();
                                        rechargeAmount.setText(" ");
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
            }
        });

        return rootView;
    }
}
