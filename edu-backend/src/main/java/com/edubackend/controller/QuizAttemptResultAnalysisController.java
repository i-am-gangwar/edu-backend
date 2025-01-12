package com.edubackend.controller;



import com.edubackend.model.quizananlysis.ResultsAnalysis;
import com.edubackend.repository.QuizResultAnalysisRepository;
import com.edubackend.service.QuizAttemptResultAnalysisServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/analytics")
public class QuizAttemptResultAnalysisController {
    QuizResultAnalysisRepository quizResultAnalysisRepository;
    QuizAttemptResultAnalysisServiceImpl quizAttemptResultAnalysisServiceImpl;

    @Autowired
    public QuizAttemptResultAnalysisController(QuizResultAnalysisRepository quizResultAnalysisRepository,
                                               QuizAttemptResultAnalysisServiceImpl quizAttemptResultAnalysisServiceImpl) {
        this.quizResultAnalysisRepository = quizResultAnalysisRepository;
        this.quizAttemptResultAnalysisServiceImpl = quizAttemptResultAnalysisServiceImpl;
    }

    @PostMapping("/{userId}")
    public ResponseEntity<?> saveResultAnalysis(@PathVariable String userId) {
        try{

            ResultsAnalysis createdAnalysis = quizAttemptResultAnalysisServiceImpl.createAnalysisByUserId(userId);
            if (createdAnalysis != null)
                return ResponseEntity.status(HttpStatus.CREATED).body(createdAnalysis);
            else
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Unable to do create analysis Pls try again! userId: "+userId);

        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(String.format("No UserID: %s found to calculate results analysis!", userId));
        }
    }

    @PostMapping("/{userId}/{quizSetId}/{setAttemptId}")
    public ResponseEntity<?> updateResultAnalysis(
            @PathVariable String userId,
            @PathVariable String quizSetId,
            @PathVariable String setAttemptId){
        try{

            ResultsAnalysis updatedAnalysis = quizAttemptResultAnalysisServiceImpl.updateAnalysisSetAttemptId(userId,quizSetId,setAttemptId);
            if (updatedAnalysis != null)
                return ResponseEntity.status(HttpStatus.CREATED).body(updatedAnalysis);
            else
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Unable to do update analysis Pls try again! userId: "+userId);

        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(String.format("No UserID: %s found to calculate results analysis!", userId));
        }
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
