package com.edubackend.service;

import com.edubackend.model.quizattempts.QuizSet;
import com.edubackend.model.quizattempts.QuizSetAttempt;
import com.edubackend.model.quizattempts.QuizAttempts;
import com.edubackend.repository.QuizAttemptsRepository;
import com.edubackend.service.interfaces.QuizAttemptsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;



@Service
public class QuizAttemptsServiceImpl implements QuizAttemptsService {

   private final QuizAttemptsRepository quizAttemptsRepository;

    @Autowired
    public QuizAttemptsServiceImpl(QuizAttemptsRepository quizAttemptsRepository) {
        this.quizAttemptsRepository = quizAttemptsRepository;
    }


    public List<QuizAttempts> getAllResultsOfUser() {
        return quizAttemptsRepository.findAll();
    }


    public QuizAttempts createQuizAttempt(String userId, String quizSetId, QuizSetAttempt newAttempt){
      QuizAttempts  createResults = new QuizAttempts();
        createResults.setUserId(userId);
        QuizSet newQuizSet = new QuizSet();
        newQuizSet.setQuizSetId(quizSetId);
        newQuizSet.setQuizSetAttempts(new ArrayList<>(List.of(newAttempt)));
        createResults.setQuizSets(new ArrayList<>(List.of(newQuizSet)));
        return createResults;

    }
    public QuizSet createQuizSetAttempt(String quizSetId, QuizSetAttempt newAttempt ){
        QuizSet newQuizSet = new QuizSet();
        newQuizSet.setQuizSetId(quizSetId);
        newQuizSet.setQuizSetAttempts(new ArrayList<>(List.of(newAttempt)));
       return newQuizSet;

    }

    public QuizAttempts addQuizAttempt(String userId, String quizSetId, QuizSetAttempt newAttempt){

        QuizAttempts optionalResults  =  quizAttemptsRepository.findByUserId(userId);
        newAttempt.setDate(new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date()));

        if (optionalResults!=null) {
            Optional<QuizSet> quizSets = optionalResults.getQuizSets().stream()
                    .filter(qs -> qs.getQuizSetId().equals(quizSetId)).findFirst();

            if (quizSets.isPresent())
               quizSets.get().getQuizSetAttempts().add(newAttempt);
            else
                optionalResults.getQuizSets().add(createQuizSetAttempt(quizSetId,newAttempt));
        }
        else
            optionalResults = createQuizAttempt(userId,quizSetId,newAttempt);
        return quizAttemptsRepository.save(optionalResults);

    }
}