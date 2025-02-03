package com.edubackend.controller;
import com.edubackend.model.quizananlysis.ResultsAnalysis;
import com.edubackend.repository.QuizResultAnalysisRepository;
import com.edubackend.service.QuizAttemptResultAnalysisServiceImpl;
import com.edubackend.utils.ApiResponse;
import com.edubackend.utils.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
        return ResponseUtil.success("Result analysis updated", updatedAnalysis);}


    @GetMapping("/{userId}")
    public ResponseEntity<ApiResponse<Object>> getResultAnalysis(@PathVariable String userId) {
        ResultsAnalysis resultsAnalysis = quizResultAnalysisRepository.findByUserId(userId);
        return ResponseUtil.success("Result analysis fetched successfully.",
                Objects.requireNonNullElseGet(resultsAnalysis, ArrayList::new));

    }
}
