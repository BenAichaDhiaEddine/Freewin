package com.example.freewin.AssosiationRecycleView;
public class Assosiation {

    String nom;
    String description;
    String argent;

    public Assosiation() {
    }

    public Assosiation(String nom,String description, String argent) {
        this.nom = nom;
        this.description=description;
    }
    public String getNom() {
        return nom;
    }
    public String getDescription() {
        return description;
    }
}
