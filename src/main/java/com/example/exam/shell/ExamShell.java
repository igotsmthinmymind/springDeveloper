package com.example.exam.shell;

import com.example.exam.service.ExamService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;

@ShellComponent
public class ExamShell {

    private static final Logger log = LoggerFactory.getLogger(ExamShell.class);

    @Autowired
    private final ExamService examService;

    public ExamShell(ExamService examService) {
        log.info("ExamShell component initialized!");
        this.examService = examService;
    }

    @ShellMethod(key = "start-exam", value = "Start the student exam")
    public void startExam(
            @ShellOption(defaultValue = "John") String firstName,
            @ShellOption(defaultValue = "Doe") String lastName) {

        examService.runExam(new ByteArrayInputStream((firstName + "\n" + lastName + "\n" + "1\n".repeat(5)).getBytes(StandardCharsets.UTF_8)));
    }
}