package com.example.QuoraApp.Service;

import com.example.QuoraApp.Models.Question;
import com.example.QuoraApp.Models.QuestionElasticDocument;
import com.example.QuoraApp.Repositories.QuestionDocumentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class QuestionIndexService implements IQuestionIndexService{
    private final QuestionDocumentRepository questionDocumentRepository;
    @Override
    public void createQuestionIndex(Question question) {
        QuestionElasticDocument document =  QuestionElasticDocument
                .builder()
                .id(question.getId())
                .title(question.getTitle())
                .content(question.getContent())
                .build();
        questionDocumentRepository.save(document);
    }


}
