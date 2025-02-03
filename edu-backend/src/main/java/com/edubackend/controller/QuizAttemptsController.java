package com.edubackend.controller;

import com.edubackend.model.quizattempts.QuizSet;
import com.edubackend.model.quizattempts.QuizSetAttempt;
import com.edubackend.model.quizattempts.QuizAttempts;
import com.edubackend.service.QuizAttemptsServiceImpl;
import com.edubackend.utils.ApiResponse;
import com.edubackend.utils.ResponseUtil;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.Objects;

@RestController
@RequestMapping("/quizAttempt")
@Validated
@ControllerAdvice
public class QuizAttemptsController {

   private final QuizAttemptsServiceImpl quizAttemptService;

    @Autowired
    public QuizAttemptsController(QuizAttemptsServiceImpl quizAttemptService) {
        this.quizAttemptService = quizAttemptService;
    }


    @PostMapping("/{userId}/quizset/{quizSetId}/attempts")
    public ResponseEntity<ApiResponse<QuizSetAttempt>> saveQuizAttempt(
            @PathVariable String userId,
            @PathVariable String quizSetId,
            @Valid @RequestBody QuizSetAttempt newAttempt) throws Exception {

        QuizSetAttempt savedSetAttempt =  quizAttemptService.saveQuizAttempt(userId,quizSetId,newAttempt);
        return ResponseUtil.success("quiz Attempt saved successfully",savedSetAttempt);
    }


    @GetMapping("/userId/{userId}")
    public ResponseEntity<ApiResponse<Object>> getAllResultUser(
            @PathVariable String userId) {
        QuizAttempts quizAttempts = quizAttemptService.getResultByUserId(userId);
        return ResponseUtil.success("data fetched successfully",
                Objects.requireNonNullElseGet(quizAttempts, ArrayList::new));
    }


        // get all attempt of any quizSet by quizSetId and UserID
    @GetMapping("/userId/{userId}/quizSetId/{quizSetId}")
    public ResponseEntity<ApiResponse<Object>> getResultsByUserIdAndQuizSetId(
            @PathVariable String userId,
            @PathVariable String quizSetId) {
        QuizSet quizSet = quizAttemptService.getResultByUserIdAndSetId(userId,quizSetId);
        return ResponseUtil.success("data fetched successfully",
                Objects.requireNonNullElseGet(quizSet, ArrayList::new));
    }

    @GetMapping("/userId/{userId}/quizSetId/{quizSetId}/{setAttemptId}")
    public ResponseEntity<ApiResponse<Object>> getResultBySetAttemptId(
            @PathVariable String userId,
            @PathVariable String quizSetId,
            @PathVariable String setAttemptId) {
        QuizSetAttempt setAttempt = quizAttemptService.getResultByUserIdAndSetIdAndSetAttemptId(userId,quizSetId,setAttemptId);
        return ResponseUtil.success("data fetched successfully",
                Objects.requireNonNullElseGet(setAttempt, ArrayList::new));
    }





}