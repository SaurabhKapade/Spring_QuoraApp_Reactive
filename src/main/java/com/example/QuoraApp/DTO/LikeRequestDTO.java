package com.example.QuoraApp.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LikeRequestDTO {

    @NotBlank(message = "Target Id is required")
    private String targetId;

    @NotBlank(message = "Target Type is required")
    private String targetType;

    @NotNull(message = "is Like is required")
    private boolean isLike;
}
