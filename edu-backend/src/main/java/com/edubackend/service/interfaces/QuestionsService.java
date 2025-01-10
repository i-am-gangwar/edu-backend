package com.edubackend.service.interfaces;

import com.edubackend.dto.QuestionDTO;
import com.edubackend.model.Questions;

import java.util.List;

public interface QuestionsService {
      List<Questions> getAllQuestions();
}
