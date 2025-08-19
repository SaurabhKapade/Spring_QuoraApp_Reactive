package com.Quora.QuoraApp.Services;

import com.Quora.QuoraApp.Adapters.QuestionAdapter;
import com.Quora.QuoraApp.DTO.QuestionRequestDTO;
import com.Quora.QuoraApp.DTO.QuestionResponseDTO;
import com.Quora.QuoraApp.Models.Question;
import com.Quora.QuoraApp.Repository.QuestionRepository;
import com.Quora.QuoraApp.Utils.CursorUtil;
import jakarta.validation.constraints.Null;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class QuestionService implements IQuestionService{
    private final QuestionRepository questionRepository;
    @Override
    public Mono<QuestionResponseDTO> createQuestion(QuestionRequestDTO questionRequestDTO) {
        Question question = Question.builder()
                .title(questionRequestDTO.getTitle())
                .content(questionRequestDTO.getContent())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        return questionRepository.save(question)
                .map(QuestionAdapter::toQuestionResponseDTO)
                .doOnSuccess(response->System.out.println("Question created successfulluy "+response))
                .doOnError(response-> System.out.println("Error while creating Question " +response));
    }

    @Override
    public Mono<?> deleteQuestionById(String id) {
        return questionRepository.deleteById(id)
                .doOnSuccess(response->System.out.println("Question deleted Successfully"+id))
                .doOnError(response->System.out.println("Cant delete question with given id"+id));
    }

    @Override
    public Mono<QuestionResponseDTO> getQuestionById(String id) {
        return questionRepository.findById(id)
                .map(QuestionAdapter::toQuestionResponseDTO)
                .doOnSuccess(response->System.out.println("Question fetched Successfully" + response))
                .doOnError(error->System.out.println("Error while fetching question "+error));
    }

    @Override
    public Flux<QuestionResponseDTO> searchQuestion(String query, int page, int size) {
        return questionRepository.findByTitleOrContentContainingIgnoreCase(query, PageRequest.of(page,size))
                .map(QuestionAdapter::toQuestionResponseDTO)
                .doOnError(error -> System.out.println("Error searching questions: " + error))
                .doOnComplete(() -> System.out.println("Questions searched successfully"));
    }

    @Override
    public Flux<QuestionResponseDTO> getAllQuestions(String cursor, int size) {
        Pageable pageable = PageRequest.of(0,size);

        if(!CursorUtil.isValidCursor(cursor)) {
            return questionRepository.findTop10ByOrderByCreatedAtAsc()
                    .map(QuestionAdapter::toQuestionResponseDTO)
                    .doOnError(error -> System.out.println("Error fetching questions: " + error))
                    .doOnComplete(() -> System.out.println("Questions fetched successfully"));
        }else{
            LocalDateTime cursorTimeStamp = CursorUtil.parseCursor(cursor);
            return questionRepository.findByCreatedAtGreaterThanOrderByCreatedAtAsc(cursorTimeStamp,pageable)
                    .map(QuestionAdapter::toQuestionResponseDTO)
                    .doOnError(error -> System.out.println("Error fetching questions: " + error))
                    .doOnComplete(() -> System.out.println("Questions fetched successfully"));
        }

    }


}
