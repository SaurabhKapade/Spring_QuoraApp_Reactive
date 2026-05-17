package com.example.QuoraApp.Adapters;

import com.example.QuoraApp.DTO.AnswerResponseDTO;
import com.example.QuoraApp.Models.Answer;

public class AnswerAdapter {
    public static AnswerResponseDTO toAnswerResponseDTO(Answer answer) {
        return AnswerResponseDTO.builder()
                .id(answer.getId())
                .content(answer.getContent())
                .questionId(answer.getQuestionId())
                .userId(answer.getUserId())
                .createdAt(answer.getCreatedAt() == null ? null : answer.getCreatedAt().toString())
                .updatedAt(answer.getUpdatedAt() == null ? null : answer.getUpdatedAt().toString())
                .build();
    }
}

