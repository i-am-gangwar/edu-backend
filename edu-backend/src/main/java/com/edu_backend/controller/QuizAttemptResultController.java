package com.edu_backend.controller;

import com.edu_backend.model.QuizAttempt;
import com.edu_backend.repository.QuizAttemptRepository;
import com.edu_backend.service.QuizAttemptResultServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/quizAttempts")
public class QuizAttemptResultController {

}