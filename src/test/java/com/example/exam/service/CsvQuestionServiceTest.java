package com.example.exam.service;

import com.example.exam.io.QuestionReader;
import com.example.exam.model.Question;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class CsvQuestionServiceTest {

    private CsvQuestionService service;

    private ByteArrayOutputStream outContent;

    @BeforeEach
    void setUp() {
        QuestionReader reader = new QuestionReader(new ClassPathResource("questions.csv"));
        outContent = new ByteArrayOutputStream();
        service = new CsvQuestionService(reader);
    }

    @Test
    void testGetAllQuestions() {
        List<Question> questions = service.getAllQuestions();
        assertEquals(5, questions.size());
        assertFalse(questions.get(0).isFreeAnswer());
        assertTrue(questions.get(4).isFreeAnswer());
    }

    @Test
    void testQuestionContentIsCorrect() {
        List<Question> questions = service.getAllQuestions();

        Question q0 = questions.get(0);
        assertEquals("What is Java?", q0.getText());
        assertEquals(List.of("A programming language", "A coffee brand", "A car brand"), q0.getOptions());

        Question q4 = questions.get(4);
        assertEquals("Free answer: What is your name?", q4.getText());
        assertEquals(List.of("FREE"), q4.getOptions());
        assertTrue(q4.isFreeAnswer());
    }

    @Test
    void testPrintQuestionsOutput() {
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outContent));

        try {
            service.printQuestions();
            String output = outContent.toString();

            assertTrue(output.contains("Q: What is Java?"));
            assertTrue(output.contains("1. A programming language"));
            assertTrue(output.contains("→ Please type your answer."));
            assertTrue(output.contains("Free answer: What is your name?"));
        } finally {
            System.setOut(originalOut);
        }
    }

    @Test
    void testInvalidResourceThrowsException() {
        QuestionReader reader = new QuestionReader(new ClassPathResource("nonexistent.csv"));
        CsvQuestionService badService = new CsvQuestionService(reader);

        RuntimeException exception = assertThrows(RuntimeException.class, badService::getAllQuestions);
        assertTrue(exception.getMessage().contains("Failed to read questions"));
        assertNotNull(exception.getCause());
    }
}