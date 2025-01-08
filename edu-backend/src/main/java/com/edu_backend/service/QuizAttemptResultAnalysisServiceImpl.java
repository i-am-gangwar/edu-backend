package com.edu_backend.service;

import com.edu_backend.model.QuizResultsAnalysis;
import com.edu_backend.model.QuizAttemptResult;
import com.edu_backend.repository.QuizAttemptResultRepository;
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



    public QuizResultsAnalysis createResultAnalysis(QuizAttemptResult userResult) {
        QuizResultsAnalysis quizResultsAnalysis = new QuizResultsAnalysis();
        quizResultsAnalysis.setOverallPerformance(new QuizResultsAnalysis.OverallPerformance());
        quizResultsAnalysis.setUserId(userResult.getUserId());
        QuizResultsAnalysis resultsAnalysis = calculateResultAnalysis(userResult,quizResultsAnalysis);
        quizResultAnalysisRepository.save(quizResultsAnalysis);
        return  resultsAnalysis;
    }


    public QuizResultsAnalysis updateResultAnalysis(QuizAttemptResult userResult,QuizResultsAnalysis quizResultsAnalysis) {
        QuizResultsAnalysis resultsAnalysis = calculateResultAnalysis(userResult,quizResultsAnalysis);
        quizResultAnalysisRepository.save(quizResultsAnalysis);
        return  resultsAnalysis;
    }



    @Override
    public QuizResultsAnalysis calculateResultAnalysis(QuizAttemptResult userResult, QuizResultsAnalysis quizResultsAnalysis){
         List<QuizAttemptResult.QuizSetResult> quizSetResult = userResult.getQuizSetResult();
         QuizResultsAnalysis.OverallPerformance overAllper =quizResultsAnalysis.getOverallPerformance();
         System.out.println("Perform: "+ overAllper);



         for(QuizAttemptResult.QuizSetResult qzSet: quizSetResult){
             // add total attempted set of this quiz set
             overAllper.setTotalQuizSets(overAllper.getTotalQuizSets()+ qzSet.getQuizSetAttemptResults().size());
             for (QuizAttemptResult.QuizSetAttemptResult attemptResult: qzSet.getQuizSetAttemptResults()){


                 // total q attempted + not attempted
                 overAllper.setTotalQ(overAllper.getTotalQ()+ attemptResult.getTotalAttemptedQuestions()+attemptResult.getTotalNotAttemptedQuestions());
                 // total attempted q
                 overAllper.setTotalAttemptedQ(overAllper.getTotalAttemptedQ()+ attemptResult.getTotalAttemptedQuestions());
                 // total correct q attempted
                 overAllper.setTotalCorrectScore(overAllper.getTotalCorrectScore()+ attemptResult.getCorrectAnswers());
                 // total incorrect q attempted
                 overAllper.setTotalIncorrectScore(overAllper.getTotalIncorrectScore()+ attemptResult.getInCorrectAnswers());
                 // set highest
                 overAllper.setHighestScore(Math.max(overAllper.getHighestScore(), attemptResult.getTotalAttemptedQuestions()));
                 // set lowest
                 overAllper.setLowestScore(Math.min(overAllper.getLowestScore(), attemptResult.getTotalAttemptedQuestions()));
                 // average so far for correct out of total q in quiz
                 overAllper.setAverageScore(Math.round((float) (overAllper.getTotalCorrectScore())*100 /overAllper.getTotalQ()));
                // accuracy of total correct q out of total attempted
                 overAllper.setOverallAccuracy(Math.round((float) (overAllper.getTotalCorrectScore())*100 /overAllper.getTotalAttemptedQ()));



                 // total time for per q
//                 overAllper.setTotalTimeTaken(overAllper.getTotalTimeTaken()+ Integer.parseInt( attemptResult.getTimeSpent()));
//                 // average time for per q
//                 overAllper.setAverageTimePerQuestion(Math.round((float) (overAllper.getTotalCorrectScore()) /overAllper.getTotalQ()));

                // subject score for correct q
                 System.out.println(attemptResult.getSubjectScoresForCorrectAnswer().entrySet());
                 System.out.println(overAllper.getSubjectScoresForCorrectAnswer());
                 for(Map.Entry<String, Integer> mp : attemptResult.getSubjectScoresForCorrectAnswer().entrySet())
                         overAllper.getSubjectScoresForCorrectAnswer().merge(mp.getKey(), mp.getValue(), Integer::sum);


                 // subject category score for correct q
                 for(Map.Entry<String, Integer> mp : attemptResult.getSubjectCategoryScoresForCorrectAnswer().entrySet()){
                     overAllper.getSubjectCategoryScoresForCorrectAnswer().merge(mp.getKey(), mp.getValue(), Integer::sum);
                 }
                 // subject score for incorrect q
                 for(Map.Entry<String, Integer> mp : attemptResult.getSubjectScoresForInCorrectAnswer().entrySet()){
                     overAllper.getSubjectScoresForInCorrectAnswer().merge(mp.getKey(), mp.getValue(), Integer::sum);
                 }
                 // subject category score for incorrect q
                 for(Map.Entry<String, Integer> mp : attemptResult.getSubjectCategoryScoresForInCorrectAnswer().entrySet()){
                     overAllper.getSubjectCategoryScoresForInCorrectAnswer().merge(mp.getKey(), mp.getValue(), Integer::sum);
                 }
                 // subject score for not attempted q
                 for(Map.Entry<String, Integer> mp : attemptResult.getSubjectScoresForNotAttemptedQ().entrySet()){
                     overAllper.getSubjectScoresForNotAttemptedQ().merge(mp.getKey(), mp.getValue(), Integer::sum);
                 }
                 // subject category score for not attempted q
                 for(Map.Entry<String, Integer> mp : attemptResult.getSubjectCategoryScoresNotAttemptedQ().entrySet()){
                     overAllper.getSubjectCategoryScoresNotAttemptedQ().merge(mp.getKey(), mp.getValue(), Integer::sum);
                 }
                 overAllper.setAnalyzedAt(new Date());

             }

         }
         quizResultsAnalysis.setOverallPerformance(overAllper);
        return quizResultsAnalysis;
    }
}