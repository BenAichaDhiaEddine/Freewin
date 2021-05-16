package com.example.freewin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class Edit_Profil extends AppCompatActivity {
    String nom, prenom, age, sexe;
    Spinner G_spinner;
    Spinner S_spinner;
    Spinner SA_spinner;
    Spinner MT_spinner;
    String[] Goptions = {"Ariana", "Beja", "Ben Arous", "Bizert", "Gabes", "Gafsa", "Jendouba", "Kairouan", "Kasserine", "Kebili", "Kef", "Mahdia", "Manouba", "Manouba", "Medenine", "Monastir", "Nabeul", "Sfax", "Sidi Bouzid", "Siliana", "Sousse", "tataouine", "Tozeur", "Tunis", "Zaghouan"};
    String[] Soptions = {"administration", "agriculture", "architecture", "armé ", "defence", "securité", "art et designe", "artisanat", "audiovisuel", "audit et gestion", "automobile", "banque et assurance", "chemie et pharmacie", "commerce", "marketing", "construction", "culture", "droit et justice", "journalisme", "electronique", "energie", "enseignement", "environnement", "finance", "hotellerie et restauration", "immobilier", "industrie", "informatique", "logistique", "maintenance", "management", "mecanique", "mode", "recherhce scientifique", "ressources humaines", "santé", "service à la personne", "sport et loisir", "tourisme", "traduction", "cosmetique", "coiffure", "beauté", "autres", "sans activité"};
    String[] SAoptions = {"celibataire", "en couple", "marié", "separé", "divorcé", "veuf"};
    String[] MToptions = {"à pied", "transport public", "vélo", "moto", "voiture"};
    String G, S, SA, MT;
    Button btn_enregistrer;
    EditText nom_edit_text;
    EditText prenom_edit_text;
    EditText age_edit_text;
    RadioGroup sexeradioGroup;
    Button completer_votre_profil;
    int p;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit__profil);
        nom_edit_text = findViewById(R.id.nom_input);
        prenom_edit_text = findViewById(R.id.nom_input);
        age_edit_text = findViewById(R.id.age_input);
        G_spinner = findViewById(R.id.governorate_spinner);
        S_spinner = findViewById(R.id.Post_spinner);
        SA_spinner = findViewById(R.id.Situation_Amoureuse_spinner);
        MT_spinner = findViewById(R.id.moyenne_de_transport_spinner);
        sexeradioGroup = findViewById(R.id.sexe_radio_groupe);
        btn_enregistrer = findViewById(R.id.Enregistrer);
        completer_votre_profil = findViewById(R.id.Completer_botre_profil);
        //sexe radiobtn put value in varible
        sexeradioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.Female_btn) {
                    sexe = "femme";
                } else if (checkedId == R.id.Male_btn) {
                    sexe = "homme";
                }
            }
        });
        //settign adapter for spinners
        ArrayAdapter Garrayadapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, Goptions);
        ArrayAdapter Sarrayadapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, Soptions);
        ArrayAdapter SAarrayadapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, SAoptions);
        ArrayAdapter MTarrayadapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, MToptions);
        Garrayadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Sarrayadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        SAarrayadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        MTarrayadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        G_spinner.setAdapter(Garrayadapter);
        S_spinner.setAdapter(Sarrayadapter);
        SA_spinner.setAdapter(SAarrayadapter);
        MT_spinner.setAdapter(MTarrayadapter);

        //spinners put value in variables
        G_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                G = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        S_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                S = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        SA_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                SA = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        MT_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                MT = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        //button enregistrer
        btn_enregistrer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //get user database
                String user_id = FirebaseAuth.getInstance().getCurrentUser().getUid();
                final DatabaseReference current_user_db = FirebaseDatabase.getInstance().getReference().child("Users").child(user_id);
                final DatabaseReference current_user_profil_db = current_user_db.child("profil");
                final DatabaseReference current_user_points_db = current_user_db.child("points");
                //putting values in variables
                nom = nom_edit_text.getText().toString();
                prenom = prenom_edit_text.getText().toString();
                age = age_edit_text.getText().toString();
                if (nom.isEmpty() || prenom.isEmpty() || age.isEmpty() || sexe.isEmpty()) {
                    Toast.makeText(Edit_Profil.this, "veuillez remplir tous les champs", Toast.LENGTH_LONG);
                } else {
                    //sending the value to database
                    Map userMap = new HashMap();
                    userMap.put("nom", nom);
                    userMap.put("prenom", prenom);
                    userMap.put("sexe", sexe);
                    userMap.put("age", age);
                    userMap.put("gouvernorat", G);
                    userMap.put("Secteur", S);
                    userMap.put("S A", SA);
                    userMap.put("M T", MT);
                    current_user_profil_db.setValue(userMap);
                    //check profil completed or not
                    current_user_db.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (!dataSnapshot.child("profilCompleted").exists()) {
                                current_user_db.child("profilCompleted").setValue("1");
                                //add 2000 points if profil not complete
                                p=Integer.parseInt(dataSnapshot.child("points").getValue().toString())+2000;
                                current_user_points_db.setValue(p);
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                        }
                    });
                    //Move To main
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }
}

