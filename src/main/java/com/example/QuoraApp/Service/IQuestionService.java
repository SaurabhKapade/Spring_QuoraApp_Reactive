package com.example.QuoraApp.Service;

import com.example.QuoraApp.DTO.QuestionRequestDTO;
import com.example.QuoraApp.DTO.QuestionResponseDTO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface IQuestionService {
    public Mono<QuestionResponseDTO> createQuestion(QuestionRequestDTO questionRequestDTO);
    public Mono<QuestionResponseDTO> getQuestionById(String id);
    public Flux<QuestionResponseDTO> getAllQuestions(String cursor,int size);
    public Mono<?> deleteQuestionById(String id);
    public Flux<QuestionResponseDTO> searchQuestion(String question,int page,int size);
}
