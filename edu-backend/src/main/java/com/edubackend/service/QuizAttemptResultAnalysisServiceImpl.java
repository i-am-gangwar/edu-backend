package com.edubackend.service;

import com.edubackend.Exceptions.Exception.OperationFailedException;
import com.edubackend.model.quizananlysis.MarksMatrics;
import com.edubackend.model.quizananlysis.OverallPerformance;
import com.edubackend.model.quizananlysis.ResultsAnalysis;
import com.edubackend.model.quizananlysis.SubjectMatrics;
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
        overAllper.setMarksMatrics(calculateMarksMatrics(overAllper.getMarksMatrics(),attemptResult));
        overAllper.setSubjectMatrics(calculateSubjectMatrics(overAllper.getSubjectMatrics(),attemptResult));
        overAllper.setAnalyzedAt(new Date());
        return  overAllper;

    }




  public MarksMatrics calculateMarksMatrics(MarksMatrics marksMatrics, QuizSetAttemptResult attemptResult){
          // average time spent per quiz till now
          double totalTime = marksMatrics.getAverageTimePerQuiz()*(marksMatrics.getTotalQuizSets().getTotal());
          // add quizSet attempted
          marksMatrics.getTotalQuizSets().add(1);
          // total q attempted + not attempted
          marksMatrics.getTotalQ().add(attemptResult.getTotalAttemptedQuestions()+attemptResult.getTotalNotAttemptedQuestions());
          // total attempted q
          marksMatrics.getTotalAttemptedQ().add(attemptResult.getTotalAttemptedQuestions());
          // total correct q attempted
          marksMatrics.getTotalCorrectScore().add(attemptResult.getCorrectAnswers());
          // total incorrect q attempted
          marksMatrics.getTotalIncorrectScore().add(attemptResult.getInCorrectAnswers());
          // set highest score of quiz
          marksMatrics.setHighestScore(Math.max(marksMatrics.getHighestScore(), attemptResult.getCorrectAnswers()));
          // set lowest score of quiz attempt
          marksMatrics.setLowestScore(Math.min(marksMatrics.getLowestScore(), attemptResult.getCorrectAnswers()));
          // average so far for correct out of total attempted questions in quizes
          marksMatrics.setAverageScore(Math.round((float) (marksMatrics.getTotalCorrectScore().getTotal())*100
                  /marksMatrics.getTotalQ().getTotal()));
          // accuracy of total correct q out of total attempted
          marksMatrics.setOverallAccuracy(Math.round((float) (marksMatrics.getTotalCorrectScore().getTotal())*100
                  / marksMatrics.getTotalAttemptedQ().getTotal()));

          totalTime = totalTime + Double.parseDouble( attemptResult.getTimeSpent());
          marksMatrics.setAverageTimePerQuiz(totalTime/marksMatrics.getTotalQuizSets().getTotal());
          System.out.println("av.:"+ marksMatrics.getAverageTimePerQuiz());

   return  marksMatrics;
  }


  public SubjectMatrics calculateSubjectMatrics(SubjectMatrics subjectMatrics,QuizSetAttemptResult attemptResult){

      // subject score for correct q
      for(Map.Entry<String, Integer> mp : attemptResult.getSubjectScoresForCorrectAnswer().entrySet())
          subjectMatrics.getSubjectScoresForCorrectAnswer().merge(mp.getKey(), mp.getValue(), Integer::sum);


      // subject category score for correct q
      for(Map.Entry<String, Integer> mp : attemptResult.getSubjectCategoryScoresForCorrectAnswer().entrySet()){
          subjectMatrics.getSubjectCategoryScoresForCorrectAnswer().merge(mp.getKey(), mp.getValue(), Integer::sum);
      }
      // subject score for incorrect q
      for(Map.Entry<String, Integer> mp : attemptResult.getSubjectScoresForInCorrectAnswer().entrySet()){
          subjectMatrics.getSubjectScoresForInCorrectAnswer().merge(mp.getKey(), mp.getValue(), Integer::sum);
      }
      // subject category score for incorrect q
      for(Map.Entry<String, Integer> mp : attemptResult.getSubjectCategoryScoresForInCorrectAnswer().entrySet()){
          subjectMatrics.getSubjectCategoryScoresForInCorrectAnswer().merge(mp.getKey(), mp.getValue(), Integer::sum);
      }
      // subject score for not attempted q
      for(Map.Entry<String, Integer> mp : attemptResult.getSubjectScoresForNotAttemptedQ().entrySet()){
          subjectMatrics.getSubjectScoresForNotAttemptedQ().merge(mp.getKey(), mp.getValue(), Integer::sum);
      }
      // subject category score for not attempted q
      for(Map.Entry<String, Integer> mp : attemptResult.getSubjectCategoryScoresNotAttemptedQ().entrySet()){
          subjectMatrics.getSubjectCategoryScoresNotAttemptedQ().merge(mp.getKey(), mp.getValue(), Integer::sum);
      }

    return subjectMatrics;

  }





}