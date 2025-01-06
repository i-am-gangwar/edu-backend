package com.edu_backend.controller;

import com.edu_backend.model.QuizAttempt;
import com.edu_backend.mongo.MongoService;
import com.edu_backend.repository.QuizAttemptRepository;
import com.edu_backend.service.quizAttemptServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Optional;

@RestController
@RequestMapping("/quizAttempt")
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
                @RequestBody QuizAttempt.QuizSetAttempt newAttempt) {
            try{
                QuizAttempt updatedQuizAttempt = quizAttemptService.addQuizAttempt(userId, quizSetId, newAttempt);
                if (!(updatedQuizAttempt == null) && !updatedQuizAttempt.getQuizSet().isEmpty()){
                    return ResponseEntity.ok(updatedQuizAttempt);
                }
                else
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User Quiz Attempt Not saved \nuserId: "+userId
                     + "\nquizSetID: "+quizSetId+"\nAttemptDetails: "+newAttempt);
            }
            catch (DataAccessResourceFailureException e) { // Handle missing collection or database access issues
                return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body("Collection not found or database unavailable. Please initialize the collection.");
            }
            catch (Exception e){
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unable to get details, try again.");
            }

        }


        @GetMapping("/userId/{userId}")  // get all quiz attempt of user by user id
        public ResponseEntity<?> getAllResultUser(@PathVariable String userId) {
            try {
                Optional<QuizAttempt> result = quizAttemptRepository.findByUserId(userId);
                if (result.isEmpty())
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(String.format("{\"message\": \"%s is not there in database\"}", userId));
                else
                    return ResponseEntity.ok(result.get());  // If the user is present, return the details
            }
            catch (DataAccessResourceFailureException e) { // Handle missing collection or database access issues
                return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body("Collection not found or database unavailable. Please initialize the collection.");
            }
            catch (Exception e){
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unable to get details, try again.");
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