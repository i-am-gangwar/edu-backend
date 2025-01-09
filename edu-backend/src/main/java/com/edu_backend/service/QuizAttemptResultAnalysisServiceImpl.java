package com.edu_backend.service;

import com.edu_backend.model.QuizAnanlysis.OverallPerformance;
import com.edu_backend.model.QuizAnanlysis.ResultsAnalysis;
import com.edu_backend.model.QuizResults.QuizResults;
import com.edu_backend.model.QuizResults.QuizSetAttemptResult;
import com.edu_backend.model.QuizResults.QuizSetResult;
import com.edu_backend.repository.QuizAttemptResultRepository;
import com.edu_backend.repository.QuizResultAnalysisRepository;
import com.edu_backend.service.Interface.QuizAttemptResultAnalysisService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.*;


@Service
@AllArgsConstructor
public class QuizAttemptResultAnalysisServiceImpl implements QuizAttemptResultAnalysisService {


    @Autowired
    QuizAttemptResultRepository  quizAttemptResultRepository;
    @Autowired
    QuizResultAnalysisRepository quizResultAnalysisRepository;

    @Override
    public ResultsAnalysis calculateAnalysis(String userId) {
        QuizResults userResult = quizAttemptResultRepository.findByUserId(userId);
        ResultsAnalysis resultsAnalysis = calculatePerformance(userResult);
        quizResultAnalysisRepository.save(resultsAnalysis);
        return resultsAnalysis;
    }

    @Override
    public ResultsAnalysis calculateAnalysis(String userId, QuizSetResult quizSetResults) {

        return null;
    }

    public ResultsAnalysis createAnalysis(QuizResults userResult){
        ResultsAnalysis resultsAnalysis = calculatePerformance(userResult);
        quizResultAnalysisRepository.save(resultsAnalysis);
        return resultsAnalysis;
    }


    public ResultsAnalysis updateAnalysis(QuizResults userResult, ResultsAnalysis quizResultsAnalysis) {
        // need to get to know whihc quiz set attempt not calculated
       // ResultsAnalysis resultsAnalysis = calculateResultAnalysis(userResult,quizResultsAnalysis);
       // quizResultAnalysisRepository.save(quizResultsAnalysis);
        return  null;
    }





    public ResultsAnalysis calculatePerformance(QuizResults userResult){

        ResultsAnalysis resultsAnalysis = new ResultsAnalysis();
        resultsAnalysis.setUserId(userResult.getUserId());
        System.out.println("resultsAnalysis object:" + resultsAnalysis);
        OverallPerformance overAllper = new OverallPerformance();
        System.out.println("resultsAnalysis object:" + resultsAnalysis);

        List<QuizSetResult> quizSetResult = userResult.getQuizSetResult();

         for(QuizSetResult qzSet: quizSetResult){

             overAllper.getMarksMatrics().getTotalQuizSets().add(qzSet.getQuizSetAttemptResults().size());  // add total attempted set of this quiz set

             for (QuizSetAttemptResult attemptResult: qzSet.getQuizSetAttemptResults()){

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

                 overAllper.getMarksMatrics().setAverageScore(Math.round((float) (overAllper.getMarksMatrics().getTotalCorrectScore().getTotal())*100 /overAllper.getMarksMatrics().getTotalQ().getTotal()));
                // accuracy of total correct q out of total attempted
                 overAllper.getMarksMatrics().setOverallAccuracy(Math.round((float) (overAllper.getMarksMatrics().getTotalCorrectScore().getTotal())*100 /
                         overAllper.getMarksMatrics().getTotalAttemptedQ().getTotal()));


                 // total time for per q
//                 overAllper.setTotalTimeTaken(overAllper.getTotalTimeTaken()+ Integer.parseInt( attemptResult.getTimeSpent()));
//                 // average time for per q
//                 overAllper.setAverageTimePerQuestion(Math.round((float) (overAllper.getTotalCorrectScore()) /overAllper.getTotalQ()));

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

             }

         }
         resultsAnalysis.setOverallPerformance(overAllper);

        return resultsAnalysis;
    }
}