package com.edu_backend.controller;

import com.edu_backend.repository.QuizAttemptResultAnalysisRepository;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/analytics")
public class QuizAttemptResultAnalysisController {

    private final QuizAttemptResultAnalysisRepository quizAttemptResultAnalysisRepository;

    public QuizAttemptResultAnalysisController(QuizAttemptResultAnalysisRepository quizAttemptResultAnalysisRepository) {
        this.quizAttemptResultAnalysisRepository = quizAttemptResultAnalysisRepository;
    }

//    public AnalyticsController(AnalyticsSummaryRepository analyticsSummaryRepository) {
//        this.resultAnalysisRepository = analyticsSummaryRepository;
//    }
//
//    public QuizAttemptResultAnalysisController(ResultAnalysisRepository resultAnalysisRepository) {
//        this.resultAnalysisRepository = resultAnalysisRepository;
//    }
//
//    @GetMapping("/{userId}")
//    public ResponseEntity<AnalyticsSummary> getAnalytics(@PathVariable String userId) {
//        return resultAnalysisRepository.findById(userId)
//                .map(ResponseEntity::ok)
//                .orElseThrow(() -> new IllegalArgumentException("No analytics found for user: " + userId));
//    }
}
