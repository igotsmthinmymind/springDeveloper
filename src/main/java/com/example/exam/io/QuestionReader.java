package com.example.exam.io;

import com.example.exam.model.Question;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class QuestionReader {

    private final Resource resource;

    public QuestionReader(Resource resource) {
        this.resource = resource;
    }

    public List<Question> readQuestions() throws IOException {
        List<Question> questions = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",", -1);
                String questionText = parts[0];
                List<String> options = new ArrayList<>(Arrays.asList(parts).subList(1, parts.length));
                questions.add(new Question(questionText, options));
            }
        }
        return questions;
    }
}