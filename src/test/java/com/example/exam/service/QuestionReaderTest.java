package com.example.exam.service;

import com.example.exam.io.QuestionReader;
import com.example.exam.model.Question;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.InputStreamResource;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class QuestionReaderTest {

    @Test
    @DisplayName("read a single question with multiple options correctly")
    void readsSingleQuestionCorrectly() throws IOException {
        String csvData = "What is Java?,Programming language,Operating system,Database";
        InputStreamResource resource = new InputStreamResource(
                new ByteArrayInputStream(csvData.getBytes(StandardCharsets.UTF_8))
        );

        QuestionReader reader = new QuestionReader(resource);
        List<Question> questions = reader.readQuestions();

        assertEquals(1, questions.size());
        Question q = questions.get(0);
        assertEquals("What is Java?", q.getText());
        assertEquals(List.of("Programming language", "Operating system", "Database"), q.getOptions());
    }

    @Test
    @DisplayName("read multiple questions from multiple lines")
    void readsMultipleQuestions() throws IOException {
        String csvData = String.join("\n",
                "Q1?,Option A,Option B",
                "Q2?,Option X,Option Y,Option Z"
        );
        InputStreamResource resource = new InputStreamResource(
                new ByteArrayInputStream(csvData.getBytes(StandardCharsets.UTF_8))
        );

        QuestionReader reader = new QuestionReader(resource);
        List<Question> questions = reader.readQuestions();

        assertEquals(2, questions.size());

        Question q1 = questions.get(0);
        assertEquals("Q1?", q1.getText());
        assertEquals(List.of("Option A", "Option B"), q1.getOptions());

        Question q2 = questions.get(1);
        assertEquals("Q2?", q2.getText());
        assertEquals(List.of("Option X", "Option Y", "Option Z"), q2.getOptions());
    }

    @Test
    @DisplayName("handle empty lines gracefully")
    void skipsEmptyLines() throws IOException {
        String csvData = String.join("\n",
                "Q1?,A,B",
                "",
                "Q2?,X,Y"
        );
        InputStreamResource resource = new InputStreamResource(
                new ByteArrayInputStream(csvData.getBytes(StandardCharsets.UTF_8))
        );

        QuestionReader reader = new QuestionReader(resource);
        List<Question> questions = reader.readQuestions();

        assertEquals(3, questions.size());
        assertEquals("Q1?", questions.get(0).getText());
        assertEquals("Q2?", questions.get(2).getText());
    }

    @Test
    @DisplayName("treat a line with only question text as having empty options list")
    void handlesQuestionWithoutOptions() throws IOException {
        String csvData = "Free question?";
        InputStreamResource resource = new InputStreamResource(
                new ByteArrayInputStream(csvData.getBytes(StandardCharsets.UTF_8))
        );

        QuestionReader reader = new QuestionReader(resource);
        List<Question> questions = reader.readQuestions();

        assertEquals(1, questions.size());
        Question q = questions.get(0);
        assertEquals("Free question?", q.getText());
        assertTrue(q.getOptions().isEmpty());
    }

    @Test
    @DisplayName("preserve empty fields (e.g. trailing commas) as empty strings in options")
    void preservesEmptyFields() throws IOException {
        String csvData = "Q?,A,,B";
        InputStreamResource resource = new InputStreamResource(
                new ByteArrayInputStream(csvData.getBytes(StandardCharsets.UTF_8))
        );

        QuestionReader reader = new QuestionReader(resource);
        List<Question> questions = reader.readQuestions();

        assertEquals(1, questions.size());
        assertEquals(List.of("A", "", "B"), questions.get(0).getOptions());
    }
}