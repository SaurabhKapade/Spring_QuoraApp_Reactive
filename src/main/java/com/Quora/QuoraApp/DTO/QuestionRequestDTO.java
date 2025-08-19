package com.Quora.QuoraApp.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QuestionRequestDTO {
    @NotBlank(message = "title is required")
    @Size(min = 10, max = 100,message = "title must be in 10 to 100 characters")
    private String title;

    @NotBlank(message="content is required")
    @Size(min = 10,max = 1000,message = "content must be in 10 to 1000 characters")
    private String content;
}
