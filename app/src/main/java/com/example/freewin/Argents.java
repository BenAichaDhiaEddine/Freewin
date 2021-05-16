package com.example.freewin;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.freewin.AssosiationRecycleView.Assosiation;
import com.example.freewin.AssosiationRecycleView.AssosiationViewHolder;
import com.example.freewin.MatchRecycleView.Match;
import com.example.freewin.MatchRecycleView.MatchViewHolder;
import com.example.freewin.TirageRecycleView.Tirage;
import com.example.freewin.TirageRecycleView.TirageViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
public class Argents extends Fragment {
    String checkedRadioButton;
    RecyclerView ArgentMatchRecycleView;
    RecyclerView ArgentTirageRecycleView;
    private FirebaseRecyclerOptions<Match> options1;
    private FirebaseRecyclerAdapter<Match, MatchViewHolder> adapter1;
    private FirebaseRecyclerOptions<Tirage> options2;
    private FirebaseRecyclerAdapter<Tirage, TirageViewHolder> adapter2;

    public Argents() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_argents, container, false);
        //Firebase References
        final String user_id = FirebaseAuth.getInstance().getCurrentUser().getUid();
        final DatabaseReference db = FirebaseDatabase.getInstance().getReference();
        final DatabaseReference current_user_db = db.child("Users").child(user_id);
        final DatabaseReference current_user_points_db = current_user_db.child("points");
        //find view by id
        ArgentMatchRecycleView = view.findViewById(R.id.ArgentMatchRecycleView);
        ArgentTirageRecycleView = view.findViewById(R.id.ArgentTirageRecycleView);



        //setting the ArgentMatch recycleview firebase
        ArgentMatchRecycleView.setLayoutManager(new LinearLayoutManager(getContext()));
        options1 = new FirebaseRecyclerOptions.Builder<Match>()
                .setQuery(db.child("argentMatch"), Match.class).build();
        adapter1 = new FirebaseRecyclerAdapter<Match, MatchViewHolder>(options1) {
            @Override
            protected void onBindViewHolder(@NonNull final MatchViewHolder holder, int i, @NonNull final Match model) {
                Log.d("argentModel.num",String.valueOf(model.num));
                holder.equipe1.setText(model.getEquipe1());
                holder.equipe2.setText(model.getEquipe2());
                holder.cout.setText("Cout : " +model.getCout()+" Points");
                holder.radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                        if (checkedId==holder.equipe1.getId()){
                            checkedRadioButton=model.equipe1.toString();
                        }
                        if (checkedId==holder.equipe2.getId()){
                            checkedRadioButton=model.equipe2.toString();
                        }
                        if (checkedId==holder.x.getId()){
                            checkedRadioButton=String.valueOf(model.num);
                        }
                    }
                });
                holder.ParierMatch.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                        builder.setMessage("parier sur l'equipe gagnante sur ce match ,un des FREEWINERS qui ont parié sur le resultat correct gagnera 250 dt")
                                .setPositiveButton("Parier", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        if (checkedRadioButton != null) {
                                            Log.d("argentCheckedRadioBtn",checkedRadioButton.toString());
                                            if (!((checkedRadioButton.equals(model.getEquipe1().toString()) || (checkedRadioButton.equals(model.getEquipe2().toString())) || (checkedRadioButton.equals(String.valueOf(model.num))))))
                                            {
                                                Toast.makeText(getActivity(), "veuillez choisir un choix dans ce match ", Toast.LENGTH_SHORT).show();
                                                holder.radioGroup.clearCheck();
                                            }
                                            else if(checkedRadioButton.equals(model.getEquipe1()) || (checkedRadioButton.equals(model.getEquipe2().toString())) || (checkedRadioButton.equals(String.valueOf(model.num))))
                                            {
                                                current_user_points_db.addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                        if (Integer.parseInt(dataSnapshot.getValue().toString()) < Integer.parseInt(String.valueOf(model.getCout())))
                                                        {
                                                            Toast.makeText(getActivity(), "vous n'avez pas assez de points", Toast.LENGTH_SHORT).show();
                                                        }
                                                        else if (Integer.parseInt(dataSnapshot.getValue().toString()) >= Integer.parseInt(String.valueOf(model.num))) {
                                                            current_user_points_db.setValue(Integer.parseInt(dataSnapshot.getValue().toString()) - Integer.parseInt(String.valueOf(model.getCout())));
                                                            if (checkedRadioButton.equals(model.getEquipe1())) {
                                                                db.child("tirage").child("match" + model.getNum()).child(model.getEquipe1()).child(user_id).setValue("1");
                                                            } else if (checkedRadioButton.equals(model.getEquipe2())) {
                                                                db.child("tirage").child("match" + model.getNum()).child(model.getEquipe2()).child(user_id).setValue("1");
                                                            } else if (checkedRadioButton.equals(String.valueOf(model.num))) {
                                                                db.child("tirage").child("match" + String.valueOf(model.num)).child("x").child(user_id).setValue("1");
                                                            }
                                                            holder.ParierMatch.setEnabled(false);
                                                            Toast.makeText(getActivity(), "Vous Avez Participer ", Toast.LENGTH_SHORT).show();
                                                        }
                                                    }

                                                    @Override
                                                    public void onCancelled(@NonNull DatabaseError databaseError) {
                                                    }
                                                });
                                            }
                                        }
                                        else {
                                            Toast.makeText(getContext(), "veuillez choisir un choix dans un match", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }).create().show();
                    }
                });
            }
            @NonNull
            @Override
            public MatchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(getActivity()).inflate(R.layout.tirage_match_item,parent,false);
                return new MatchViewHolder(view) ;
            }
        };
        adapter1.startListening();
        ArgentMatchRecycleView.setAdapter(adapter1);
        if (adapter1!=null)
            adapter1.startListening();

        //setting the argent tirage recycleView
        ArgentTirageRecycleView.setLayoutManager(new LinearLayoutManager(getContext()));
        options2 = new FirebaseRecyclerOptions.Builder<Tirage>()
                .setQuery(db.child("argentTirage"), Tirage.class).build();
        adapter2 = new FirebaseRecyclerAdapter<Tirage, TirageViewHolder>(options2) {
            @NonNull
            @Override
            public TirageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(getActivity()).inflate(R.layout.tirage_normal_item,parent,false);
                return new TirageViewHolder(view) ;
            }
            @Override
            protected void onBindViewHolder(@NonNull final TirageViewHolder holder, int i, @NonNull final Tirage model) {
                holder.tirage.setText(model.text);
                holder.textView.setText("Cout: "+String.valueOf(model.cout + " Points"));
                holder.tirage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlertDialog.Builder builder =new AlertDialog.Builder(getContext());
                        builder.setMessage(model.textDialog)
                                .setPositiveButton("gagner", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        current_user_points_db.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                if (Integer.parseInt(dataSnapshot.getValue().toString())<model.cout){
                                                    Toast.makeText(getActivity(), "vous n'avez pas assez de points", Toast.LENGTH_SHORT).show();
                                                }
                                                else if (Integer.parseInt(dataSnapshot.getValue().toString())>=model.cout) {
                                                    current_user_points_db.setValue(Integer.parseInt(dataSnapshot.getValue().toString())-model.cout);
                                                    db.child("tirage").child(model.nomTirage).child(user_id).setValue(1);
                                                    holder.tirage.setEnabled(false);
                                                    Toast.makeText(getActivity(), "Vous Avez Participer ", Toast.LENGTH_SHORT).show();
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
        };
        adapter2.startListening();
        ArgentTirageRecycleView.setAdapter(adapter2);
        if (adapter2!=null)
            adapter2.startListening();
        return view;
    }
    @Override
    public void onStop() {
        if (adapter1!=null)
            adapter1.stopListening();
        if (adapter2!=null)
            adapter2.stopListening();
        super.onStop();
    }
}