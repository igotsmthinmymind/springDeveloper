package com.example.exam.service;

import com.example.exam.model.Question;
import java.util.List;

public interface QuestionService {

    List<Question> getAllQuestions();

    void printQuestions();
}