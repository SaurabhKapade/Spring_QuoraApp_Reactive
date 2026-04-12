package com.example.QuoraApp.Models;


import com.example.QuoraApp.Enums.TargetTypeEnums;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "votes")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Vote {

    @Id
    private String id;

    private String userId;

    @Indexed
    private String targetId;

    private TargetTypeEnums targetType;

    private Integer voteType;

    @CreatedDate
    private LocalDateTime createdAt;
}
