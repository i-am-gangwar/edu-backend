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
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/leaderboard")
public class LeaderboardController {


    @Autowired
    LeaderboardServiceImpl leaderboardService;

    @PostMapping
    public ResponseEntity<String> calculateLeaderboard(){
        if(leaderboardService.createOrUpdateLeaderboard())
           return ResponseEntity.status(HttpStatus.CREATED).body("leaderboard calculated/updated and saved!");
        else
            throw new OperationFailedException("unable to create or update leaderboard the request this time.");
    }
    @GetMapping
    public ResponseEntity<ApiResponse<Object>> getLeaderboard() {
        List<Leaderboard> leaderboard = leaderboardService.getLeaderboard();
        return ResponseUtil.success("Leaderboard data fetched successfully.",
                Objects.requireNonNullElseGet(leaderboard, ArrayList::new));
    }

}
