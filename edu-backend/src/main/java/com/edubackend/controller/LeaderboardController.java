package com.edubackend.controller;


import com.edubackend.Exceptions.Exception.OperationFailedException;
import com.edubackend.Exceptions.Exception.ResourceNotFoundException;
import com.edubackend.model.Leaderboard;
import com.edubackend.service.LeaderboardServiceImpl;
import com.edubackend.utils.ApiResponse;
import com.edubackend.utils.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/leaderboard")
public class LeaderboardController {


    @Autowired
    LeaderboardServiceImpl leaderboardService;

    @PostMapping
    public ResponseEntity<String> calculateLeaderboard(){
        if(leaderboardService.saveLeaderboard())
           return ResponseEntity.status(HttpStatus.CREATED).body("leaderboard calculated and saved!");
        else
            throw new OperationFailedException("unable to process the request this time.");
    }

    @GetMapping
    public ResponseEntity<ApiResponse< List<Leaderboard> >> getLeaderboard(){
        try {
            List<Leaderboard>  leaderboard = leaderboardService.getLeaderboard();
            if (!leaderboard.isEmpty())
                return ResponseUtil.success("Leaderboard data fetched successfully.",leaderboard);
            else
                throw new ResourceNotFoundException("No leaderboard found");
        } catch (Exception e) {
            throw new RuntimeException("An unexpected error occurred while creating quiz attempt.", e);
        }
    }


}
