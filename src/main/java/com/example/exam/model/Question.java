package com.example.exam.model;

import java.util.List;

public class Question {

    private final String text;

    private final List<String> options;

    private final boolean isFreeAnswer;

    public Question(String text, List<String> options) {
        this.text = text;
        this.options = options;
        this.isFreeAnswer = options.size() == 1 && "FREE".equalsIgnoreCase(options.get(0));
    }

    public String getText() {
        return text;
    }

    public List<String> getOptions() {
        return options;
    }

    public boolean isFreeAnswer() {
        return isFreeAnswer;
    }
}