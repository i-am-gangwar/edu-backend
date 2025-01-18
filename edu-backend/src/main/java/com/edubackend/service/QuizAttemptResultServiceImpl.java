package com.edubackend.service;

import com.edubackend.Exceptions.Exception.OperationFailedException;
import com.edubackend.Exceptions.Exception.ResourceNotFoundException;
import com.edubackend.dto.QuestionDTO;
import com.edubackend.model.quizattempts.QuizAttempts;
import com.edubackend.model.quizattempts.QuizSet;
import com.edubackend.model.quizattempts.QuizSetAttempt;
import com.edubackend.model.quizresults.QuizResults;
import com.edubackend.model.quizresults.QuizSetAttemptResult;
import com.edubackend.model.quizresults.QuizSetResult;
import com.edubackend.repository.QuizAttemptResultRepository;
import com.edubackend.repository.QuizAttemptsRepository;
import com.edubackend.service.interfaces.QuizAttemptResultService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;

@Service
@Slf4j
public class QuizAttemptResultServiceImpl implements QuizAttemptResultService {


  private final QuizAttemptResultRepository quizAttemptResultRepository;
  private final QuestionService questionService;
  private final QuizAttemptsServiceImpl quizAttemptsService;
  private final QuizAttemptsRepository quizAttemptsRepository;

    @Autowired
    public QuizAttemptResultServiceImpl(QuizAttemptResultRepository quizAttemptResultRepository,
                                        QuestionService questionService,
                                        QuizAttemptsServiceImpl quizAttemptsService,
                                        QuizAttemptsRepository quizAttemptsRepository) {
        this.quizAttemptResultRepository = quizAttemptResultRepository;
        this.questionService = questionService;
        this.quizAttemptsService = quizAttemptsService;
        this.quizAttemptsRepository = quizAttemptsRepository;

    }

    @Override
    public QuizResults createQuizResult(String userId, String quizSetId, QuizSetAttemptResult quizResult){
        QuizResults quizSetAttemptResult = new QuizResults();
        quizSetAttemptResult.setUserId(userId);
        QuizSetResult newResult = new QuizSetResult();
        newResult.setQuizSetId(quizSetId);
        newResult.setQuizSetAttemptResults(new ArrayList<>(List.of(quizResult)));
        quizSetAttemptResult.setQuizSetResults(new ArrayList<>(List.of(newResult)));
        return quizSetAttemptResult;
    }

    @Override
    public QuizSetResult createQuizSetResult(String quizSetId,QuizSetAttemptResult quizSetAttemptResult){
        QuizSetResult newQuizSetResult = new QuizSetResult();
        newQuizSetResult.setQuizSetId(quizSetId);
        newQuizSetResult.setQuizSetAttemptResults(new ArrayList<>(List.of(quizSetAttemptResult)));
        return newQuizSetResult;

    }



    @Override
    @Transactional
    public QuizSetAttemptResult saveQuizAttemptResult(String userId, String quizSetId, String quizSetAttemptId) {

        try{
            QuizSetAttempt quizSetAttempt =  quizAttemptsService.getResultByUserIdAndSetIdAndSetAttemptId(userId,quizSetId,quizSetAttemptId);
            QuizSetAttemptResult quizSetAttemptResult = calculateResultOfQuizAttempt(quizSetAttemptId,quizSetAttempt);
            QuizResults quizResults = quizAttemptResultRepository.findByUserId(userId);

            if (quizResults!=null) {
                Optional<QuizSetResult> quizSetResult = quizResults.getQuizSetResults().stream()
                        .filter(qsr -> qsr.getQuizSetId().equals(quizSetId)).findFirst();

                if (quizSetResult.isPresent())
                    quizSetResult.get().getQuizSetAttemptResults().add(quizSetAttemptResult);
                else
                    quizResults.getQuizSetResults().add(createQuizSetResult(quizSetId,quizSetAttemptResult));
            }
            else
                quizResults = createQuizResult(userId,quizSetId,quizSetAttemptResult);

            quizAttemptResultRepository.save(quizResults);

            return quizSetAttemptResult;

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
            String errorMessage = "An unexpected error occurred while creating quiz attempt result. try again!";
            log.error(errorMessage, e);
            throw new RuntimeException(errorMessage, e);
        }



    }


    @Transactional
    public QuizResults saveAllQuizAttemptResult(String userId) {

        try{

            QuizAttempts quizAttempts = quizAttemptsRepository.findByUserId(userId);
            QuizResults quizResults = new QuizResults();

            if (quizAttempts!=null) {
                quizResults.setUserId(userId);
                for(QuizSet quizSet : quizAttempts.getQuizSets()) {

                    QuizSetResult quizSetResult = new QuizSetResult();
                    quizSetResult.setQuizSetId(quizSet.getQuizSetId());

                    for (QuizSetAttempt quizSetAttempt : quizSet.getQuizSetAttempts()) {
                        QuizSetAttemptResult quizSetAttemptResult = calculateResultOfQuizAttempt(quizSetAttempt.getQuizSetAttemptId(), quizSetAttempt);
                        quizSetResult.getQuizSetAttemptResults().add(quizSetAttemptResult);
                    }
                    quizResults.getQuizSetResults().add(quizSetResult);
                }
                QuizResults savedResults = quizAttemptResultRepository.findByUserId(userId);
                if(savedResults!=null)
                    quizResults.setId(savedResults.getId());

                quizAttemptResultRepository.save(quizResults);
                return quizResults;
            }
            else throw new NullPointerException("No quiz Attempts found for result calculation, userId: "+ userId);

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
            String errorMessage = "An unexpected error occurred while creating quiz attempt result. try again!";
            log.error(errorMessage, e);
            throw new RuntimeException(errorMessage, e);
        }



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








    public QuizResults getResultByUserId(String userId){
        QuizResults quizResults = quizAttemptResultRepository.findByUserId(userId);
        return checkData(quizResults);

    }

    public QuizSetResult getResultByUserIdAndQuizSetId(String userId, String quizSetId){
        QuizSetResult quizSetResult = quizAttemptResultRepository.findQuizSetAttemptResultsByUserIdAndQuizSetId(userId,quizSetId);
        if(quizSetResult.getQuizSetAttemptResults().isEmpty())
            throw  new ResourceNotFoundException(String.format(
                    "User Quiz Attempt Not Saved. userId: %s, quizSetId: %s",
                    userId, quizSetId));
        quizSetResult = checkData(quizSetResult);
        quizSetResult.setQuizSetId(quizSetId);
        return quizSetResult;
    }


    public QuizSetAttemptResult getResultByUserIdAndQuizSetIdAndSetAttemptId(String userId, String quizSetId, String quizSetAttemptId) throws Exception {
        try {
            QuizSetResult results = quizAttemptResultRepository.findQuizSetAttemptResultsByUserIdAndQuizSetId(userId, quizSetId);
            Optional<QuizSetAttemptResult> quizSetAttemptResult = results.getQuizSetAttemptResults().stream()
                    .filter(qs -> qs.getQuizSetAttemptId().equals(quizSetAttemptId)).findFirst();
            System.out.println(quizSetAttemptResult);
            if (quizSetAttemptResult.isPresent())
                return checkData(quizSetAttemptResult.get());
            else
                throw new ResourceNotFoundException(String.format(
                        "User Quiz Attempt result Not found. userId: %s, quizSetId: %s, Attempt Details: %s",
                        userId, quizSetId, quizSetAttemptId));
        }
        catch (ResourceNotFoundException ex){
            throw new ResourceNotFoundException(String.format(
                    "User Quiz Attempt result Not found. userId: %s, quizSetId: %s, Attempt Details: %s",
                    userId, quizSetId, quizSetAttemptId));

        }

    }



    public <T> T checkData(T result){

        try {
            if (result==null) {
                throw new OperationFailedException(
                        "User data is not found in db please enter valid details");
            }
            return result;
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
        catch (ResourceNotFoundException ex){
            throw new ResourceNotFoundException("An error occurred while retrieving user results. Please try again.");
        }

    }


}

