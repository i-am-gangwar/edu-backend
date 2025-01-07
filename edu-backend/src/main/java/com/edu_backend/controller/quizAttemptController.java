package com.edu_backend.controller;

import com.edu_backend.model.QuizAttempt;
import com.edu_backend.mongo.MongoService;
import com.edu_backend.repository.QuizAttemptRepository;
import com.edu_backend.service.quizAttemptServiceImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.util.Optional;

@RestController
@RequestMapping("/quizAttempt")
@Validated
@ControllerAdvice
public class quizAttemptController {

    @Autowired
    quizAttemptServiceImpl quizAttemptService;
    @Autowired
    QuizAttemptRepository quizAttemptRepository;

    @Autowired
    MongoService mongoService;


    @PostMapping("/{userId}/quizset/{quizSetId}/attempts")  // save user quiz attempt on submit
    public ResponseEntity<?> addQuizAttempt(
            @PathVariable String userId,
            @PathVariable String quizSetId,
            @Valid @RequestBody QuizAttempt.QuizSetAttempt newAttempt) {
        try {
            QuizAttempt updatedQuizAttempt = quizAttemptService.addQuizAttempt(userId, quizSetId, newAttempt);
            if (!(updatedQuizAttempt == null) && !updatedQuizAttempt.getQuizSet().isEmpty()) {
                return ResponseEntity.status(HttpStatus.CREATED).body(updatedQuizAttempt);
            } else
                ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(
                        String.format("User Quiz Attempt Not Saved\nuserId: %s\nquizSetId: %s\nAttempt Details: %s",
                                userId, quizSetId, newAttempt));
        }
        catch (DataAccessResourceFailureException e) { // Handle missing collection or database access issues
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body("Collection not found or database unavailable. Please initialize the collection.");
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unable to get details, try again.");
        }
        return null;
    }




        @GetMapping("/userId/{userId}")  // get all quiz attempt of user by user id
        public ResponseEntity<?> getAllResultUser(@PathVariable String userId) {
            try {
                Optional<QuizAttempt> result = quizAttemptRepository.findByUserId(userId);
                if (result.isPresent()) {
                    return ResponseEntity.ok(result.get());
                } else {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(String.format("User ID %s is not found in the database.", userId));
                }
            } catch (DataAccessResourceFailureException e) {
                return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body("Collection not found or database unavailable. Please initialize the collection.");
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while retrieving user results. Please try again.");
            }
        }


        // get all attempt of any quizSet by quizSetId and UserID
        @GetMapping("/userId/{userId}/quizSetId/{quizSetId}")
        public ResponseEntity<?> getResultsByUserIdAndQuizSetId(@PathVariable String userId, @PathVariable String quizSetId) {
          try {
              Optional<QuizAttempt.QuizSet> results =  mongoService.findQuizSet(userId, quizSetId);
              System.out.println("result:"+results);
              if (results.isEmpty())
                  return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User quizSet is not there in database userId: "+ userId+ ", quizSet:"+quizSetId);
               else
                   return ResponseEntity.ok(results.get());
          }
          catch (DataAccessResourceFailureException e) {
              return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body("Collection not found or database unavailable. Please initialize the collection.");
          }
          catch (Exception e){
              System.out.println("exception:"+ e);
              return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unable to get details, try again.");
          }
        }
}