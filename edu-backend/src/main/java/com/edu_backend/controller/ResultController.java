package com.edu_backend.controller;

import com.edu_backend.model.Results;
import com.edu_backend.repository.ResultsRepository;
import com.edu_backend.service.ResultServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/results")
public class ResultController {

    @Autowired
    ResultServiceImpl resultServiceImpl;

    @Autowired
    ResultsRepository resultsRepository;

    // save user quiz attempt on submit
    @PostMapping("/{userId}/quizsets/{quizSetId}/attempts")
    public ResponseEntity<?> addQuizAttempt(
            @PathVariable String userId,
            @PathVariable String quizSetId,
            @RequestBody Results.QuizAttempt newAttempt) {
        Results updatedResults = resultServiceImpl.addQuizAttempt(userId, quizSetId, newAttempt);
        if (!(updatedResults == null) && !updatedResults.getQuizSet().isEmpty()){
            return ResponseEntity.ok(updatedResults);
        }
        else
            return (ResponseEntity<?>) ResponseEntity.notFound();
    }

    // get all quiz attempt of user
    @GetMapping("/userId/{userId}")
    public Results getAllResultUser(@PathVariable String userId){
        return resultsRepository.findByUserId(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Result not found"));
    }

    // get all attempt of any quizset

    @GetMapping("/userId/{userId}/quizSetId/{quizSetId}")
    public ResponseEntity<?> getResultsByUserIdAndQuizSetId(@PathVariable String userId, @PathVariable String quizSetId) {
        Optional<Results> results = resultServiceImpl.getResultsByUserIdAndQuizSetId(userId, quizSetId);

        if (results.isPresent()) {
            Results result = results.get();
            // Filter quizSets by quizSetId
            result.setQuizSet(
                    result.getQuizSet().stream()
                    .filter(quizSet -> quizSet.getQuizSetId().equals(quizSetId))
                    .collect(Collectors.toList())
            );
            return ResponseEntity.ok(results);
        } else {
            return ResponseEntity.notFound().build();
        }

    }







//

//    @PostMapping()
//    public ResponseEntity<?> saveResults(@RequestBody Results inputResults) {
//       // System.out.println("User ID from URL: " + userId);
//        System.out.println("Input Results: " + inputResults);
//        if (inputResults == null || inputResults.getQuizSet() == null) {
//            System.out.println("Invalid input!");
//            return new ResponseEntity<>("not saved",HttpStatus.NOT_FOUND);
//        }
//        // Set the userId
//       // inputResults.setUserId(userId);
//        System.out.println("Updated Results: " + inputResults);
//        // Save to database
//        resultServiceImpl.saveResult(inputResults );
//        return new ResponseEntity<>("saved",HttpStatus.OK);
//    }






}
