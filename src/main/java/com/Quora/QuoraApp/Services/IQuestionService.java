package com.Quora.QuoraApp.Services;

import com.Quora.QuoraApp.DTO.QuestionRequestDTO;
import com.Quora.QuoraApp.DTO.QuestionResponseDTO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface IQuestionService {
    Mono<QuestionResponseDTO> createQuestion(QuestionRequestDTO questionRequestDTO);
    Mono<?> deleteQuestionById(String id);
    Mono<QuestionResponseDTO>getQuestionById(String id);
    Flux<QuestionResponseDTO>searchQuestion(String query,int page,int size);

    Flux<QuestionResponseDTO>getAllQuestions(String cursor,int size);
}
