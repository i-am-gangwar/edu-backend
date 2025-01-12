package com.edubackend.controller;

import com.edubackend.model.quizattempts.QuizAttempts;
import com.edubackend.model.quizattempts.QuizSetAttempt;
import com.edubackend.model.quizresults.QuizResults;
import com.edubackend.model.quizresults.QuizSetAttemptResult;
import com.edubackend.model.quizresults.QuizSetResult;
import com.edubackend.repository.QuizAttemptResultRepository;
import com.edubackend.repository.QuizAttemptsRepository;
import com.edubackend.service.QuizAttemptResultServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/quizAttemptResult")
public class QuizAttemptResultController {

    private final QuizAttemptResultServiceImpl quizAttemptResultService;
    private final  QuizAttemptsRepository quizAttemptsRepository;



    @Autowired
    public QuizAttemptResultController(QuizAttemptResultServiceImpl quizAttemptResultService,
                                       QuizAttemptsRepository quizAttemptsRepository) {
        this.quizAttemptResultService = quizAttemptResultService;
        this.quizAttemptsRepository =quizAttemptsRepository;

    }

    @PostMapping("/{userId}/{quizSetId}/{quizSetAttemptId}")
    public ResponseEntity<?> saveQuizAttemptResult(
            @PathVariable String userId,
            @PathVariable String quizSetId,
            @PathVariable String quizSetAttemptId) {
       try{
             QuizResults result =  quizAttemptResultService.saveQuizAttemptResult(userId,quizSetId,quizSetAttemptId);
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
    public ResponseEntity<?> getResultByUserId(@PathVariable String userId){
        try {
            QuizResults quizResults = quizAttemptResultService.getResultByUserId(userId);

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
            QuizSetResult results = quizAttemptResultService.getResultByUserIdAndQuizSetId(userId,quizSetId);
            if (results!=null && !results.getQuizSetAttemptResults().isEmpty()) {
                results.setQuizSetId(quizSetId);
                return ResponseEntity.ok(results);
            }
            else
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("User quizSet is not there in database userId: or SetAttempts are empty"+ userId+ ", quizSet:"+quizSetId);

        }
        catch (DataAccessResourceFailureException e){
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                    .body("Collection not found or database unavailable. Please initialize the collection.");
        }
        catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unable to get details, try again."+e);
        }
    }


    @GetMapping("/{userId}/{quizSetId}/{quizSetAttemptId}")
    public ResponseEntity<?> getQuizSetAttemptResult(
            @PathVariable String userId
            ,@PathVariable String quizSetId,
            @PathVariable String quizSetAttemptId) {

        try {
            QuizSetAttemptResult setAttemptResult =
                    quizAttemptResultService.getResultByUserIdAndQuizSetIdAndSetAttemptId(userId,quizSetId,quizSetAttemptId);

            if (setAttemptResult!=null) {
                return ResponseEntity.ok(setAttemptResult);
            }
            else
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("User quizSet is not there in database userId: or SetAttempts are empty"+ userId+ ", quizSet:"+quizSetId);

        }
        catch (DataAccessResourceFailureException e) {
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                    .body("Collection not found or database unavailable. Please initialize the collection.");
        }
        catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unable to get details, try again."+e);
        }
    }




}