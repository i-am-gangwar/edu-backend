package com.edu_backend.service;

import com.edu_backend.model.QuizAttempt;
import com.edu_backend.repository.QuizAttemptRepository;
import com.edu_backend.service.Interface.QuizAttemptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;



@Service
public class quizAttemptServiceImpl implements QuizAttemptService {

    @Autowired
    QuizAttemptRepository quizAttemptRepository;

    @Override
    public List<com.edu_backend.model.QuizAttempt> getAllResultsOfUser() {
        return quizAttemptRepository.findAll();
    }

    public QuizAttempt addQuizAttempt(String userId, String quizSetId, QuizAttempt.QuizSetAttempt newAttempt){
        Optional<com.edu_backend.model.QuizAttempt> optionalResults  = quizAttemptRepository.findByUserId(userId);
        System.out.println("user:" + optionalResults);
        newAttempt.setDate(new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date()));
        System.out.println("set date:" +newAttempt.getDate());
        QuizAttempt quizAttempt;
        if (optionalResults .isPresent()) {
            quizAttempt = optionalResults.get();
            Optional<com.edu_backend.model.QuizAttempt.QuizSet> optionalQuizSet = quizAttempt.getQuizSet().stream()
                    .filter(qs -> qs.getQuizSetId().equals(quizSetId))
                    .findFirst();

            if (optionalQuizSet.isPresent()) {
                // Add to existing QuizSet
                QuizAttempt.QuizSet quizSet = optionalQuizSet.get();
                quizSet.getQuizSetAttempts().add(newAttempt);

            } else {
                // Create new QuizSet with the new attempt
                QuizAttempt.QuizSet newQuizSet = new QuizAttempt.QuizSet(quizSetId);
                newQuizSet.setQuizSetId(quizSetId);
                newQuizSet.setQuizSetAttempts(new ArrayList<>(List.of(newAttempt)));
                quizAttempt.getQuizSet().add(newQuizSet);


            }
        }
        else{
            // Create new QuizSetAttempt document with the new QuizSet and attempt
            quizAttempt = new QuizAttempt();
            quizAttempt.setUserId(userId);
            QuizAttempt.QuizSet newQuizSet = new QuizAttempt.QuizSet(quizSetId);
            newQuizSet.setQuizSetId(quizSetId);
            newQuizSet.setQuizSetAttempts(new ArrayList<>(List.of(newAttempt)));
            quizAttempt.setQuizSet(new ArrayList<>(List.of(newQuizSet)));

        }
        return quizAttemptRepository.save(quizAttempt);

    }


    public Optional<QuizAttempt.QuizSet> getResultsByUserIdAndQuizSetId(String userId, String quizSetId) {
        return quizAttemptRepository.findByUserIdAndQuizSetQuizSetId(userId, quizSetId);
    }
}