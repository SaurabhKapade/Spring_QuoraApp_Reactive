package com.example.QuoraApp.Models;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Like {

    @Id
    private String id;

    private String targetId;

    private String targetType; //  QUESTION, ANSWER

    private boolean isLike;

    @CreatedDate
    private LocalDateTime createdAt;
}
