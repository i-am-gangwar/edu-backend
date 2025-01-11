package com.edubackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableMongoRepositories(basePackages = "com.edubackend.repository")
@ComponentScan(basePackages = "com.edubackend")
public class EduBackendApplication {

	public static void main(String[] args) {

		SpringApplication.run(EduBackendApplication.class, args);
		System.out.println("Application Started");
	}

}
