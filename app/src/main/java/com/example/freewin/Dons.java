package com.example.freewin;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.freewin.AssosiationRecycleView.Assosiation;
import com.example.freewin.AssosiationRecycleView.AssosiationViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
/**
 * A simple {@link Fragment} subclass.
 */
public class Dons extends Fragment {
    private RecyclerView recyclerView;
    private DatabaseReference db;
    private FirebaseRecyclerOptions<Assosiation> options;
    private FirebaseRecyclerAdapter<Assosiation, AssosiationViewHolder> adapter;
    private String user_id ;
    public Dons() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_dons, container, false);
        user_id= FirebaseAuth.getInstance().getUid();
        db= FirebaseDatabase.getInstance().getReference();
        recyclerView=view.findViewById(R.id.recycleView);
        //setting the recycle view firebase
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        options = new FirebaseRecyclerOptions.Builder<Assosiation>()
                .setQuery(db.child("assosiations"),Assosiation.class).build();
        adapter= new FirebaseRecyclerAdapter<Assosiation, AssosiationViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull AssosiationViewHolder holder, int i, @NonNull final Assosiation model) {
                holder.nom.setText(model.getNom());
                holder.description.setText(model.getDescription());
                holder.contribuer.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlertDialog.Builder builder=new AlertDialog.Builder(getContext());
                        builder.setMessage("Contribuer par 1 dinar à cette assosiation , ca va te couter 7000 points ")
                                .setPositiveButton("Contribuer", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        db.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                int p = Integer.parseInt(dataSnapshot.child("Users").child(user_id).child("points").getValue().toString());
                                                if (p>=6990){
                                                    p-=6990;
                                                    db.child("Users").child(user_id).child("points").setValue(p);
                                                    int a = Integer.parseInt(dataSnapshot.child("assosiations").child(model.getNom()).child("argent").getValue().toString());
                                                    a+=1;
                                                    db.child("assosiations").child(model.getNom()).child("argent").setValue(a);
                                                }
                                                else {
                                                    Toast.makeText(getContext(), "Vous n'avez pas assez de points", Toast.LENGTH_LONG).show();
                                                }
                                            }
                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                            }
                                        });
                                    }
                                }).create().show();
                    }
                });
            }
            @NonNull
            @Override
            public AssosiationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(getActivity()).inflate(R.layout.assosiations_item,parent,false);
                return new AssosiationViewHolder(view) ;
            }
        };
        adapter.startListening();
        recyclerView.setAdapter(adapter);

        if (adapter!=null)
            adapter.startListening();
        return view;
    }
    @Override
    public void onStop() {
        if (adapter!=null)
            adapter.stopListening();
        super.onStop();

    }
}
