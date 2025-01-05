package com.edu_backend.controller;

import com.edu_backend.service.QuizAttemptResultServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/quizAttemptResult")
public class QuizAttemptResultController {

    @Autowired
    QuizAttemptResultServiceImpl quizAttemptResultService;

    @PostMapping("/{userId}/{quizSetId}/{quizSetAttemptId}")
    public ResponseEntity<?> saveQuizAttemptResult(@PathVariable String userId, @PathVariable String quizSetId, @PathVariable String quizSetAttemptId) {
      boolean result =  quizAttemptResultService.calculateQuizAttemptResult(userId,quizSetId,quizSetAttemptId);
      if (result){
          return  new ResponseEntity<>("Result Saved",HttpStatus.OK);
      }
      else{
          return  new ResponseEntity<>("Result not Saved",HttpStatus.NOT_FOUND);
      }

      }
}