package com.example.quizandroid.model;

public class Proposition {
    private Long id;
    private String libelle;
    private int correct;

    // Constructors
    public Proposition() {}

    public Proposition(Long id, String libelle, int correct) {
        this.id = id;
        this.libelle = libelle;
        this.correct = correct;
    }

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public int getCorrect() {
        return correct;
    }

    public void setCorrect(int correct) {
        this.correct = correct;
    }
}
