package com.example.QuoraApp.Controllers;

import com.example.QuoraApp.DTO.QuestionResponseDTO;
import com.example.QuoraApp.Service.FeedService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/api/feed")
@RequiredArgsConstructor
public class FeedController {
    private final FeedService feedService;

    @GetMapping("/{userId}")
    public Flux<QuestionResponseDTO> getFeed(
            @PathVariable String userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return feedService.getFeed(userId, page, size);
    }
}
