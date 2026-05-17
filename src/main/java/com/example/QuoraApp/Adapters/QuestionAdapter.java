package com.example.QuoraApp.Adapters;

import com.example.QuoraApp.DTO.CreateQuestionDTO;
import com.example.QuoraApp.DTO.QuestionResponseDTO;
import com.example.QuoraApp.Models.Question;

public class QuestionAdapter {
    public static QuestionResponseDTO toQuestionResponseDTO(Question question){
//        Flux<Answer> answers = (question.getId());
        return QuestionResponseDTO.builder()
                .id(question.getId())
                .title(question.getTitle())
                .content(question.getContent())
                .authorId(question.getUserId())
                .views(question.getViews())
                .upVotes(question.getUpVotes())
                .downVotes(question.getDownVotes())
                .answersCount(question.getAnswersCount())
//                .answers(answers)
                .createdAt(question.getCreatedAt())
                .updatedAt(question.getUpdatedAt())
                .build();
    }
    public static CreateQuestionDTO toCreateQuestionDTO(Question question){
        return CreateQuestionDTO.builder()
                .id(question.getId())
                .title(question.getTitle())
                .content(question.getContent())
                .createdAt(question.getCreatedAt())
                .build();
    }
}
