package com.example.freewin.MatchRecycleView;

import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.freewin.R;


public class MatchViewHolder extends RecyclerView.ViewHolder {
    public RadioButton equipe1;
    public RadioButton equipe2;
    public RadioButton x;
    public Button ParierMatch;
    public TextView cout;
    public RadioGroup radioGroup;


    public MatchViewHolder(@NonNull View itemView) {
        super(itemView);
        equipe1=itemView.findViewById(R.id.equipe1);
        equipe2=itemView.findViewById(R.id.equipe2);
        x=itemView.findViewById(R.id.matchX);
        ParierMatch=itemView.findViewById(R.id.parierMatch);
        cout=itemView.findViewById(R.id.coutTextViewMatch);
        radioGroup=itemView.findViewById(R.id.matchRadioGroup);
    }
}
