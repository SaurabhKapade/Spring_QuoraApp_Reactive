package com.example.QuoraApp.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QuestionRequestDTO {

    @NotBlank(message="Title is Required")
    @Size(min=10,max=100,message="Title must be between 10 to 100 characters")
    private String title;

    @NotBlank(message="Content is Required")
    @Size(min=10,max=1000,message = "Content must be between 10 to 1000 characters")
    private String content;

    private List<String> tags;
}
