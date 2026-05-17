package com.example.QuoraApp.Service;

import com.example.QuoraApp.Adapters.AnswerAdapter;
import com.example.QuoraApp.Adapters.UserAdapter;
import com.example.QuoraApp.DTO.AnswerRequestDTO;
import com.example.QuoraApp.DTO.AnswerResponseDTO;
import com.example.QuoraApp.Models.Answer;
import com.example.QuoraApp.Repositories.AnswerRepository;
import com.example.QuoraApp.Repositories.QuestionRepository;
import com.example.QuoraApp.Repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AnswerService implements IAnswerService {

    private final AnswerRepository answerRepository;
    private final UserRepository userRepository;
    private final QuestionRepository questionRepository;

    @Override
    public Mono<AnswerResponseDTO> createAnswer(AnswerRequestDTO answerRequestDTO) {
        return userRepository.findById(answerRequestDTO.getUserId())
                .switchIfEmpty(Mono.error(new IllegalArgumentException("User not found")))
                .then(questionRepository.findById(answerRequestDTO.getQuestionId()))
                .switchIfEmpty(Mono.error(new IllegalArgumentException("Question not found")))
                .flatMap(q -> {
                    Answer answer = Answer.builder()
                            .content(answerRequestDTO.getContent())
                            .questionId(answerRequestDTO.getQuestionId())
                            .userId(answerRequestDTO.getUserId())
                            .upVotes(0)
                            .downVotes(0)
                            .createdAt(LocalDateTime.now())
                            .updatedAt(LocalDateTime.now())
                            .build();

                    return answerRepository.save(answer)
                            .flatMap(saved ->
                                    questionRepository.incrementAnswersCount(saved.getQuestionId())
                                            .thenReturn(saved)
                            );
                })
                .flatMap(saved -> userRepository.findById(saved.getUserId())
                        .map(UserAdapter::toUserResponseDTO)
                        .switchIfEmpty(Mono.error(new IllegalArgumentException("User not found")))
                        .map(userDto -> {
                            AnswerResponseDTO dto = AnswerAdapter.toAnswerResponseDTO(saved);
                            dto.setUser(userDto);
                            return dto;
                        })
                );
    }

    @Override
    public Mono<AnswerResponseDTO> getAnswerById(String id) {
        return answerRepository.findById(id)
                .switchIfEmpty(Mono.error(new IllegalArgumentException("Answer not found")))
                .flatMap(answer -> userRepository.findById(answer.getUserId())
                        .map(UserAdapter::toUserResponseDTO)
                        .switchIfEmpty(Mono.error(new IllegalArgumentException("User not found")))
                        .map(userDto -> {
                            AnswerResponseDTO dto = AnswerAdapter.toAnswerResponseDTO(answer);
                            dto.setUser(userDto);
                            return dto;
                        })
                );
    }

    public Flux<AnswerResponseDTO> getAnswersByQuestionId(String questionId) {
        return answerRepository.findAnswerByQuestionId(questionId)
                .flatMap(answer -> userRepository.findById(answer.getUserId())
                        .map(UserAdapter::toUserResponseDTO)
                        .map(userDto -> {
                            AnswerResponseDTO dto = AnswerAdapter.toAnswerResponseDTO(answer);
                            dto.setUser(userDto);
                            return dto;
                        })
                );
    }
}

