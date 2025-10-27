package com.example.exam.service;

import com.example.exam.io.QuestionReader;
import com.example.exam.model.Question;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(properties = "spring.shell.enabled=false")
public class CsvQuestionServiceTest {

    private CsvQuestionService service;

    @BeforeEach
    void setUp() {
        QuestionReader reader = new QuestionReader("questions.csv");
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
    void testInvalidResourceThrowsException() {
        QuestionReader reader = new QuestionReader("nonexistent.csv");
        CsvQuestionService badService = new CsvQuestionService(reader);

        RuntimeException exception = assertThrows(RuntimeException.class, badService::getAllQuestions);
        assertTrue(exception.getMessage().contains("Failed to read questions"));
        assertNotNull(exception.getCause());
    }
}