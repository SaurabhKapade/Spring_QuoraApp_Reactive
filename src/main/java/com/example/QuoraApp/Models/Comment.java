package com.example.QuoraApp.Models;


import com.example.QuoraApp.Enums.TargetTypeEnums;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collation = "comments")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Comment {
    @Id
    private String id;

    private String userId;

    private TargetTypeEnums commentType;

    private String targetId;

    private String content;

    @CreatedDate
    private LocalDateTime createdAt;
}
