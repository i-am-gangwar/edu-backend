package com.edubackend.controller;
import com.edubackend.Exceptions.Exception.ResourceNotFoundException;
import com.edubackend.model.quizananlysis.ResultsAnalysis;
import com.edubackend.repository.QuizResultAnalysisRepository;
import com.edubackend.service.QuizAttemptResultAnalysisServiceImpl;
import com.edubackend.utils.ApiResponse;
import com.edubackend.utils.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/analytics")
public class QuizAttemptResultAnalysisController {

    @Autowired
    QuizResultAnalysisRepository quizResultAnalysisRepository;
    @Autowired
    QuizAttemptResultAnalysisServiceImpl quizAttemptResultAnalysisServiceImpl;


    @PostMapping("/{userId}")
    public ResponseEntity<ApiResponse<ResultsAnalysis>> saveResultAnalysis(@PathVariable String userId) {
        ResultsAnalysis createdAnalysis = quizAttemptResultAnalysisServiceImpl.createAnalysisByUserId(userId);
        return ResponseUtil.success("Result analysis created", createdAnalysis);
    }


    @PostMapping("/{userId}/{quizSetId}/{setAttemptId}")
    public ResponseEntity<ApiResponse<ResultsAnalysis>> updateResultAnalysis(
            @PathVariable String userId,
            @PathVariable String quizSetId,
            @PathVariable String setAttemptId) {
        ResultsAnalysis updatedAnalysis = quizAttemptResultAnalysisServiceImpl.updateAnalysisSetAttemptId(userId, quizSetId, setAttemptId);
        return ResponseUtil.success("Result analysis updated", updatedAnalysis);

    }


    @GetMapping("/{userId}")
    public ResponseEntity<ApiResponse<ResultsAnalysis>> getResultAnalysis(@PathVariable String userId) {
        try {
            ResultsAnalysis resultsAnalysis = quizResultAnalysisRepository.findByUserId(userId);
            if (resultsAnalysis != null)
                return ResponseUtil.success("Result analysis data fetched successfully.", resultsAnalysis);
            else throw new ResourceNotFoundException("No result analysis found");
        }
        catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException("No result analysis found for useId: " + userId);
        }
        catch (Exception e) {
            throw new RuntimeException("An unexpected error occurred while creating quiz attempt.", e);
        }
    }
}
