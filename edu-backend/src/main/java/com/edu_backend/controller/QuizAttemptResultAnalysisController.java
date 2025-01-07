package com.edu_backend.controller;

import com.edu_backend.model.AllResultsAnalysis;
import com.edu_backend.model.QuizAttemptResult;
import com.edu_backend.repository.QuizAttemptResultAnalysisRepository;
import com.edu_backend.repository.QuizAttemptResultRepository;
import com.edu_backend.service.QuizAttemptResultAnalysisServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/analytics")
public class QuizAttemptResultAnalysisController {

     @Autowired
    QuizAttemptResultAnalysisServiceImpl quizAttemptResultAnalysisServiceImpl;

     @Autowired
    QuizAttemptResultRepository quizAttemptResultRepository;

     @PostMapping("/{userId}")
    public ResponseEntity<?> ResultAnalysis(@PathVariable String userId){
         QuizAttemptResult userResult = quizAttemptResultRepository.findByUserId(userId).orElse(null);
         System.out.println("userResult: "+userResult);
         if (userResult!=null){
             AllResultsAnalysis allResultsAnalysis = quizAttemptResultAnalysisServiceImpl.ResultAnalysis(userResult);
             if(allResultsAnalysis!=null)
                 return ResponseEntity.status(HttpStatus.CREATED).body(allResultsAnalysis);
             else
                 return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Unable to do analysis Pls try again!");
        }
        else
            ResponseEntity.status(HttpStatus.NOT_FOUND).body( "No UserID: %s found to calculate results analysis!" + userId);
         return null;
     }

}
