package com.example.QuoraApp.Models;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Question {
    @Id
    private String id;

    @NotBlank(message="Title is Required")
    @Size(min = 10,max=100,message="Title must be between 10 to 100 characters")
    private String title;

    @NotBlank(message="Content is Required")
    @Size(min = 10,max=1000,message="Content must be between 10 to 1000 characters")
    private String content;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;


}
