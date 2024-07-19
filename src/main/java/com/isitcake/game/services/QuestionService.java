package com.isitcake.game.services;

import com.isitcake.game.entities.Question;

import java.util.List;

public interface QuestionService {
    List<Question> getAllQuestions();
    Question getQuestionById(Long id);
}
