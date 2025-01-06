package com.edu_backend.service;

import com.edu_backend.dto.QuestionDTO;
import com.edu_backend.model.QuizAttempt;
import com.edu_backend.model.QuizAttemptResult;
import com.edu_backend.mongo.MongoService;
import com.edu_backend.repository.QuizAttemptResultRepository;
import com.edu_backend.service.Interface.QuizAttemptResultService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;

@Service
public class QuizAttemptResultServiceImpl implements QuizAttemptResultService {

    @Autowired
    QuizAttemptResultRepository quizAttemptResultRepository;
    @Autowired
    QuestionService questionService;
    @Autowired
    private MongoService mongoService;

    @Override
    @Transactional
    public QuizAttemptResult saveQuizAttemptResult(String userId, String quizSetId,QuizAttemptResult.QuizSetAttemptResult quizResult ) {
       // fetch quizSetAttemptResult and update it in DB
        QuizAttemptResult quizSetAttemptResult = quizAttemptResultRepository.findByUserId(userId).orElse(null);
// Assign either the retrieved result or null to the variable
        if (quizSetAttemptResult!=null) {
            Optional<QuizAttemptResult.QuizSetResult> quizSetResult = quizSetAttemptResult.getQuizSetResult().stream()
                    .filter(qsr -> qsr.getQuizSetId().equals(quizSetId)).findFirst();
            if (quizSetResult.isPresent()) {
                QuizAttemptResult.QuizSetResult result = quizSetResult.get();
                result.getQuizSetAttemptResults().add(quizResult);
            } else {
                QuizAttemptResult.QuizSetResult newResult = new QuizAttemptResult.QuizSetResult(quizSetId);
                newResult.setQuizSetAttemptResults(new ArrayList<>(List.of(quizResult)));
                quizSetAttemptResult.getQuizSetResult().add(newResult);
            }

        } else {
            // Create new QuizSetAttemptResult document with the new QuizSet and attempt
            quizSetAttemptResult = new QuizAttemptResult();
            quizSetAttemptResult.setUserId(userId);
            QuizAttemptResult.QuizSetResult newResult = new QuizAttemptResult.QuizSetResult(quizSetId);
            newResult.setQuizSetAttemptResults(new ArrayList<>(List.of(quizResult)));
            quizSetAttemptResult.setQuizSetResult(new ArrayList<>(List.of(newResult)));
        }
        // Save the result
        quizAttemptResultRepository.save(quizSetAttemptResult);
        return quizSetAttemptResult;
    }



    @Transactional
    public QuizAttemptResult.QuizSetAttemptResult calculateResultOfQuizAttempt(String quizSetAttemptId, Optional<QuizAttempt.QuizSetAttempt> quizSetAttempt) {
        QuizAttemptResult.QuizSetAttemptResult quizSetAttemptResult = new QuizAttemptResult.QuizSetAttemptResult(quizSetAttemptId);
        // Initialize counters
        int totalQuestions = quizSetAttempt.get().getQuizSetAttempt().size();
        int totalAttemptedQuestions = 0;
        int correctAnswers = 0;
        int incorrectAnswers = 0;
        Map<String, Integer> subjectScoresForCorrectAns = new HashMap<>();
        Map<String, Integer> subjectCategoryScoresForCorrectAns = new HashMap<>();
        Map<String, Integer> subjectScoresForInCorrectAns = new HashMap<>();
        Map<String, Integer> subjectCategoryScoresForInForCorrectAns = new HashMap<>();
        Map<String, Integer> subjectScoresForNotAttemptedQ = new HashMap<>();
        Map<String, Integer> subjectCategoryScoresNotAttemptedQ = new HashMap<>();

        try {
            // Iterate through the quizSetAttempt's questions
            for (Map.Entry<String, List<String>> entry : quizSetAttempt.get().getQuizSetAttempt().entrySet()) {
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
            quizSetAttemptResult.setTimeSpent(quizSetAttempt.get().getTotalTimeTakenToAttempt());
            quizSetAttemptResult.setAnalyzedAt(new Date());

            System.out.println("Result calculated: " + quizSetAttemptResult);

            return quizSetAttemptResult;


        } catch (Exception e) {
            System.err.println("Error calculating quiz attempt result: " + e.getMessage());
            throw new RuntimeException("Result not calculated due to an error", e);
        }


    }
}