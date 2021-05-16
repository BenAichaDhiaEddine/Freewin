package com.example.freewin;


import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


/**
 * A simple {@link Fragment} subclass.
 */
public class Partage extends Fragment {
    TextView codeParainage;
    EditText inputCodeParaiange;
    Button partage;
    Button valider;
    LinearLayout validerLayout;
    static boolean codeExist;
    public Partage() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View frag=  inflater.inflate(R.layout.fragment_partage, container, false);
        //find views
        codeParainage=frag.findViewById(R.id.code_parainage);
        inputCodeParaiange=frag.findViewById(R.id.InputCodeParainage);
        partage=frag.findViewById(R.id.partage_partager);
        valider=frag.findViewById(R.id.partage_valider);
        validerLayout=frag.findViewById(R.id.validerLayout);
        //firebase refferences
        final DatabaseReference db= FirebaseDatabase.getInstance().getReference();
        final String user_id= FirebaseAuth.getInstance().getUid();
        db.child("codeParainage").child(user_id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                codeParainage.setText(dataSnapshot.getValue().toString().trim());
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });


        //OnClick Listeners
        partage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, " Entrer mon code de parrainage dans l'application FREEWIN pour gagner 500 points : " + codeParainage.getText().toString().trim());
                sendIntent.setType("text/plain");
                Intent shareIntent = Intent.createChooser(sendIntent, null);
                startActivity(shareIntent);
            }
        });
        valider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //get the code
                final String input = inputCodeParaiange.getText().toString().trim();
                if (input.isEmpty()) {
                    Toast.makeText(getActivity(), "Veuillez entrer un code", Toast.LENGTH_SHORT).show();
                } else {
                    db.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            //search for the userid tha has the code
                            for (DataSnapshot postSnapshot : dataSnapshot.child("codeParainage").getChildren()) {
                                //if u find it
                                if (postSnapshot.getValue().toString().trim().equals(input)) {
                                    codeExist=true;
                                    //get user2000 id
                                    final String userID_2000 = postSnapshot.getKey();
                                    //check if the user2000id different to user500id
                                    if (userID_2000.trim().equals(user_id.trim())) {
                                        Toast.makeText(getActivity(), "ce code est ton propre code", Toast.LENGTH_LONG).show();
                                    }
                                    else if (!userID_2000.equals(user_id)) {
                                        db.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                //get user2000 points
                                                String user2000points = dataSnapshot.child("Users").child(userID_2000).child("points").getValue().toString();
                                                //add to user2000 2000 points
                                                db.child("Users").child(userID_2000).child("points").setValue(Integer.parseInt(user2000points) + 2000);
                                                //set to user500 valider 1 to enable button
                                                db.child("Users").child(user_id).child("valider").setValue("1");
                                                //add to user500 500 points
                                                String user500points = dataSnapshot.child("Users").child(user_id).child("points").getValue().toString();
                                                db.child("Users").child(user_id).child("points").setValue(Integer.parseInt(user500points) + 500);
                                                validerLayout.setVisibility(View.GONE);
                                                valider.setVisibility(View.GONE);
                                            }
                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                            }
                                        });
                                    }
                                }
                            }
                            if (codeExist==false){
                                Toast.makeText(getActivity(), "ce code n'est pas valide", Toast.LENGTH_SHORT).show();
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                        }
                    });
                }
            }
        });
        codeExist=false;
        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child("Users").child(user_id).child("valider").exists()){
                    valider.setVisibility(View.GONE);
                    validerLayout.setVisibility(View.GONE);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
        return frag;
    }
}