package com.edubackend.controller;

import com.edubackend.model.quizattempts.QuizSet;
import com.edubackend.model.quizattempts.QuizSetAttempt;
import com.edubackend.model.quizattempts.QuizAttempts;
import com.edubackend.mongo.MongoService;
import com.edubackend.repository.QuizAttemptsRepository;
import com.edubackend.service.QuizAttemptsServiceImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/quizAttempt")
@Validated
@ControllerAdvice
public class QuizAttemptsController {

    QuizAttemptsServiceImpl quizAttemptService;
    QuizAttemptsRepository quizAttemptsRepository;
    MongoTemplate mongoTemplate;
    MongoService mongoService;

    @Autowired
    public QuizAttemptsController(QuizAttemptsServiceImpl quizAttemptService, QuizAttemptsRepository quizAttemptsRepository, MongoTemplate mongoTemplate, MongoService mongoService) {
        this.quizAttemptService = quizAttemptService;
        this.quizAttemptsRepository = quizAttemptsRepository;
        this.mongoTemplate = mongoTemplate;
        this.mongoService = mongoService;
    }

    @PostMapping("/{userId}/quizset/{quizSetId}/attempts")  // save user quiz attempt on submit
    public ResponseEntity<String> addQuizAttempt(
            @PathVariable String userId,
            @PathVariable String quizSetId,
            @Valid @RequestBody QuizSetAttempt newAttempt) {

        try {

            QuizAttempts savedQuizAttempt = quizAttemptService.addQuizAttempt(userId, quizSetId, newAttempt);

            if ((savedQuizAttempt != null) && !savedQuizAttempt.getQuizSets().isEmpty())
                return ResponseEntity.status(HttpStatus.CREATED).body(savedQuizAttempt.toString());

            else
                ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(
                        String.format("User Quiz Attempt Not Saved\nuserId: %s\nquizSetId: %s\nAttempt Details: %s",
                                userId, quizSetId, newAttempt));

        }
        catch (DataAccessResourceFailureException e) { // Handle missing collection or database access issues
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                    .body("Collection not found or database unavailable. Please initialize the collection.");
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unable to get details, try again.");
        }

        return null;
    }




    @GetMapping("/userId/{userId}")  // get all quiz attempt of user by user id
    public ResponseEntity<String> getAllResultUser(@PathVariable String userId) {

            try {
                QuizAttempts result = quizAttemptsRepository.findByUserId(userId);

                if (result!=null)
                    return ResponseEntity.ok(result.toString());
                else
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(String.format("User ID %s is not found in the database.", userId));


            }
            catch (DataAccessResourceFailureException e) {
                return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                        .body("Collection not found or database unavailable. Please initialize the collection.");
            }
            catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("An error occurred while retrieving user results. Please try again.");
            }
    }


        // get all attempt of any quizSet by quizSetId and UserID
    @GetMapping("/userId/{userId}/quizSetId/{quizSetId}")
    public ResponseEntity<String> getResultsByUserIdAndQuizSetId(
            @PathVariable String userId,
            @PathVariable String quizSetId) {

          try {
                QuizSet results =  mongoService.findQuizSet(userId, quizSetId);

              if (results!=null)
                  return ResponseEntity.ok(results.toString());
              else
                  return ResponseEntity.status(HttpStatus.NOT_FOUND)
                          .body("User quizSet is not there in database userId: "+ userId+ ", quizSet:"+quizSetId);

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