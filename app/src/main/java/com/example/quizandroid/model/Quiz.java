package com.example.quizandroid.model;

public class Quiz {
    private Long id;
    private String libelle;
    private int etat;
    private int noQuestionCourante;
    private int etape;

    public Quiz(Long id, String libelle, int etat, int noQuestionCourante, int etape) {
        this.id = id;
        this.libelle = libelle;
        this.etat = etat;
        this.noQuestionCourante = noQuestionCourante;
        this.etape = etape;
    }

    public Quiz(String libelle, int noQuestionCourante) {
        this.libelle = libelle;
        this.noQuestionCourante = noQuestionCourante;
    }

    // Getters and Setters
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

    public int getEtat() {
        return etat;
    }

    public void setEtat(int etat) {
        this.etat = etat;
    }

    public int getNoQuestionCourante() {
        return noQuestionCourante;
    }

    public void setNoQuestionCourante(int noQuestionCourante) {
        this.noQuestionCourante = noQuestionCourante;
    }

    public int getEtape() {
        return etape;
    }

    public void setEtape(int etape) {
        this.etape = etape;
    }

    // toString() method for debugging and logging
    @Override
    public String toString() {
        return "Quiz{" +
                "id=" + id +
                ", libelle='" + libelle + '\'' +
                ", etat=" + etat +
                ", noQuestionCourante=" + noQuestionCourante +
                ", etape=" + etape +
                '}';
    }
}