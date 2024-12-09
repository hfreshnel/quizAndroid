package com.example.quizandroid.model;
public class Quiz {
    private String title;
    private String description;

    public Quiz(String title, String description) {
        this.title = title;
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }
}

