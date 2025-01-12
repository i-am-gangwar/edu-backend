package com.edubackend.controller;

import com.edubackend.model.quizattempts.QuizSet;
import com.edubackend.model.quizattempts.QuizSetAttempt;
import com.edubackend.model.quizattempts.QuizAttempts;
import com.edubackend.service.QuizAttemptsServiceImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/quizAttempt")
@Validated
@ControllerAdvice
public class QuizAttemptsController {

   private final QuizAttemptsServiceImpl quizAttemptService;

    @Autowired
    public QuizAttemptsController(QuizAttemptsServiceImpl quizAttemptService) {
        this.quizAttemptService = quizAttemptService;
    }

    @PostMapping("/{userId}/quizset/{quizSetId}/attempts")  // save user quiz attempt on submit
    public ResponseEntity<?> addQuizAttempt(
            @PathVariable String userId,
            @PathVariable String quizSetId,
            @Valid @RequestBody QuizSetAttempt newAttempt) {

        try {

            QuizAttempts savedQuizAttempt = quizAttemptService.addQuizAttempt(userId, quizSetId, newAttempt);

            if ((savedQuizAttempt != null) && !savedQuizAttempt.getQuizSets().isEmpty())
                return ResponseEntity.status(HttpStatus.CREATED).body(savedQuizAttempt);

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
    public ResponseEntity<?> getAllResultUser(@PathVariable String userId) {

            try {
                QuizAttempts result = quizAttemptService.getResultByUserId(userId);
                if (result!=null)
                    return ResponseEntity.ok()
                            .header("Content-Type", "application/json").
                            body(result);
                else
                    return ResponseEntity.status(HttpStatus.NOT_FOUND)
                            .header("Content-Type", "application/json")
                            .body(String.format("User ID %s is not found in the database.", userId));
            }
            catch (DataAccessResourceFailureException e) {
                return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                        .header("Content-Type", "application/json")
                        .body("Collection not found or database unavailable. Please initialize the collection.");
            }
            catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .header("Content-Type", "application/json")
                        .body("An error occurred while retrieving user results. Please try again.");
            }
    }


        // get all attempt of any quizSet by quizSetId and UserID
    @GetMapping("/userId/{userId}/quizSetId/{quizSetId}")
    public ResponseEntity<?> getResultsByUserIdAndQuizSetId(
            @PathVariable String userId,
            @PathVariable String quizSetId) {

          try {
                QuizSet results =  quizAttemptService.getResultByUserIdAndSetId(userId,quizSetId);
              if (results!=null && !results.getQuizSetAttempts().isEmpty()) {
                  results.setQuizSetId(quizSetId);
                  return ResponseEntity.ok(results);
              }
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

    @GetMapping("/userId/{userId}/quizSetId/{quizSetId}/{setAttemptId}")
    public ResponseEntity<?> getResultSetAttemptIdById(
            @PathVariable String userId,
            @PathVariable String quizSetId,
            @PathVariable String setAttemptId) {

        try {
           QuizSetAttempt setAttempt = quizAttemptService.getResultByUserIdAndSetIdAndSetAttemptId(userId,quizSetId,setAttemptId);
            if (setAttempt!=null) {
                return ResponseEntity.ok(setAttempt);
            }
            else
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("User quizSetAttempt is not there in database userId: "+ userId+ ", quizSet:"+quizSetId+
                                ", setAttemptId:"+setAttemptId);

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