package com.edubackend.controller;

import com.edubackend.model.quizresults.QuizResults;
import com.edubackend.model.quizresults.QuizSetAttemptResult;
import com.edubackend.model.quizresults.QuizSetResult;
import com.edubackend.service.QuizAttemptResultServiceImpl;
import com.edubackend.utils.ApiResponse;
import com.edubackend.utils.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/quizAttemptResult")
public class QuizAttemptResultController {

    private final QuizAttemptResultServiceImpl quizAttemptResultService;

    @Autowired
    public QuizAttemptResultController(QuizAttemptResultServiceImpl quizAttemptResultService) {
        this.quizAttemptResultService = quizAttemptResultService;


    }


    @PostMapping("/{userId}/{quizSetId}/{quizSetAttemptId}")
    public ResponseEntity<ApiResponse<QuizSetAttemptResult>> saveQuizAttemptResult(
            @PathVariable String userId,
            @PathVariable String quizSetId,
            @PathVariable String quizSetAttemptId) {
        QuizSetAttemptResult savedQuizAttemptResult =  quizAttemptResultService.saveQuizAttemptResult(userId,quizSetId,quizSetAttemptId);
        return  ResponseUtil.success("Quiz Set Attempt result saved successfully",savedQuizAttemptResult);

    }

    @PostMapping("/{userId}")
    public ResponseEntity<?> saveAllQuizAttemptResult(
            @PathVariable String userId) {
        QuizResults result =  quizAttemptResultService.saveAllQuizAttemptResult(userId);
        return  ResponseUtil.success("User result saved successfully for all attempts",result);
    }





    // get result by user id
    @GetMapping("/{userId}")
    public ResponseEntity<ApiResponse<QuizResults>> getResultByUserId(@PathVariable String userId){
        QuizResults quizResults = quizAttemptResultService.getResultByUserId(userId);
      return ResponseUtil.success("Result data fetched successfully.",quizResults);

    }


    @GetMapping("/{userId}/{quizSetId}")
    public ResponseEntity<ApiResponse<QuizSetResult>> getQuizSetsResult(@PathVariable String userId,@PathVariable String quizSetId){
            QuizSetResult results = quizAttemptResultService.getResultByUserIdAndQuizSetId(userId,quizSetId);
           return ResponseUtil.success("Result data fetched successfully.",results);

    }


    @GetMapping("/{userId}/{quizSetId}/{quizSetAttemptId}")
    public ResponseEntity<ApiResponse<QuizSetAttemptResult>> getQuizSetAttemptResult(
            @PathVariable String userId
            ,@PathVariable String quizSetId,
            @PathVariable String quizSetAttemptId) throws Exception {
            QuizSetAttemptResult setAttemptResult =
                    quizAttemptResultService.getResultByUserIdAndQuizSetIdAndSetAttemptId(userId,quizSetId,quizSetAttemptId);
        return ResponseUtil.success("Result data fetched successfully.",setAttemptResult);

    }




}