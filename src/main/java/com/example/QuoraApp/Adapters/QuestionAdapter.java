package com.example.QuoraApp.Adapters;

import com.example.QuoraApp.DTO.QuestionResponseDTO;
import com.example.QuoraApp.Models.Question;

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
