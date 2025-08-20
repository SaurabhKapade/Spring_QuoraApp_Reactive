package com.example.QuoraApp.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AnswerResponseDTO {

    private String id;

    private String content;

    private String questionId;

    private String createdAt;

    private String updatedAt;
}
