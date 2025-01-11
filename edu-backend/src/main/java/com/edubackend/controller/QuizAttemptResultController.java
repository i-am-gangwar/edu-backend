package com.edubackend.controller;

import com.edubackend.model.quizattempts.QuizSetAttempt;
import com.edubackend.model.quizresults.QuizResults;
import com.edubackend.model.quizresults.QuizSetAttemptResult;
import com.edubackend.model.quizresults.QuizSetResult;
import com.edubackend.mongo.MongoService;
import com.edubackend.repository.QuizAttemptResultRepository;
import com.edubackend.service.QuizAttemptResultServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/quizAttemptResult")
public class QuizAttemptResultController {


   private final QuizAttemptResultServiceImpl quizAttemptResultService;
    private final MongoService mongoService;
    private final QuizAttemptResultRepository quizAttemptResultRepository;

    @Autowired
    public QuizAttemptResultController(QuizAttemptResultServiceImpl quizAttemptResultService,
                                       MongoService mongoService,
                                       QuizAttemptResultRepository quizAttemptResultRepository) {
        this.quizAttemptResultService = quizAttemptResultService;
        this.mongoService = mongoService;
        this.quizAttemptResultRepository = quizAttemptResultRepository;
    }

    @PostMapping("/{userId}/{quizSetId}/{quizSetAttemptId}")
    public ResponseEntity<?> saveQuizAttemptResult(
            @PathVariable String userId,
            @PathVariable String quizSetId,
            @PathVariable String quizSetAttemptId) {

       try{
           QuizSetAttempt quizAttempt = mongoService.findQuizSetAttempt(userId, quizSetId, quizSetAttemptId);
           QuizSetAttemptResult quizResultCalculated = quizAttemptResultService.calculateResultOfQuizAttempt(quizSetAttemptId, quizAttempt); // calculating result of attempted quiz
           QuizResults result =  quizAttemptResultService.saveQuizAttemptResult(userId,quizSetId,quizResultCalculated);
           if (result!=null)
               return  ResponseEntity.status(HttpStatus.CREATED).body("Quiz Attempt Result saved");
           else
               return  ResponseEntity.status(HttpStatus.NOT_FOUND).body("Quiz Attempt Result not saved\n"+"UserId: "+userId+ "\nQuizSetId: "+quizSetId+"\nQuizAttemptId: "+quizSetAttemptId);
       }
       catch (Exception e){
           return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unable to get details, try again.");
       }
    }


    // get result by user id
    @GetMapping("/{userId}")
    public ResponseEntity<?> getQuizAttemptsResult(@PathVariable String userId){
        try {
            QuizResults quizResults = quizAttemptResultRepository.findByUserId(userId);

            if (quizResults != null)
                return ResponseEntity.ok()
                        .header("Content-Type", "application/json").
                        body(quizResults);
            else
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .header("Content-Type", "application/json")
                        .body(String.format("User ID %s is not found in the database.", userId));

        }
        catch (Exception e){
            return ResponseEntity.status(HttpStatus.FOUND)
                    .body("Not found! "+e);
        }
    }


    @GetMapping("/{userId}/{quizSetId}")
    public ResponseEntity<?> getQuizSetsResult(@PathVariable String userId,@PathVariable String quizSetId){
        try {
            QuizSetResult quizSetResults = mongoService.findQuizSetResults(userId,quizSetId);
            if (quizSetResults!=null)
                return ResponseEntity.ok(quizSetResults);
            else
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("User quizSet is not there in database userId: "+ userId+ ", quizSet:"+quizSetId);
        }
        catch (Exception e){
            return ResponseEntity.status(HttpStatus.FOUND)
                    .body("Not found! "+e);
        }
    }


    @GetMapping("/{userId}/{quizSetId}/{attemptId}")
    public ResponseEntity<?> getQuizSetAttemptResult(@PathVariable String userId,@PathVariable String quizSetId,
                                                 @PathVariable String quizSetAttemptId) {
        try {
            QuizSetAttemptResult quizSetAttemptResult = mongoService.findQuizSetAttemptResult(userId, quizSetId, quizSetAttemptId);
            if (quizSetAttemptResult != null)
                return ResponseEntity.ok(quizSetAttemptResult);
            else
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("User quizSetAttemptResult is not there in database userId: " + userId + ", quizSet:" + quizSetId
                                + ", quizSetAttemptId:" + quizSetAttemptId);


        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.FOUND)
                    .body("Not found! " + e);
        }
    }

    // get result by user id and quiz set id
    // get result by user id and quiz set id and quiz attempt id

    // delete by user id
    // delete by by user id and quiz set id and quiz attempt id




}