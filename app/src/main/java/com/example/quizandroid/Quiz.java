package com.example.quizandroid;

public class Quiz {
    private String title;
    private int iconResId;

    public Quiz(String title, int iconResId) {
        this.title = title;
        this.iconResId = iconResId;
    }

    public String getTitle() {
        return title;
    }

    public int getIconResId() {
        return iconResId;
    }
}
