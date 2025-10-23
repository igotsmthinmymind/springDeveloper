package com.example.exam.service;

import com.example.exam.model.Question;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.*;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

public class ExamServiceTest {

    private ExamService examService;

    private QuestionService mockQuestionService;

    private InputStream originalIn;

    private PrintStream originalOut;

    private ByteArrayOutputStream outContent;

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
    void testExamPassesWhenScoreMeetsThreshold() {
        List<Question> questions = Arrays.asList(
                new Question("Q1", Arrays.asList("Correct", "Wrong")),
                new Question("Q2", Arrays.asList("Correct", "Wrong")),
                new Question("Q3", Arrays.asList("Correct", "Wrong")),
                new Question("Q4", Arrays.asList("Correct", "Wrong")),
                new Question("Q5", Arrays.asList("Correct", "Wrong"))
        );
        when(mockQuestionService.getAllQuestions()).thenReturn(questions);
        String input = "Vadim\nShibanov\n1\n1\n1\n1\n1\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        examService.runExam();

        String output = outContent.toString();
        assertTrue(output.contains("Congratulations! You passed the exam."));
        assertTrue(output.contains("You answered 5 out of 5 correctly."));
    }

    @Test
    void testExamFailsWhenScoreBelowThreshold() {
        List<Question> questions = Arrays.asList(
                new Question("Q1", Arrays.asList("Correct", "Wrong")),
                new Question("Q2", Arrays.asList("Correct", "Wrong")),
                new Question("Q3", Arrays.asList("Correct", "Wrong"))
        );
        when(mockQuestionService.getAllQuestions()).thenReturn(questions);

        String input = "Test\nUser\n1\n2\n1\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        examService.runExam();

        String output = outContent.toString();
        assertTrue(output.contains("Sorry, you did not pass."));
        assertTrue(output.contains("You answered 2 out of 3 correctly."));
    }

    @Test
    void testFreeAnswerCountsAsCorrectIfNotEmpty() {
        List<Question> questions = List.of(
                new Question("Name?", List.of("FREE"))
        );
        when(mockQuestionService.getAllQuestions()).thenReturn(questions);

        String input = "Vadim\nShibanov\nMyName\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        examService.runExam();

        String output = outContent.toString();
        assertTrue(output.contains("You answered 1 out of 1 correctly."));
    }

    @Test
    void testFreeAnswerEmptyIsNotCounted() {
        List<Question> questions = List.of(
                new Question("Name?", List.of("FREE"))
        );
        when(mockQuestionService.getAllQuestions()).thenReturn(questions);

        String input = "Vadim\nShibanov\n\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        examService.runExam();

        String output = outContent.toString();
        assertTrue(output.contains("You answered 0 out of 1 correctly."));
    }
}