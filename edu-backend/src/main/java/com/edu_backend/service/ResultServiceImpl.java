package com.edu_backend.service;

import com.edu_backend.model.Results;
import com.edu_backend.repository.ResultsRepository;
import com.edu_backend.service.Interface.ResultService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class ResultServiceImpl implements ResultService {

    @Autowired
    ResultsRepository resultsRepository;

    @Override
    public List<Results> getAllResultsOfUser() {
        return resultsRepository.findAll();
    }

    public Results addQuizAttempt(String userId, String quizSetId, Results.QuizAttempt newAttempt){
        Optional<Results> optionalResults  = resultsRepository.findByUserId(userId);
        System.out.println("user:" + optionalResults);
        newAttempt.setDate(new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date()));
        System.out.println("set date:" +newAttempt.getDate());
        Results results;
        if (optionalResults .isPresent()) {
            results = optionalResults.get();
            Optional<Results.QuizSet> optionalQuizSet = results.getQuizSet().stream()
                    .filter(qs -> qs.getQuizSetId().equals(quizSetId))
                    .findFirst();

            if (optionalQuizSet.isPresent()) {
                // Add to existing QuizSet
               Results.QuizSet quizSet = optionalQuizSet.get();
             quizSet.getQuizSetAttempts().add(newAttempt);

            } else {
                // Create new QuizSet with the new attempt
                Results.QuizSet newQuizSet = new Results.QuizSet();
                newQuizSet.setQuizSetId(quizSetId);
                newQuizSet.setQuizSetAttempts(new ArrayList<>(List.of(newAttempt)));
                results.getQuizSet().add(newQuizSet);


            }
        }
        else{
            // Create new Results document with the new QuizSet and attempt
            results = new Results();
            results.setUserId(userId);
            Results.QuizSet newQuizSet = new Results.QuizSet();
            newQuizSet.setQuizSetId(quizSetId);
            newQuizSet.setQuizSetAttempts(new ArrayList<>(List.of(newAttempt)));
            results.setQuizSet(new ArrayList<>(List.of(newQuizSet)));

        }
        return resultsRepository.save(results);

    }


    public Optional<Results> getResultsByUserIdAndQuizSetId(String userId, String quizSetId) {
        return resultsRepository.findByUserIdAndQuizSetQuizSetId(userId, quizSetId);
    }
}