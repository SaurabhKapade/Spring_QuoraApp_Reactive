package com.example.QuoraApp.Events;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.cglib.core.Local;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ViewCountEvent {

    private String targetId;

    private String targetType;

    private LocalDateTime timestamps;

}
