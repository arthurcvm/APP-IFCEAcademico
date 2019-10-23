package br.com.arthurcvm.ifceacademico;

import java.io.Serializable;

public class FileObject implements Serializable {
    public String materia;
    public String title;
    public String url;
    public String data;

    public FileObject(String materia, String title, String url, String data) {
        this.materia = materia;
        this.title = title;
        this.url = url;
        this.data = data;
    }

    public FileObject() {
    }

    public String getMateria() {
        return materia;
    }

    public String getTitle() {
        return title;
    }

    public String getUrl() {
        return url;
    }

    public String getData() {
        return data;
    }
}
