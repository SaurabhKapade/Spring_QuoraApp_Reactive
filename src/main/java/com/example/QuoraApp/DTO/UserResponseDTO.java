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
public class UserResponseDTO {
    public String id;
    public String userName;
    public String email;
    public String password;
    public Integer followersCount;
    public Integer followingsCount;
    public LocalDateTime createdAt;
    public List<QuestionResponseDTO> questions;

}
