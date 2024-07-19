package com.isitcake.game.services;

import com.isitcake.game.entities.Question;

import java.util.List;

public interface QuestionService {
    Question saveQuestion(Question question);
    List<Question> getAllQuestions();
    Question getQuestionById(Long id);
}
