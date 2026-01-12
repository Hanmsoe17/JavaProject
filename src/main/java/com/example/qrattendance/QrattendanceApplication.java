package com.example.qrattendance;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories("com.example.qrattendance.repository")
public class QrattendanceApplication {
    public static void main(String[] args) {
        SpringApplication.run(QrattendanceApplication.class, args);
    }
}
