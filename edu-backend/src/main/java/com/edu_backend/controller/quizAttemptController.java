package com.edu_backend.controller;

import com.edu_backend.model.QuizAttempt;
import com.edu_backend.repository.QuizAttemptRepository;
import com.edu_backend.service.quizAttemptServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/quizAttempt")
public class quizAttemptController {

        @Autowired
        quizAttemptServiceImpl quizAttemptService;
        @Autowired
        QuizAttemptRepository quizAttemptRepository;

        // save user quiz attempt on submit
        @PostMapping("/{userId}/quizsets/{quizSetId}/attempts")
        public ResponseEntity<?> addQuizAttempt(
                @PathVariable String userId,
                @PathVariable String quizSetId,
                @RequestBody QuizAttempt.QuizSetAttempt newAttempt) {
            QuizAttempt updatedQuizAttempt = quizAttemptService.addQuizAttempt(userId, quizSetId, newAttempt);
            if (!(updatedQuizAttempt == null) && !updatedQuizAttempt.getQuizSet().isEmpty()){
                return ResponseEntity.ok(updatedQuizAttempt);
            }
            else
                return (ResponseEntity<?>) ResponseEntity.notFound();
        }
        // get all quiz attempt of user by user id
        @GetMapping("/userId/{userId}")
        public QuizAttempt getAllResultUser(@PathVariable String userId){
            return quizAttemptRepository.findByUserId(userId)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Result not found"));
        }
        // get all attempt of any quizSet by quizSetId and UserID
        @GetMapping("/userId/{userId}/quizSetId/{quizSetId}")
        public ResponseEntity<?> getResultsByUserIdAndQuizSetId(@PathVariable String userId, @PathVariable String quizSetId) {
            Optional<QuizAttempt.QuizSet> results = quizAttemptService.getResultsByUserIdAndQuizSetId(userId, quizSetId);

            if (results.isPresent()) {
                return ResponseEntity.ok(results);
            } else {
                return ResponseEntity.notFound().build();
            }

        }
}