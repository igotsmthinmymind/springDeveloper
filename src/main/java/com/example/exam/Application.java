package com.example.exam;

import com.example.exam.service.QuestionService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Application {
    public static void main(String[] args) {

        ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
        QuestionService service = context.getBean(QuestionService.class);
        service.printQuestions();
    }
}