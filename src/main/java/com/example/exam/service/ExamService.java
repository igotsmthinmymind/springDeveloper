package com.example.exam.service;

import com.example.exam.model.Question;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Scanner;

@Service
public class ExamService {

    @Autowired
    private final QuestionService questionService;

    private final int passingScore;

    private final Scanner scanner = new Scanner(System.in);


    public ExamService(QuestionService questionService,
                       @Value("${exam.passing.score}") int passingScore) {
        this.questionService = questionService;
        this.passingScore = passingScore;
    }

    public void runExam() {
        System.out.println("Welcome to the Student Exam!");
        System.out.print("Please enter your first name: ");
        String firstName = scanner.nextLine().trim();
        System.out.print("Please enter your last name: ");
        String lastName = scanner.nextLine().trim();

        System.out.println("Hello, " + firstName + " " + lastName + "! Let's begin the exam.\n");

        List<Question> allQuestions = questionService.getAllQuestions();
        int totalQuestions = Math.min(5, allQuestions.size());
        int correctCount = 0;

        for (int i = 0; i < totalQuestions; i++) {
            Question q = allQuestions.get(i);
            System.out.println("Q" + (i + 1) + ": " + q.getText());

            if (q.isFreeAnswer()) {
                System.out.print("Your answer: ");
                String userAnswer = scanner.nextLine().trim();
                if (!userAnswer.isEmpty()) {
                    correctCount++;
                }
            } else {
                List<String> options = q.getOptions();
                for (int j = 0; j < options.size(); j++) {
                    System.out.println("  " + (j + 1) + ". " + options.get(j));
                }
                System.out.print("Choose option (1-" + options.size() + "): ");
                String input = scanner.nextLine().trim();
                try {
                    int choice = Integer.parseInt(input);
                    if (choice == 1) {
                        correctCount++;
                    }
                } catch (NumberFormatException ignored) {
                }
            }
            System.out.println();
        }

        System.out.println("Exam completed!");
        System.out.println("You answered " + correctCount + " out of " + totalQuestions + " correctly.");

        if (correctCount >= passingScore) {
            System.out.println("Congratulations! You passed the exam.");
        } else {
            System.out.println("Sorry, you did not pass. Better luck next time.");
        }
    }
}