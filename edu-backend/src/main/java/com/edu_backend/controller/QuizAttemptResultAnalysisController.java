package com.edu_backend.controller;


import com.edu_backend.model.QuizAttemptResult;
import com.edu_backend.model.QuizResultsAnalysis;
import com.edu_backend.repository.QuizAttemptResultRepository;
import com.edu_backend.repository.QuizResultAnalysisRepository;
import com.edu_backend.service.QuizAttemptResultAnalysisServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/analytics")
public class QuizAttemptResultAnalysisController {

    @Autowired
    QuizAttemptResultRepository quizAttemptResultRepository;

    @Autowired
    QuizResultAnalysisRepository quizResultAnalysisRepository;

    @Autowired
    QuizAttemptResultAnalysisServiceImpl quizAttemptResultAnalysisServiceImpl;



    @PostMapping("/{userId}")
    public ResponseEntity<?> saveResultAnalysis(@PathVariable String userId) {
        QuizAttemptResult userResult = quizAttemptResultRepository.findByUserId(userId).orElse(null);
        System.out.println("userResult: " + userResult);
        if (userResult != null) {
            QuizResultsAnalysis quizResultsAnalysis = quizResultAnalysisRepository.findByUserId(userId);
            if (quizResultsAnalysis == null) {
                QuizResultsAnalysis createdResultsAnalysis = quizAttemptResultAnalysisServiceImpl.createResultAnalysis(userResult);
                if (createdResultsAnalysis != null)
                    return ResponseEntity.status(HttpStatus.CREATED).body(createdResultsAnalysis);
                else
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Unable to do create analysis Pls try again! userId: "+userId);
            }
            else{
                QuizResultsAnalysis updateResultsAnalysis = quizAttemptResultAnalysisServiceImpl.updateResultAnalysis(userResult,quizResultsAnalysis);
                if (updateResultsAnalysis != null)
                    return ResponseEntity.status(HttpStatus.CREATED).body(updateResultsAnalysis);
                else
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Unable to do update analysis Pls try again! userId: "+userId);
            }
        }
        else
          return ResponseEntity.status(HttpStatus.NOT_FOUND).body(String.format("No UserID: %s found to calculate results analysis!",userId));
    }




    @GetMapping("/{userId}")
    public ResponseEntity<?> getresultAnalysis(@PathVariable String userId) {
        try {
            QuizResultsAnalysis quizResultsAnalysis = quizResultAnalysisRepository.findByUserId(userId);
            if (quizResultsAnalysis != null)
                return ResponseEntity.status(HttpStatus.FOUND).body(quizResultsAnalysis);
            else
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No UserID: %s found to calculate results analysis in database!" + userId);
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }
    }
}
