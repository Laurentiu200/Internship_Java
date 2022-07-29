package com.example.internship_java;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories("com.example.internship_java.repository")
public class InternshipJavaApplication {

	public static void main(String[] args) {
		SpringApplication.run(InternshipJavaApplication.class, args);
	}

}
