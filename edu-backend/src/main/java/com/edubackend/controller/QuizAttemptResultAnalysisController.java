package com.edubackend.controller;


import com.edubackend.model.quizresults.QuizResults;
import com.edubackend.model.quizananlysis.ResultsAnalysis;
import com.edubackend.repository.QuizAttemptResultRepository;
import com.edubackend.repository.QuizResultAnalysisRepository;
import com.edubackend.service.QuizAttemptResultAnalysisServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/analytics")
public class QuizAttemptResultAnalysisController {

    QuizAttemptResultRepository quizAttemptResultRepository;
    QuizResultAnalysisRepository quizResultAnalysisRepository;
    QuizAttemptResultAnalysisServiceImpl quizAttemptResultAnalysisServiceImpl;

    @Autowired
    public QuizAttemptResultAnalysisController(QuizAttemptResultRepository quizAttemptResultRepository,
                                               QuizResultAnalysisRepository quizResultAnalysisRepository,
                                               QuizAttemptResultAnalysisServiceImpl quizAttemptResultAnalysisServiceImpl) {
        this.quizAttemptResultRepository = quizAttemptResultRepository;
        this.quizResultAnalysisRepository = quizResultAnalysisRepository;
        this.quizAttemptResultAnalysisServiceImpl = quizAttemptResultAnalysisServiceImpl;
    }

    @PostMapping("/{userId}")
    public ResponseEntity<?> saveResultAnalysis(@PathVariable String userId) {
        QuizResults userResult = quizAttemptResultRepository.findByUserId(userId);
        if (userResult != null) {
            ResultsAnalysis resultsAnalysis = quizResultAnalysisRepository.findByUserId(userId);
            if (resultsAnalysis == null) {
                ResultsAnalysis createdAnalysis = quizAttemptResultAnalysisServiceImpl.createAnalysis(userResult);
                if (createdAnalysis != null)
                    return ResponseEntity.status(HttpStatus.CREATED).body(createdAnalysis);
                else
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Unable to do create analysis Pls try again! userId: "+userId);
            }
            else{
                ResultsAnalysis updateAnalysis = quizAttemptResultAnalysisServiceImpl.updateAnalysis(userResult, resultsAnalysis);
                if (updateAnalysis != null)
                    return ResponseEntity.status(HttpStatus.CREATED).body(updateAnalysis);
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
            ResultsAnalysis resultsAnalysis = quizResultAnalysisRepository.findByUserId(userId);
            if (resultsAnalysis != null)
                return ResponseEntity.status(HttpStatus.FOUND).body(resultsAnalysis);
            else
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No UserID: %s found to calculate results analysis in database!" + userId);
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }
    }
}
