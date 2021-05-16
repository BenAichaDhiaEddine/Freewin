package com.example.freewin.MatchRecycleView;

public class Match {
    public  int cout;
    public  String equipe1;
    public String equipe2;
    public int tempsFin;
    public int num;

    public Match() {
    }

    public Match(int cout, String equipe1, String equipe2,int num, int tempsFin) {
        this.cout = cout;
        this.equipe1 = equipe1;
        this.equipe2 = equipe2;
        this.num=num;
        this.tempsFin = tempsFin;
    }
    public int getCout() {
        return cout;
    }
    public void setCout(int cout) {
        this.cout = cout;
    }

    public String getEquipe1() {
        return equipe1;
    }

    public void setEquipe1(String equipe1) {
        this.equipe1 = equipe1;
    }

    public String getEquipe2() {
        return equipe2;
    }

    public void setEquipe2(String equipe2) {
        this.equipe2 = equipe2;
    }

    public int getTempsFin() {
        return tempsFin;
    }

    public void setTempsFin(int tempsFin) {
        this.tempsFin = tempsFin;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }


}
