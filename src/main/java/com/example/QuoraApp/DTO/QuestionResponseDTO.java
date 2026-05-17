package com.example.QuoraApp.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QuestionResponseDTO {
    private String id;
    private String title;
    private String content;
    private String authorId;
    private UserResponseDTO author;
    private Integer views;
    private Integer upVotes;
    private Integer downVotes;
    private Integer answersCount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<AnswerResponseDTO> answers;
}




