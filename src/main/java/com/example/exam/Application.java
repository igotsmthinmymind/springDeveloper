package com.example.exam;

import com.example.exam.config.AppConfig;
import com.example.exam.service.ExamService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Application {
    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        ExamService examService = context.getBean(ExamService.class);
        examService.runExam(System.in);
    }
}