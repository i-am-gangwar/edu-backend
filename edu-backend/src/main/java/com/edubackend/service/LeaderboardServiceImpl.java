package com.edubackend.service;


import com.edubackend.model.leaderboard.Leaderboard;
import com.edubackend.model.quizananlysis.ResultsAnalysis;
import com.edubackend.repository.LeaderboardRepo;
import com.edubackend.repository.QuizResultAnalysisRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.*;
import java.util.stream.Collectors;
import com.edubackend.model.leaderboard.Pair;

@Service
public class LeaderboardServiceImpl {

    @Autowired
    QuizResultAnalysisRepository quizResultAnalysisRepository;
    @Autowired
    LeaderboardRepo leaderboardRepo;
    @Autowired
    UserService userService;

    public boolean createLeaderboard(){
        Leaderboard leaderboard = calculateLeaderboard();
        List<Leaderboard> l = leaderboardRepo.findAll();
        if (!l.isEmpty()) {
            l.get(0).setScore(leaderboard.getScore());
            leaderboardRepo.save(l.get(0));
            return true;
        }
        else {
            leaderboardRepo.save(leaderboard);
            return true;
        }
    }

    public boolean updateLeaderboard(String userId) throws Exception {
        try {
            List<Leaderboard> l = leaderboardRepo.findAll();
            if (!l.isEmpty()) {
                Leaderboard leaderboard = updateUserScoreInLeaderboard(userId);
                    l.get(0).setScore(leaderboard.getScore());
                    leaderboardRepo.save(l.get(0));
                return true;
            }
            else {
                createLeaderboard();
            }
        }
        catch (Exception ex){
            throw new Exception();
        }
        return false;
    }




    public Leaderboard updateUserScoreInLeaderboard(String userId) throws Exception {
        try {
            List<Leaderboard> l = leaderboardRepo.findAll();
            Leaderboard leaderboard = new Leaderboard();
            if (!l.isEmpty())
                leaderboard = l.get(0);
            String userName = userService.getUserById(userId);
            Integer score = quizResultAnalysisRepository.findByUserId(userId).getOverallPerformance().getMarksMatrics().getTotalCorrectScore().getTotal();
            Pair userScore = new Pair(userName, score);
            leaderboard.setScore(updateOrAddUserScore(leaderboard.getScore(), userId, userScore));
            leaderboard.setScore(sortMapByValueDescending(leaderboard.getScore()));
            return leaderboard;
        }
        catch (Exception ex){
            throw  new Exception();
        }
    }


    public Leaderboard calculateLeaderboard(){
        List<ResultsAnalysis> resultsAnalyses = quizResultAnalysisRepository.findAll();
        Leaderboard leaderboard = new Leaderboard();
        for (ResultsAnalysis analysis : resultsAnalyses) {
         String userName = userService.getUserById(analysis.getUserId());
            Pair userScore  = new Pair(userName,analysis.getOverallPerformance().getMarksMatrics().getTotalCorrectScore().getTotal());
            leaderboard.getScore().put(analysis.getUserId(), userScore);
        }
        leaderboard.setScore(sortMapByValueDescending(leaderboard.getScore()));
       return leaderboard;
    }




    public List<Leaderboard> getLeaderboard(){
        return leaderboardRepo.findAll();
    }

    public static Map<String, Pair<String, Integer>> sortMapByValueDescending(Map<String, Pair<String, Integer>> leaderboard) {
        return leaderboard.entrySet()
                .stream()
                .sorted((entry1, entry2) -> entry2.getValue().getScore().compareTo(entry1.getValue().getScore()))
                .collect(Collectors.toMap(
                        Map.Entry::getKey,      // Map key remains the same
                        Map.Entry::getValue,    // Map value remains the same
                        (e1, e2) -> e1,         // In case of duplicates, choose the first entry
                        LinkedHashMap::new      // Preserve insertion order (descending order)
                ));
    }


    public Map<String, Pair<String, Integer>> updateOrAddUserScore(
            Map<String, Pair<String, Integer>> map,
            String key, Pair<String, Integer> newPair) {
        if (map.containsKey(key)) {
            map.computeIfPresent(key, (k, existingPair) ->
                    new Pair<>(existingPair.getUserName(), newPair.getScore()));
        } else {
            map.put(key, newPair);
        }
        return map;
    }

}
