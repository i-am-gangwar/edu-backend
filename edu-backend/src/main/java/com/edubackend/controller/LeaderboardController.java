package com.edubackend.controller;


import com.edubackend.model.Leaderboard;
import com.edubackend.service.LeaderboardServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
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
            return ResponseEntity.status(HttpStatus.CREATED).body("leaderboard not calculated try again!");

    }

    @GetMapping
    public ResponseEntity<List<Leaderboard>> getLeaderboard(){
        return ResponseEntity.ok(leaderboardService.getLeaderboard());
    }


}
