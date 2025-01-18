package com.edubackend.service;


import com.edubackend.model.Leaderboard;
import com.edubackend.model.quizananlysis.ResultsAnalysis;
import com.edubackend.repository.LeaderboardRepo;
import com.edubackend.repository.QuizResultAnalysisRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class LeaderboardServiceImpl {

    @Autowired
    QuizResultAnalysisRepository quizResultAnalysisRepository;
    @Autowired
    LeaderboardRepo leaderboardRepo;


    public boolean createOrUpdateLeaderboard(){
        Leaderboard leaderboard = calculateLeaderboard();
        if (leaderboard!=null) {
            List<Leaderboard> l = leaderboardRepo.findAll();
            if(!l.isEmpty()) {
                l.get(0).setScore(leaderboard.getScore());
                leaderboardRepo.save(l.get(0));
                return true;
            }
            else {
                leaderboardRepo.save(leaderboard);
                return  true;
            }
        }
        else
           return false;
    }


    public List<Leaderboard> getLeaderboard(){
        return leaderboardRepo.findAll();
    }






    public Leaderboard calculateLeaderboard(){

        List<ResultsAnalysis> resultsAnalyses = quizResultAnalysisRepository.findAll();
        Leaderboard leaderboard = new Leaderboard();
        for (ResultsAnalysis analysis : resultsAnalyses) {

            leaderboard.getScore().put(
                    analysis.getUserId(),
                    analysis.getOverallPerformance().getMarksMatrics().getTotalCorrectScore().getTotal());
        }
        leaderboard.setScore(shortMapOfInteger(leaderboard.getScore()));
       return leaderboard;

    }

    public Map<String, Integer> shortMapOfInteger(Map<String,Integer> map){
        Map<String, Integer> sortedMapDesc = map.entrySet()
                .stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1, // In case of duplicate keys
                        LinkedHashMap::new // Maintain insertion order
                ));

        System.out.println("Sorted Map (Descending): " + sortedMapDesc);
        return  sortedMapDesc;

    }

}
