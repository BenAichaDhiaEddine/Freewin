package com.example.freewin.AssosiationRecycleView;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.freewin.R;

public class AssosiationViewHolder extends RecyclerView.ViewHolder {
    public TextView nom;
    public TextView description;
    public Button contribuer;

    public AssosiationViewHolder(@NonNull View itemView) {
        super(itemView);
        nom=itemView.findViewById(R.id.item_nom);
        description=itemView.findViewById(R.id.item_description);
        contribuer=itemView.findViewById(R.id.item_contribuer_btn);
    }
}
