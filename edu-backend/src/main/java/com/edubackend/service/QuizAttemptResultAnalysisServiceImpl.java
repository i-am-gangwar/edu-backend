package com.edubackend.service;

import com.edubackend.Exceptions.Exception.OperationFailedException;
import com.edubackend.model.quizananlysis.OverallPerformance;
import com.edubackend.model.quizananlysis.ResultsAnalysis;
import com.edubackend.model.quizresults.QuizResults;
import com.edubackend.model.quizresults.QuizSetAttemptResult;
import com.edubackend.model.quizresults.QuizSetResult;
import com.edubackend.repository.QuizAttemptResultRepository;
import com.edubackend.repository.QuizResultAnalysisRepository;
import com.edubackend.service.interfaces.QuizAttemptResultAnalysisService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;


@Service
@Slf4j
public class QuizAttemptResultAnalysisServiceImpl implements QuizAttemptResultAnalysisService {

  private final   QuizAttemptResultRepository  quizAttemptResultRepository;
  private final   QuizResultAnalysisRepository quizResultAnalysisRepository;
  private final   QuizAttemptResultServiceImpl quizAttemptResultService;

    @Autowired
    public QuizAttemptResultAnalysisServiceImpl(QuizAttemptResultRepository quizAttemptResultRepository,
                                                QuizResultAnalysisRepository quizResultAnalysisRepository,
                                                QuizAttemptResultServiceImpl quizAttemptResultService) {
        this.quizAttemptResultRepository = quizAttemptResultRepository;
        this.quizResultAnalysisRepository = quizResultAnalysisRepository;
        this.quizAttemptResultService = quizAttemptResultService;
    }


    public ResultsAnalysis createAnalysisByUserId(String userId ){
        try {

            QuizResults userResult = quizAttemptResultRepository.findByUserId(userId);
            ResultsAnalysis resultsAnalysis = calculatePerformance(userResult);
            ResultsAnalysis rsltanalysis = quizResultAnalysisRepository.findByUserId(userId);
            if(rsltanalysis!=null)
                resultsAnalysis.setId(rsltanalysis.getId());
            quizResultAnalysisRepository.save(resultsAnalysis);
            return resultsAnalysis;
        }
        catch (OperationFailedException e) {
            log.warn("Operation failed: {}", e.getMessage(), e);
            throw e;
        }
        catch (DataAccessResourceFailureException e) {  // Handle missing collection or database access issues
            String errorMessage = "Collection not found or database unavailable. Please initialize the collection.";
            log.error(errorMessage, e);
            throw new DataAccessResourceFailureException(errorMessage);

        }
        catch (Exception e) {
            // Handle unexpected exceptions and log them for troubleshooting
            String errorMessage = "An unexpected error occurred while creating quiz attempt.";
            log.error(errorMessage, e);
            throw new RuntimeException(errorMessage, e);
        }


    }


    public ResultsAnalysis updateAnalysisSetAttemptId(String userId,String quizSetId, String quizSetAttemptId) {

        try{
            ResultsAnalysis resultsAnalysis = quizResultAnalysisRepository.findByUserId(userId);
            QuizSetAttemptResult quizSetAttemptResult =  quizAttemptResultService.getResultByUserIdAndQuizSetIdAndSetAttemptId(userId,quizSetId,quizSetAttemptId);
            if(quizSetAttemptResult==null)
                throw new OperationFailedException("User data not found in db.");
            if (resultsAnalysis!=null)
                resultsAnalysis.setOverallPerformance(
                    calculateAttemptPerformance(resultsAnalysis.getOverallPerformance(),quizSetAttemptResult));
            else{
                 resultsAnalysis =  new ResultsAnalysis();
                resultsAnalysis.setUserId(userId);
                OverallPerformance overAllper = new OverallPerformance();
                resultsAnalysis.setOverallPerformance(calculateAttemptPerformance(
                        resultsAnalysis.getOverallPerformance(),quizSetAttemptResult));
            }
            quizResultAnalysisRepository.save(resultsAnalysis);
            return resultsAnalysis;

        }
        catch (OperationFailedException e) {
            log.warn("Operation failed: {}", e.getMessage(), e);
            throw e;
        }
        catch (DataAccessResourceFailureException e) {  // Handle missing collection or database access issues
            String errorMessage = "Collection not found or database unavailable. Please initialize the collection.";
            log.error(errorMessage, e);
            throw new DataAccessResourceFailureException(errorMessage);

        }
        catch (Exception e) {
            // Handle unexpected exceptions and log them for troubleshooting
            String errorMessage = "An unexpected error occurred while creating quiz attempt.";
            log.error(errorMessage, e);
            throw new RuntimeException(errorMessage, e);
        }



    }



    @Transactional
    public ResultsAnalysis calculatePerformance(QuizResults userResult){

        ResultsAnalysis resultsAnalysis = new ResultsAnalysis();
        resultsAnalysis.setUserId(userResult.getUserId());
        OverallPerformance overAllper = new OverallPerformance();
        List<QuizSetResult> quizSetResult = userResult.getQuizSetResults();

        for(QuizSetResult qzSet: quizSetResult){
            for (QuizSetAttemptResult attemptResult: qzSet.getQuizSetAttemptResults())
                overAllper = calculateAttemptPerformance(overAllper,attemptResult);
        }
        resultsAnalysis.setOverallPerformance(overAllper);
        return resultsAnalysis;
    }


    @Transactional
    public OverallPerformance calculateAttemptPerformance(OverallPerformance overAllper, QuizSetAttemptResult attemptResult) {
        // add quizSet attempted
        overAllper.getMarksMatrics().getTotalQuizSets().add(1);
        // total q attempted + not attempted
        overAllper.getMarksMatrics().getTotalQ().add(attemptResult.getTotalAttemptedQuestions()+attemptResult.getTotalNotAttemptedQuestions());
        // total attempted q
        overAllper.getMarksMatrics().getTotalAttemptedQ().add(attemptResult.getTotalAttemptedQuestions());
        // total correct q attempted
        overAllper.getMarksMatrics().getTotalCorrectScore().add(attemptResult.getCorrectAnswers());
        // total incorrect q attempted
        overAllper.getMarksMatrics().getTotalIncorrectScore().add(attemptResult.getInCorrectAnswers());
        // set highest
        overAllper.getMarksMatrics().setHighestScore(Math.max(overAllper.getMarksMatrics().getHighestScore(), attemptResult.getTotalAttemptedQuestions()));
        // set lowest
        overAllper.getMarksMatrics().setLowestScore(Math.min(overAllper.getMarksMatrics().getLowestScore(), attemptResult.getTotalAttemptedQuestions()));
        // average so far for correct out of total q in quiz
        overAllper.getMarksMatrics().setAverageScore(Math.round((float) (overAllper.getMarksMatrics().getTotalCorrectScore().getTotal())*100
                /overAllper.getMarksMatrics().getTotalQ().getTotal()));
        // accuracy of total correct q out of total attempted
        overAllper.getMarksMatrics().setOverallAccuracy(Math.round((float) (overAllper.getMarksMatrics().getTotalCorrectScore().getTotal())*100
                / overAllper.getMarksMatrics().getTotalAttemptedQ().getTotal()));
        // subject score for correct q
        for(Map.Entry<String, Integer> mp : attemptResult.getSubjectScoresForCorrectAnswer().entrySet())
            overAllper.getSubjectMatrics().getSubjectScoresForCorrectAnswer().merge(mp.getKey(), mp.getValue(), Integer::sum);


        // subject category score for correct q
        for(Map.Entry<String, Integer> mp : attemptResult.getSubjectCategoryScoresForCorrectAnswer().entrySet()){
            overAllper.getSubjectMatrics().getSubjectCategoryScoresForCorrectAnswer().merge(mp.getKey(), mp.getValue(), Integer::sum);
        }
        // subject score for incorrect q
        for(Map.Entry<String, Integer> mp : attemptResult.getSubjectScoresForInCorrectAnswer().entrySet()){
            overAllper.getSubjectMatrics().getSubjectScoresForInCorrectAnswer().merge(mp.getKey(), mp.getValue(), Integer::sum);
        }
        // subject category score for incorrect q
        for(Map.Entry<String, Integer> mp : attemptResult.getSubjectCategoryScoresForInCorrectAnswer().entrySet()){
            overAllper.getSubjectMatrics().getSubjectCategoryScoresForInCorrectAnswer().merge(mp.getKey(), mp.getValue(), Integer::sum);
        }
        // subject score for not attempted q
        for(Map.Entry<String, Integer> mp : attemptResult.getSubjectScoresForNotAttemptedQ().entrySet()){
            overAllper.getSubjectMatrics().getSubjectScoresForNotAttemptedQ().merge(mp.getKey(), mp.getValue(), Integer::sum);
        }
        // subject category score for not attempted q
        for(Map.Entry<String, Integer> mp : attemptResult.getSubjectCategoryScoresNotAttemptedQ().entrySet()){
            overAllper.getSubjectMatrics().getSubjectCategoryScoresNotAttemptedQ().merge(mp.getKey(), mp.getValue(), Integer::sum);
        }
        overAllper.setAnalyzedAt(new Date());
        return  overAllper;

    }








}