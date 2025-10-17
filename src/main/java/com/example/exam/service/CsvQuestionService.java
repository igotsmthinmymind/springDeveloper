package com.example.exam.service;

import com.example.exam.io.QuestionReader;
import com.example.exam.model.Question;

import java.util.List;

public class CsvQuestionService implements QuestionService {

    private final QuestionReader reader;

    public CsvQuestionService(QuestionReader reader) {
        this.reader = reader;
    }

    @Override
    public List<Question> getAllQuestions() {
        try {
            return reader.readQuestions();
        } catch (Exception e) {
            throw new RuntimeException("Failed to read questions", e);
        }
    }

    @Override
    public void printQuestions() {
        List<Question> questions = getAllQuestions();
        for (Question q : questions) {
            System.out.println("Q: " + q.getText());
            if (q.isFreeAnswer()) {
                System.out.println(" → Please type your answer.");
            } else {
                for (int i = 0; i < q.getOptions().size(); i++) {
                    System.out.println("   " + (i + 1) + ". " + q.getOptions().get(i));
                }
            }
            System.out.println();
        }
    }
}