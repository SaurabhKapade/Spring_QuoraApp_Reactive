package com.example.QuoraApp.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LikeResponseDTO {

    private String id;

    private String targetId;

    private String targetType;

    private boolean isLike;

    private LocalDateTime createdAt;
}
