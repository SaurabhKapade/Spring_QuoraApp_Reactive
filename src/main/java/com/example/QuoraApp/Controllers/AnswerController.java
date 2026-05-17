package com.example.QuoraApp.Controllers;

import com.example.QuoraApp.DTO.AnswerRequestDTO;
import com.example.QuoraApp.DTO.AnswerResponseDTO;
import com.example.QuoraApp.Service.AnswerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/answers")
@RequiredArgsConstructor
public class AnswerController {

    private final AnswerService answerService;

    @PostMapping()
    public Mono<AnswerResponseDTO> createAnswer(@RequestBody AnswerRequestDTO answerRequestDTO) {
        System.out.println("req recived "+ answerRequestDTO.getContent());
        return answerService.createAnswer(answerRequestDTO);
    }

    @GetMapping("/{id}")
    public Mono<AnswerResponseDTO> getAnswerById(@PathVariable String id) {
        return answerService.getAnswerById(id);
    }

    @GetMapping("/question/{questionId}")
    public Flux<AnswerResponseDTO> getAnswersByQuestionId(@PathVariable String questionId) {
        return answerService.getAnswersByQuestionId(questionId);
    }
}

