package com.example.quizandroid.model;

import java.sql.Timestamp;

public class Quiz {
    private Long id;
    private String libelle;
    private int etat;
    private Timestamp dateDebutQuiz;
    private int noQuestionCourante;
    private int etape;
    private Timestamp dateDebutQuestion;

    public Quiz(Long id, String libelle, int etat, Timestamp dateDebutQuiz, int noQuestionCourante, int etape, Timestamp dateDebutQuestion) {
        this.id = id;
        this.libelle = libelle;
        this.etat = etat;
        this.dateDebutQuiz = dateDebutQuiz;
        this.noQuestionCourante = noQuestionCourante;
        this.etape = etape;
        this.dateDebutQuestion = dateDebutQuestion;
    }

    public Quiz(String libelle,int noQuestionCourante) {
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

    public Timestamp getDateDebutQuiz() {
        return dateDebutQuiz;
    }

    public void setDateDebutQuiz(Timestamp dateDebutQuiz) {
        this.dateDebutQuiz = dateDebutQuiz;
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

    public Timestamp getDateDebutQuestion() {
        return dateDebutQuestion;
    }

    public void setDateDebutQuestion(Timestamp dateDebutQuestion) {
        this.dateDebutQuestion = dateDebutQuestion;
    }

    // toString() method for debugging and logging
    @Override
    public String toString() {
        return "Quiz{" +
                "id=" + id +
                ", libelle='" + libelle + '\'' +
                ", etat=" + etat +
                ", dateDebutQuiz=" + dateDebutQuiz +
                ", noQuestionCourante=" + noQuestionCourante +
                ", etape=" + etape +
                ", dateDebutQuestion=" + dateDebutQuestion +
                '}';
    }

}

