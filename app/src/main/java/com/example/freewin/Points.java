package com.example.freewin;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 */
public class Points extends Fragment {
    Button gagner_100_point;
    Button tenter_de_gagner_2000_points;
    Button regarder_un_video;
    Button inviter_amis;
    Button completer_votre_profil;
    TextView completer_votre_profil_text;
    TextView recompenseDePreference;
    TextView TenterDeGagnier2000Points;
    TextView RegarderVideo;
    TextView InviterAmis;
    int p;
    int codeparainage;
    TextView timer_100_points;
    TextView timer_2000_points;
    public Long TimeAfter3Hours100pt;
    public Points() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout in the frag
        View frag = inflater.inflate(R.layout.fragment_points, container, false);
        //find views
        gagner_100_point = frag.findViewById(R.id.gagner_100_points_chaque_jour);
        tenter_de_gagner_2000_points = frag.findViewById(R.id.Tenter_de_gagner_2000_Points);
        regarder_un_video = frag.findViewById(R.id.gagner_100_points_En_Regardant_Un_Video);
        inviter_amis = frag.findViewById(R.id.inviter_des_amis);
        completer_votre_profil = frag.findViewById(R.id.Completer_botre_profil);
        completer_votre_profil_text=frag.findViewById(R.id.PointsTextCompleterVotreProfil);
        TenterDeGagnier2000Points=frag.findViewById(R.id.PointsTextTenterDeGagnier2000Points);
        recompenseDePreference=frag.findViewById(R.id.PointsTextrecompenseDePreference);
        RegarderVideo=frag.findViewById(R.id.PointsTextGagnier100Points);
        InviterAmis=frag.findViewById(R.id.PointstextGagner2000Points);
        timer_100_points=frag.findViewById(R.id.timer_100_points);
        timer_2000_points=frag.findViewById(R.id.timer_2000_points);

        final String user_id = FirebaseAuth.getInstance().getCurrentUser().getUid();
        final DatabaseReference db = FirebaseDatabase.getInstance().getReference();
        final DatabaseReference current_user_db = db.child("Users").child(user_id);
        final DatabaseReference current_user_points_db = db.child("Users").child(user_id).child("points");
        //points listener
        current_user_points_db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    p = Integer.parseInt(dataSnapshot.getValue().toString());
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
        //Buttons click listener
        gagner_100_point.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String gagner = "gagner";
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setMessage("gagner 100 point chaque 3 heurs just en cliquant ce bouton , revener apres 3 heurs pour gagner encore")
                        .setPositiveButton(gagner, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                p += 100;
                                current_user_points_db.setValue(p);
                                //set time when finish
                                db.child("tirage").child("100pts").child(user_id).setValue((Integer.parseInt(String.valueOf((System.currentTimeMillis()/1000/60)+180))));
                                gagner_100_point.setEnabled(false);

                            }
                        }).create().show();
            }
        });
        tenter_de_gagner_2000_points.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String gagner="participer";
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setMessage("participer à un tirage au sort sur 2000 points . la resultat sera annoncé aujourd'hui à 19:00 heure ")
                        .setPositiveButton(gagner, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                db.child("tirage").child("2000pts").child(user_id).setValue("1");
                                tenter_de_gagner_2000_points.setEnabled(false);

                            }
                        }).create().show();
            }
        });
        completer_votre_profil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String gagner="gagner 2000 points";
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setMessage("completer votre profil et gagner 2000 points")
                        .setPositiveButton(gagner, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(getContext(), Edit_Profil.class);
                                startActivity(intent);
                            }
                        }).create().show();
            }
        });
        //inviter amis on click listner
        inviter_amis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String gagner="inviter";
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setMessage("inviter des amis sur les reseaux sociaux et gagner 2000 points pour chaque amis qui nous rejoint")
                        .setPositiveButton(gagner, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //read code parainage
                                db.child("codeParainage").child(user_id).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        if (dataSnapshot.exists())
                                            codeparainage= Integer.parseInt(dataSnapshot.getValue().toString());
                                    }
                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {
                                    }
                                });
                                Intent sendIntent = new Intent();
                                sendIntent.setAction(Intent.ACTION_SEND);
                                sendIntent.putExtra(Intent.EXTRA_TEXT, " Entrer mon code de parrainage dans l'application FREEWIN pour gagner 500 points : " + codeparainage);
                                sendIntent.setType("text/plain");
                                Intent shareIntent = Intent.createChooser(sendIntent, null);
                                startActivity(shareIntent);
                            }
                        }).create().show();
            }
        });
        regarder_un_video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(),Video.class);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                i.putExtra("video","button");
                startActivity(i);
            }
        });
        //if user participate tirage 2000 pts set visibility gone
        db.child("tirage").child("2000pts").child(user_id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    tenter_de_gagner_2000_points.setEnabled(false);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
        //if user complete profil set visibility gone
        current_user_db.child("profilCompleted").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    completer_votre_profil.setVisibility(View.GONE);
                    completer_votre_profil_text.setVisibility(View.GONE);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
        // if user participate in gagner 100 points
        db.child("tirage").child("100pts").child(user_id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists() &&((Integer.parseInt(dataSnapshot.getValue().toString()))>=(Integer.parseInt(String.valueOf(System.currentTimeMillis()/1000/60))))){
                    gagner_100_point.setEnabled(false);
                }
                //if 100 points after 3 hours
                if (dataSnapshot.exists() &&((Integer.parseInt(dataSnapshot.getValue().toString()))<(Integer.parseInt(String.valueOf(System.currentTimeMillis()/1000/60))))){
                    gagner_100_point.setEnabled(true);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });


    return frag;
    }


}