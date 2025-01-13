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


    public boolean saveLeaderboard(){
        Leaderboard leaderboard = calculateLeaderboard();
        if (leaderboard!=null) {
            List<Leaderboard> l = leaderboardRepo.findAll();
            if(!l.isEmpty()) {
                l.get(0).setOverallAccuracy(leaderboard.getOverallAccuracy());
                l.get(0).setTotalQuizAttempted(leaderboard.getTotalQuizAttempted());
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
        for (ResultsAnalysis analysis : resultsAnalyses){

            leaderboard.getTotalQuizAttempted().put(
                    analysis.getUserId(),
                    analysis.getOverallPerformance().getMarksMatrics().getTotalQuizSets().getTotal());

            leaderboard.getOverallAccuracy().put(
                    analysis.getUserId(),
                    analysis.getOverallPerformance().getMarksMatrics().getOverallAccuracy());
        }
        leaderboard.setTotalQuizAttempted(shortmapOfInteger(leaderboard.getTotalQuizAttempted()));
        leaderboard.setOverallAccuracy(shortmap(leaderboard.getOverallAccuracy()));
       return leaderboard;

    }

    public Map<String, Integer> shortmapOfInteger(Map<String,Integer> map){
        // Sort the map by values in descending order
        Map<String, Integer> sortedMapDesc = map.entrySet()
                .stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1, // In case of duplicate keys
                        LinkedHashMap::new // Maintain insertion order
                ));

        // Print the sorted map (descending)
        System.out.println("Sorted Map (Descending): " + sortedMapDesc);
        return  sortedMapDesc;

    }

    public Map<String, Double> shortmap(Map<String,Double> map){
        // Sort the map by values in descending order
        Map<String, Double> sortedMapDesc = map.entrySet()
                .stream()
                .sorted(Map.Entry.<String, Double>comparingByValue().reversed())
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1, // In case of duplicate keys
                        LinkedHashMap::new // Maintain insertion order
                ));

        // Print the sorted map (descending)
        System.out.println("Sorted Map (Descending): " + sortedMapDesc);
        return  sortedMapDesc;

    }


}
