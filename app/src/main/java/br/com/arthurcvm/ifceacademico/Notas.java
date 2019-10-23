package br.com.arthurcvm.ifceacademico;

import java.util.ArrayList;

public class Notas {
    private ArrayList<Nota> N1 = new ArrayList<>();
    private ArrayList<Nota> N2 = new ArrayList<>();
    private ArrayList<Nota> Final = new ArrayList<>();

    public Notas() {
    }

    public Notas(ArrayList<Nota> n1, ArrayList<Nota> n2, ArrayList<Nota> aFinal) {
        N1 = n1;
        N2 = n2;
        Final = aFinal;
    }

    public ArrayList<Nota> getN1() {
        return N1;
    }

    public void setN1(ArrayList<Nota> n1) {
        N1 = n1;
    }

    public ArrayList<Nota> getN2() {
        return N2;
    }

    public void setN2(ArrayList<Nota> n2) {
        N2 = n2;
    }

    public ArrayList<Nota> getFinal() {
        return Final;
    }

    public void setFinal(ArrayList<Nota> aFinal) {
        Final = aFinal;
    }
}
