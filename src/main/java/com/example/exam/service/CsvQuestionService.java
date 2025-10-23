package com.example.exam.service;

import com.example.exam.io.QuestionReader;
import com.example.exam.model.Question;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
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
}