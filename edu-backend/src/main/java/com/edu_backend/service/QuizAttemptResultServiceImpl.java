package com.edu_backend.service;

import com.edu_backend.dto.QuestionDTO;
import com.edu_backend.model.QuizAttempt;
import com.edu_backend.model.QuizAttemptResult;
import com.edu_backend.mongo.MongoService;
import com.edu_backend.repository.QuizAttemptRepository;
import com.edu_backend.repository.QuizAttemptResultAnalysisRepository;
import com.edu_backend.repository.QuizAttemptResultRepository;
import com.edu_backend.service.Interface.QuizAttemptResultService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;

@Service
public class QuizAttemptResultServiceImpl implements QuizAttemptResultService {

    @Autowired
    QuizAttemptRepository quizAttemptRepository;
    @Autowired
    QuizAttemptResultRepository quizAttemptResultRepository;

    @Autowired
   QuizAttemptResultAnalysisRepository quizAttemptResultAnalysisRepository;

    @Autowired
    QuestionService questionService;
    @Autowired
    private MongoService mongoService;

    @Override
    @Transactional
    public boolean calculateQuizAttemptResult(String userId,String quizSetId,String quizSetAttemptId) {
        // fetch quizsetattempt
        Optional<QuizAttempt.QuizSetAttempt> quizSetAttempt = mongoService.findQuizSetAttempt(userId, quizSetId, quizSetAttemptId);
        if (quizSetAttempt.isPresent()) {
            // Create a new result object
            QuizAttemptResult quizAttemptResult = new QuizAttemptResult();
            quizAttemptResult.setUserId(userId);
            quizAttemptResult.setQuizAttemptId(quizSetAttempt.get().getQuizSetAttemptId());
            quizAttemptResult.setQuizSetId(quizSetId);
            quizAttemptResult.setAnalyzedAt(new Date());
            // Initialize counters
            int totalQuestions = quizSetAttempt.get().getQuizSetAttempt().size();
            int totalAttemptedQuestions = 0;
            int correctAnswers = 0;
            int incorrectAnswers = 0;
            Map<String, Integer> subjectWiseScores = new HashMap<>();
            Map<String, Integer> subjectWiseCategoryScores = new HashMap<>();
            try {
                // Iterate through the quizSetAttempt's questions
                for (Map.Entry<String, List<String>> entry : quizSetAttempt.get().getQuizSetAttempt().entrySet()) {
                    String questionId = entry.getKey();
                    List<String> selectedAnswers = entry.getValue();
                    // Fetch the question details
                    Optional<QuestionDTO> questionDTO = questionService.getQuestionById(questionId);
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
                            subjectWiseScores.merge(question.getSubjectId(), 1, Integer::sum);
                            subjectWiseCategoryScores.merge(question.getCategory(), 1, Integer::sum);
                        } else {
                            incorrectAnswers++;
                        }
                    }
                }
                // Calculate and set final metrics
                quizAttemptResult.setTotalAttemptedQuestions(totalAttemptedQuestions);
                quizAttemptResult.setTotalNotAttemptedQuestions(totalQuestions - totalAttemptedQuestions);
                quizAttemptResult.setCorrectAnswers(correctAnswers);
                quizAttemptResult.setInCorrectAnswers(incorrectAnswers);
                quizAttemptResult.setAccuracy((correctAnswers * 100.0) / totalAttemptedQuestions);
                quizAttemptResult.setSubjectWiseScores(subjectWiseScores);
                quizAttemptResult.setSubjectWiseCategoryScores(subjectWiseCategoryScores);
                // Save the result
                   quizAttemptResultRepository.save(quizAttemptResult);
                   System.out.println("Result calculated: "+ quizAttemptResult);
            } catch (Exception e) {
                System.err.println("Error calculating quiz attempt result: " + e.getMessage());
                throw new RuntimeException("Result not calculated due to an error", e);
            }
            return true;
        } else {
            return false;
        }
    }

}