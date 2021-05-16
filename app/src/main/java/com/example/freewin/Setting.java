package com.example.freewin;


import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


/**
 * A simple {@link Fragment} subclass.
 */
public class Setting extends Fragment {

    Button sign_out_btn;
    Button btn_delete_account;
    Button modifier_profil;
    TextView profil_nom;
    TextView profil_prenom;
    TextView profil_age;
    TextView profil_sexe;
    TextView profil_secteur;
    TextView profil_SA;
    TextView profil_MT;
    TextView profil_gouvernorate;

    public Setting() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View frag = inflater.inflate(R.layout.fragment_setting, container, false);

        //textView find
        profil_nom=frag.findViewById(R.id.profil_nom);
        profil_prenom=frag.findViewById(R.id.profil_prenom);
        profil_age=frag.findViewById(R.id.profil_age);
        profil_sexe=frag.findViewById(R.id.profil_sexe);
        profil_secteur=frag.findViewById(R.id.profil_secteur);
        profil_SA=frag.findViewById(R.id.profil_SA);
        profil_MT=frag.findViewById(R.id.profil_mt);
        profil_gouvernorate=frag.findViewById(R.id.profil_gouvernorate);

        //read values

        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final String user_id = user.getUid();
        final DatabaseReference current_user_profil_db = FirebaseDatabase.getInstance().getReference().child("Users").child(user_id).child("profil");
        current_user_profil_db.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child("nom").exists()){
                    profil_nom.setText(dataSnapshot.child("nom").getValue().toString());
                }
                if (dataSnapshot.child("prenom").exists()){
                    profil_prenom.setText(dataSnapshot.child("prenom").getValue().toString());
                }
                if (dataSnapshot.child("age").exists()){
                    profil_age.setText(dataSnapshot.child("age").getValue().toString());
                }
                if (dataSnapshot.child("sexe").exists()){
                    profil_sexe.setText(dataSnapshot.child("sexe").getValue().toString());
                }
                if (dataSnapshot.child("Secteur").exists()){
                    profil_secteur.setText(dataSnapshot.child("Secteur").getValue().toString());
                }
                if (dataSnapshot.child("gouvernorat").exists()){
                    profil_gouvernorate.setText(dataSnapshot.child("gouvernorat").getValue().toString());
                }
                if (dataSnapshot.child("S A").exists()){
                    profil_SA.setText(dataSnapshot.child("S A").getValue().toString());
                }
                if (dataSnapshot.child("M T").exists()){
                    profil_MT.setText(dataSnapshot.child("M T").getValue().toString());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });








        //sign out button
        sign_out_btn = frag.findViewById(R.id.sign_out_button);
        sign_out_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //signout user auth account
                AuthUI
                        .getInstance()
                        .signOut(getActivity())
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                //disable button
                                sign_out_btn.setEnabled(false);
                                //clear stack and move to login
                                Intent intent = new Intent(frag.getContext(), Login.class);
                                getActivity().finishAffinity();
                                startActivity(intent);
                            }
                        });
            }
        });
        //delete account button
        btn_delete_account = frag.findViewById(R.id.btn_delete_account);
        btn_delete_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //get current user id
                String user_id = FirebaseAuth.getInstance().getCurrentUser().getUid();
                //get current user database
                DatabaseReference current_user_db = FirebaseDatabase.getInstance().getReference().child("Users").child(user_id);
                //delete user database
                current_user_db.removeValue();
                //delete user code parainage
                FirebaseDatabase.getInstance().getReference().child("codeParainage").child(user_id).removeValue();
                //delete user auth account
                AuthUI
                        .getInstance()
                        .delete(getActivity())
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                AuthUI
                                        .getInstance()
                                        .signOut(getActivity())
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                //disable button
                                                sign_out_btn.setEnabled(false);
                                                //clear stack and move to login
                                                Intent intent = new Intent(frag.getContext(), Login.class);
                                                getActivity().finishAffinity();
                                                startActivity(intent);
                                            }
                                        });
                            }
                        });
            }
        });

        //btn modifier profil
        modifier_profil = frag.findViewById(R.id.Modifier_profil);
        modifier_profil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(frag.getContext(), Edit_Profil.class);
                startActivity(intent);
            }
        });
        return frag;
    }


}
