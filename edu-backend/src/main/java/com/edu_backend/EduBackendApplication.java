package com.edu_backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableMongoRepositories(basePackages = "com.edu_backend.repository")
public class EduBackendApplication {

	public static void main(String[] args) {

		SpringApplication.run(EduBackendApplication.class, args);
		System.out.println("Application Started");
	}

}
