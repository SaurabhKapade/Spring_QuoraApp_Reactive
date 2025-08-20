package com.example.QuoraApp.Service;

import com.example.QuoraApp.DTO.AnswerRequestDTO;
import com.example.QuoraApp.DTO.AnswerResponseDTO;
import reactor.core.publisher.Mono;

public interface IAnswerService {
   public Mono<AnswerResponseDTO>createAnswer(AnswerRequestDTO answerRequestDTO);

   public Mono<AnswerResponseDTO>getAnswerById(String id);
}
