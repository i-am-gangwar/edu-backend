package com.edubackend.controller;

import com.edubackend.model.quizananlysis.ResultsAnalysis;
import com.edubackend.model.quizattempts.QuizSetAttempt;
import com.edubackend.model.quizresults.QuizSetAttemptResult;
import com.edubackend.utils.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/executeAll")
public class OrchestrationController {

    @Autowired
    QuizAttemptsController quizAttemptsController;
    @Autowired
    QuizAttemptResultController quizAttemptResultController;
    @Autowired
    QuizAttemptResultAnalysisController quizAttemptResultAnalysisController;
    @Autowired
    LeaderboardController leaderboardController;



    @PostMapping("/{userId}/{quizSetId}/attempts")
    public String executeAllApis(@PathVariable String userId, @PathVariable String quizSetId, @Valid @RequestBody QuizSetAttempt newAttempt) throws Exception {

        ResponseEntity<ApiResponse<QuizSetAttempt>> savedData = quizAttemptsController.saveQuizAttempt(userId,quizSetId, newAttempt);
        String quizSetAttemptId = savedData.getBody().getData().getQuizSetAttemptId();
        System.out.println("Quiz set attempt data saved successfully!, setattemptId: "+ quizSetAttemptId);


        ResponseEntity<ApiResponse<QuizSetAttemptResult>> savedResult = quizAttemptResultController.saveQuizAttemptResult(userId,quizSetId,quizSetAttemptId);
        System.out.println("Quiz set attempt result calculated and data saved successfully");
        // saved the quiz analysis
        ResponseEntity<ApiResponse<ResultsAnalysis>> savedAnalysis = quizAttemptResultAnalysisController.updateResultAnalysis(userId,quizSetId,quizSetAttemptId);
        System.out.println("Quiz set attempt result analysis calculated and data saved successfully");
        ResponseEntity<String> updatedLeaderboard = leaderboardController.calculateLeaderboard();
        System.out.println("Leaderboard calculated and data saved successfully");



        return "all apis call performed";
    }
}