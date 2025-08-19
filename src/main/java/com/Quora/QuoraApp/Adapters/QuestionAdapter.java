package com.Quora.QuoraApp.Adapters;

import com.Quora.QuoraApp.DTO.QuestionResponseDTO;
import com.Quora.QuoraApp.Models.Question;

public class QuestionAdapter {
    public static QuestionResponseDTO toQuestionResponseDTO(Question question){
        return QuestionResponseDTO.builder()
                .id(question.getId())
                .title(question.getTitle())
                .content(question.getContent())
                .createdAt(question.getCreatedAt())
                .build();
    }
}
