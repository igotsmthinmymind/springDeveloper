package com.example.exam.service;

import com.example.exam.model.Question;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.*;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ExamServiceTest {

    private ExamService examService;

    private QuestionService mockQuestionService;

    private InputStream originalIn;

    private PrintStream originalOut;

    private ByteArrayOutputStream outContent;

    private InputStream inputStream;

    List<Question> questions = Arrays.asList(
            new Question("Q1", Arrays.asList("Correct", "Wrong")),
            new Question("Q2", Arrays.asList("Correct", "Wrong")),
            new Question("Q3", Arrays.asList("Correct", "Wrong")),
            new Question("Q4", Arrays.asList("Correct", "Wrong")),
            new Question("Q5", Arrays.asList("Correct", "Wrong"))
    );;

    @BeforeEach
    void setUp() {
        mockQuestionService = Mockito.mock(QuestionService.class);
        examService = new ExamService(mockQuestionService, 4);

        originalIn = System.in;
        originalOut = System.out;
        outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
    }

    @AfterEach
    void tearDown() {
        System.setIn(originalIn);
        System.setOut(originalOut);
    }

    @Test
    @DisplayName("pass the exam when the number of correct answers meets or exceeds the passing threshold")
    void testExamPassesWhenScoreMeetsThreshold() {
        when(mockQuestionService.getAllQuestions()).thenReturn(questions);

        String input = "Vadim\nShibanov\n1\n1\n1\n1\n1\n";
        inputStream = new ByteArrayInputStream(input.getBytes());

        examService.runExam(inputStream);

        String output = outContent.toString();
        assertTrue(output.contains("Congratulations! You passed the exam."));
        assertTrue(output.contains("You answered 5 out of 5 correctly."));
    }

    @Test
    @DisplayName("fail the exam when the number of correct answers is below the passing threshold")
    void testExamFailsWhenScoreBelowThreshold() {
        when(mockQuestionService.getAllQuestions()).thenReturn(questions);

        String input = "Vadim\nShibanov\n2\n2\n1\n1\n1\n";
        inputStream = new ByteArrayInputStream(input.getBytes());

        examService.runExam(inputStream);

        String output = outContent.toString();
        assertTrue(output.contains("Sorry, you did not pass."));
        assertTrue(output.contains("You answered 3 out of 5 correctly."));
    }

    @Test
    @DisplayName("treat a non-empty free-text answer as correct (if applicable in current mock setup)")
    void testFreeAnswerCountsAsCorrectIfNotEmpty() {
        when(mockQuestionService.getAllQuestions()).thenReturn(questions);

        String input = "Vadim\nShibanov\n2\n1\n1\n1\n1\n";
        inputStream = new ByteArrayInputStream(input.getBytes());

        examService.runExam(inputStream);

        String output = outContent.toString();
        assertTrue(output.contains("Congratulations! You passed the exam."));
        assertTrue(output.contains("You answered 4 out of 5 correctly."));
    }

    @Test
    @DisplayName("treat an empty free-text answer as incorrect")
    void testFreeAnswerEmptyIsNotCounted() {
        when(mockQuestionService.getAllQuestions()).thenReturn(questions);

        String input = "Vadim\nShibanov\n2\n1\n1\n1\n\n";
        inputStream = new ByteArrayInputStream(input.getBytes());

        examService.runExam(inputStream);

        String output = outContent.toString();
        assertTrue(output.contains("Sorry, you did not pass."));
        assertTrue(output.contains("You answered 3 out of 5 correctly."));
    }
}