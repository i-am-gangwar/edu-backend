package com.edubackend.service;

import com.edubackend.dto.QuestionDTO;
import com.edubackend.model.quizattempts.QuizSetAttempt;
import com.edubackend.model.quizresults.QuizResults;
import com.edubackend.model.quizresults.QuizSetAttemptResult;
import com.edubackend.model.quizresults.QuizSetResult;
import com.edubackend.repository.QuizAttemptResultRepository;
import com.edubackend.service.interfaces.QuizAttemptResultService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;

@Service
public class QuizAttemptResultServiceImpl implements QuizAttemptResultService {


  private final   QuizAttemptResultRepository quizAttemptResultRepository;
  private final QuestionService questionService;

    @Autowired
    public QuizAttemptResultServiceImpl(QuizAttemptResultRepository quizAttemptResultRepository, QuestionService questionService) {
        this.quizAttemptResultRepository = quizAttemptResultRepository;
        this.questionService = questionService;
    }

    public QuizResults createQuizAttemptResult(String userId, String quizSetId, QuizSetAttemptResult quizResult){
        QuizResults quizSetAttemptResult = new QuizResults();
        quizSetAttemptResult.setUserId(userId);
        QuizSetResult newResult = new QuizSetResult();
        newResult.setQuizSetId(quizSetId);
        newResult.setQuizSetAttemptResults(new ArrayList<>(List.of(quizResult)));
        quizSetAttemptResult.setQuizSetResult(new ArrayList<>(List.of(newResult)));
        return quizSetAttemptResult;
    }

    public QuizSetResult createQuizSetResult(String quizSetId,QuizSetAttemptResult quizSetAttemptResult){
        QuizSetResult newQuizSetResult = new QuizSetResult();
        newQuizSetResult.setQuizSetId(quizSetId);
        newQuizSetResult.setQuizSetAttemptResults(new ArrayList<>(List.of(quizSetAttemptResult)));
        return newQuizSetResult;

    }


    @Override
    @Transactional
    public QuizResults saveQuizAttemptResult(String userId, String quizSetId, QuizSetAttemptResult quizResult ) {

        QuizResults quizSetAttemptResult = quizAttemptResultRepository.findByUserId(userId);

        if (quizSetAttemptResult!=null) {
            Optional<QuizSetResult> quizSetResult = quizSetAttemptResult.getQuizSetResult().stream()
                    .filter(qsr -> qsr.getQuizSetId().equals(quizSetId)).findFirst();

            if (quizSetResult.isPresent())
                   quizSetResult.get().getQuizSetAttemptResults().add(quizResult);
            else
                quizSetAttemptResult.getQuizSetResult().add(createQuizSetResult(quizSetId,quizResult));
        }
        else
            quizSetAttemptResult = createQuizAttemptResult(userId,quizSetId,quizResult);

        quizAttemptResultRepository.save(quizSetAttemptResult);
        return quizSetAttemptResult;
    }



    @Transactional
    public QuizSetAttemptResult calculateResultOfQuizAttempt(String quizSetAttemptId, QuizSetAttempt quizSetAttempt) {

        QuizSetAttemptResult quizSetAttemptResult = new QuizSetAttemptResult();
        quizSetAttemptResult.setQuizSetAttemptId(quizSetAttemptId);

        // Initialize counters
        int totalQuestions = quizSetAttempt.getSetAttempt().size();
        int totalAttemptedQuestions = 0;
        int correctAnswers = 0;
        int incorrectAnswers = 0;
        Map<String, Integer> subjectScoresForCorrectAns = new HashMap<>();
        Map<String, Integer> subjectCategoryScoresForCorrectAns = new HashMap<>();
        Map<String, Integer> subjectScoresForInCorrectAns = new HashMap<>();
        Map<String, Integer> subjectCategoryScoresForInForCorrectAns = new HashMap<>();
        Map<String, Integer> subjectScoresForNotAttemptedQ = new HashMap<>();
        Map<String, Integer> subjectCategoryScoresNotAttemptedQ = new HashMap<>();

        try {   // Iterate through the quizSetAttempt's questions
            for (Map.Entry<String, List<String>> entry : quizSetAttempt.getSetAttempt().entrySet()) {
                String questionId = entry.getKey();
                List<String> selectedAnswers = entry.getValue();
                Optional<QuestionDTO> questionDTO = questionService.getQuestionById(questionId);   // Fetch the question details
                if (questionDTO.isEmpty()) {
                    System.err.println("Question ID: " + questionId + " not found in the database");
                    continue;
                }
                QuestionDTO question = questionDTO.get();
                List<String> correctOptionIds = question.getOptions().stream()
                        .filter(QuestionDTO.Option::isCorrect)
                        .map(QuestionDTO.Option::getId)
                        .toList();
                // Check if the question was attempted
                if (!selectedAnswers.isEmpty()) {
                    totalAttemptedQuestions++;
                    // Determine correctness
                    if (selectedAnswers.size() == correctOptionIds.size() &&
                            new HashSet<>(selectedAnswers).containsAll(correctOptionIds)) {
                        correctAnswers++;
                        subjectScoresForCorrectAns.merge(question.getSubjectId(), 1, Integer::sum);
                        subjectCategoryScoresForCorrectAns.merge(question.getCategory(), 1, Integer::sum);
                    } else {
                        incorrectAnswers++;
                        subjectScoresForInCorrectAns.merge(question.getSubjectId(), 1, Integer::sum);
                        subjectCategoryScoresForInForCorrectAns.merge(question.getCategory(), 1, Integer::sum);
                    }
                }
                else{
                    subjectScoresForNotAttemptedQ.merge(question.getSubjectId(), 1, Integer::sum);
                    subjectCategoryScoresNotAttemptedQ.merge(question.getCategory(), 1, Integer::sum);

                }
            }
            // Calculate and set final metrics
            quizSetAttemptResult.setTotalAttemptedQuestions(totalAttemptedQuestions);
            quizSetAttemptResult.setTotalNotAttemptedQuestions(totalQuestions - totalAttemptedQuestions);
            quizSetAttemptResult.setCorrectAnswers(correctAnswers);
            quizSetAttemptResult.setInCorrectAnswers(incorrectAnswers);
            quizSetAttemptResult.setAccuracy((correctAnswers * 100.0) / totalAttemptedQuestions);
            // correct subject and their category details
            quizSetAttemptResult.setSubjectScoresForCorrectAnswer(subjectScoresForCorrectAns);
            quizSetAttemptResult.setSubjectCategoryScoresForCorrectAnswer(subjectCategoryScoresForCorrectAns);
            // incorrect subject and their category details
            quizSetAttemptResult.setSubjectScoresForInCorrectAnswer(subjectScoresForInCorrectAns);
            quizSetAttemptResult.setSubjectCategoryScoresForInCorrectAnswer(subjectCategoryScoresForInForCorrectAns);
            // Not attempted questions subject and their category details
            quizSetAttemptResult.setSubjectScoresForNotAttemptedQ(subjectScoresForNotAttemptedQ);
            quizSetAttemptResult.setSubjectCategoryScoresNotAttemptedQ(subjectCategoryScoresNotAttemptedQ);
            quizSetAttemptResult.setTimeSpent(quizSetAttempt.getTotalTimeTakenToAttempt());
            quizSetAttemptResult.setAnalyzedAt(new Date());
            return quizSetAttemptResult;

        }
        catch (Exception e) {
            throw new RuntimeException("Result not calculated due to an error", e);
        }
    }
}

