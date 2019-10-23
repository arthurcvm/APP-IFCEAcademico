package br.com.arthurcvm.ifceacademico;

import java.io.Serializable;

public class DiarioObjeto implements Serializable {
    public String nome;
    public String professor;
    public Notas notas = new Notas();
    public int carga;
    public int faltas;
    public int aulas;

    public DiarioObjeto(String nome, String professor, Notas notas, int carga, int faltas, int aulas) {
        this.nome = nome;
        this.professor = professor;
        this.notas = notas;
        this.carga = carga;
        this.faltas = faltas;
        this.aulas = aulas;
    }

    public DiarioObjeto() {
    }

    public String getNome() {
        return nome;
    }

    public String getProfessor() {
        return professor;
    }

    public Notas getNotas() {
        return notas;
    }

    public int getCarga() {
        return carga;
    }

    public int getFaltas() {
        return faltas;
    }

    public int getAulas() {
        return aulas;
    }
}
