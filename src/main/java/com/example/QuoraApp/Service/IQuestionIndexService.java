package com.example.QuoraApp.Service;

import com.example.QuoraApp.Models.Question;
import com.example.QuoraApp.Models.QuestionElasticDocument;

import java.util.List;

public interface IQuestionIndexService {
    void createQuestionIndex(Question question);
}
