package com.edubackend.service;

import com.edubackend.Exceptions.Exception.OperationFailedException;
import com.edubackend.Exceptions.Exception.ResourceNotFoundException;
import com.edubackend.model.quizattempts.QuizSet;
import com.edubackend.model.quizattempts.QuizSetAttempt;
import com.edubackend.model.quizattempts.QuizAttempts;
import com.edubackend.repository.QuizAttemptsRepository;
import com.edubackend.service.interfaces.QuizAttemptsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;



@Slf4j
@Service
public class QuizAttemptsServiceImpl implements QuizAttemptsService {

   private final QuizAttemptsRepository quizAttemptsRepository;

    @Autowired
    public QuizAttemptsServiceImpl(QuizAttemptsRepository quizAttemptsRepository) {
        this.quizAttemptsRepository = quizAttemptsRepository;
    }

    @Override
    public QuizAttempts createQuizAttempt(String userId, String quizSetId, QuizSetAttempt newAttempt){
        QuizAttempts  createResults = new QuizAttempts();
        createResults.setUserId(userId);
        QuizSet newQuizSet = new QuizSet();
        newQuizSet.setQuizSetId(quizSetId);
        newQuizSet.setQuizSetAttempts(new ArrayList<>(List.of(newAttempt)));
        createResults.setQuizSets(new ArrayList<>(List.of(newQuizSet)));
        return createResults;

    }

    @Override
    public QuizSet createQuizSetAttempt(String quizSetId, QuizSetAttempt newAttempt ){
        QuizSet newQuizSet = new QuizSet();
        newQuizSet.setQuizSetId(quizSetId);
        newQuizSet.setQuizSetAttempts(new ArrayList<>(List.of(newAttempt)));
        return newQuizSet;

    }

    @Override
    public QuizSetAttempt createSetAttempt(QuizSetAttempt newAttempt) {
        QuizSetAttempt setAttempt = new QuizSetAttempt();
        setAttempt.setDate(new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date()));
        setAttempt.setTotalTimeTakenToAttempt(newAttempt.getTotalTimeTakenToAttempt());
        setAttempt.setSetAttempt(newAttempt.getSetAttempt());
        return setAttempt;
    }



    public  QuizSetAttempt saveQuizAttempt(String userId, String quizSetId, QuizSetAttempt newAttempt ) throws Exception {
        try {
            QuizSetAttempt savedQuizSetAttempt = addQuizAttempt(userId, quizSetId, newAttempt);
            if (savedQuizSetAttempt == null) {
                throw new OperationFailedException(String.format(
                        "User Quiz Attempt Not Saved. userId: %s, quizSetId: %s, Attempt Details: %s",
                        userId, quizSetId, newAttempt));
            }
            return savedQuizSetAttempt;
        }
        catch (OperationFailedException e) {
            log.warn("Operation failed: {}", e.getMessage(), e);
            throw e;
        }
        catch (DataAccessResourceFailureException e) {
            String errorMessage = "Collection not found or database unavailable. Please initialize the collection.";
            log.error(errorMessage, e);
            throw new DataAccessResourceFailureException(errorMessage);

        }
         catch (Exception e) {
            String errorMessage = "An unexpected error occurred while saving the quiz attempt.";
            log.error(errorMessage, e);
            throw new RuntimeException(errorMessage, e);
        }
    }

    @Transactional
    public QuizSetAttempt addQuizAttempt(String userId, String quizSetId, QuizSetAttempt newAttempt){
        QuizAttempts optionalResults  =  quizAttemptsRepository.findByUserId(userId);
        QuizSetAttempt setAttempt = createSetAttempt(newAttempt);
        if (optionalResults!=null) {

            Optional<QuizSet> quizSets = optionalResults.getQuizSets().stream()
                    .filter(qs -> qs.getQuizSetId().equals(quizSetId)).findFirst();

            if (quizSets.isPresent())
               quizSets.get().getQuizSetAttempts().add(setAttempt);
            else
                optionalResults.getQuizSets().add(createQuizSetAttempt(quizSetId,setAttempt));
        }
        else
            optionalResults = createQuizAttempt(userId,quizSetId,setAttempt);
        quizAttemptsRepository.save(optionalResults);
        return setAttempt;

    }

    public QuizAttempts getResultByUserId(String userId) {
        return quizAttemptsRepository.findByUserId(userId);

    }

    public QuizSet getResultByUserIdAndSetId(String userId,String quizSetId) {

        return quizAttemptsRepository.findQuizSetAttemptsByUserIdAndQuizSetId(userId,quizSetId);


    }


    public QuizSetAttempt getResultByUserIdAndSetIdAndSetAttemptId(String userId, String quizSetId, String setAttemptId) {
            QuizSet results =  quizAttemptsRepository.findQuizSetAttemptsByUserIdAndQuizSetId(userId,quizSetId);
            Optional<QuizSetAttempt> quizSetAttempt = results.getQuizSetAttempts().stream()
                    .filter(qs -> qs.getQuizSetAttemptId().equals(setAttemptId)).findFirst();
        return quizSetAttempt.orElse(null);


    }



//    public <T> T checkData(T result){
//        try {
//            if (result==null) {
//                throw new OperationFailedException(
//                        "User data is not found in db, enter valid userId");
//            }
//            return result;
//        }
//        catch (OperationFailedException e) {
//            log.warn("Operation failed: {}", e.getMessage(), e);
//            throw e;
//        }
//        catch (DataAccessResourceFailureException e) {
//            String errorMessage = "Collection not found or database unavailable. Please initialize the collection.";
//            log.error(errorMessage, e);
//            throw new DataAccessResourceFailureException(errorMessage);
//        }
//        catch (ResourceNotFoundException ex){
//            throw new ResourceNotFoundException("An error occurred while retrieving user results. Please try again.");
//        }
//
//    }



}