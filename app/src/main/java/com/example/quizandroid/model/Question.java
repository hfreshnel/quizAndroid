package com.example.quizandroid.model;

public class Question {
    private long id;
    private String libelle;

    public Question(long id, String libelle) {
        this.id = id;
        this.libelle = libelle;
    }

    public Question() {}

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    @Override
    public String toString() {
        return "Question{" +
                "id=" + id +
                ", libelle='" + libelle + '\'' +
                '}';
    }
}
