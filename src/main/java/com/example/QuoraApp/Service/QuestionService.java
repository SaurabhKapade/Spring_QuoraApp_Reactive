package com.example.QuoraApp.Service;
import co.elastic.clients.elasticsearch._types.query_dsl.QueryBuilders;
import com.example.QuoraApp.Adapters.QuestionAdapter;
import com.example.QuoraApp.Adapters.AnswerAdapter;
import com.example.QuoraApp.DTO.CreateQuestionDTO;
import com.example.QuoraApp.DTO.QuestionRequestDTO;
import com.example.QuoraApp.DTO.QuestionResponseDTO;
import com.example.QuoraApp.Events.ViewCountEvent;
import com.example.QuoraApp.Models.Question;
import com.example.QuoraApp.Models.QuestionElasticDocument;
import com.example.QuoraApp.Producers.KafkaEventProducer;
import com.example.QuoraApp.Repositories.AnswerRepository;
import com.example.QuoraApp.Repositories.QuestionRepository;
import com.example.QuoraApp.Repositories.UserRepository;
import com.example.QuoraApp.Utils.CursorUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.client.elc.NativeQueryBuilder;
import org.springframework.data.elasticsearch.core.ReactiveElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class QuestionService implements IQuestionService{

    private final QuestionRepository questionRepository;
    private final UserRepository userRepository;
    private final AnswerRepository answerRepository;
    private final KafkaEventProducer kafkaEventProducer;
    private final IQuestionIndexService questionIndexService;
    private final ReactiveElasticsearchOperations elasticsearchOperations;
    private final FeedService feedService;
    @Override
    public Mono<CreateQuestionDTO> createQuestion(QuestionRequestDTO questionRequestDTO) {
        Question question = Question.builder()
                .title(questionRequestDTO.getTitle())
                .content(questionRequestDTO.getContent())
                .userId(questionRequestDTO.getUserId())
                .views(0)
                .upVotes(0)
                .downVotes(0)
                .answersCount(0)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        return questionRepository.save(question)
                .flatMap(savedQuestion ->
                        feedService.fanOutQuestion(savedQuestion)
                                .then(Mono.fromCallable(() -> {
                                    questionIndexService.createQuestionIndex(savedQuestion);
                                    return QuestionAdapter.toCreateQuestionDTO(savedQuestion);
                                }))
                )
                .doOnSuccess(res -> System.out.println("Question created successfully " + res))
                .doOnError(err -> System.out.println("Error: " + err));
    }

    @Override
    public Mono<QuestionResponseDTO> getQuestionById(String id) {
        return questionRepository.findById(id)
                .flatMap(question -> {
                    QuestionResponseDTO dto = QuestionAdapter.toQuestionResponseDTO(question);
                    Mono<java.util.List<com.example.QuoraApp.DTO.AnswerResponseDTO>> answersMono =
                            answerRepository.findAnswerByQuestionId(question.getId())
                                    .flatMap(answer ->
                                            userRepository.findById(answer.getUserId())
                                                    .map(com.example.QuoraApp.Adapters.UserAdapter::toUserResponseDTO)
                                                    .switchIfEmpty(Mono.error(new IllegalArgumentException("User not found")))
                                                    .map(userDto -> {
                                                        var ansDto = AnswerAdapter.toAnswerResponseDTO(answer);
                                                        ansDto.setUser(userDto);
                                                        return ansDto;
                                                    })
                                    )
                                    .collectList();

                    Mono<com.example.QuoraApp.DTO.UserResponseDTO> authorMono =
                            userRepository.findById(question.getUserId())
                                    .map(com.example.QuoraApp.Adapters.UserAdapter::toUserResponseDTO)
                                    .switchIfEmpty(Mono.error(new IllegalArgumentException("User not found")));

                    return Mono.zip(authorMono, answersMono)
                            .map(tuple -> {
                                dto.setAuthor(tuple.getT1());
                                dto.setAnswers(tuple.getT2());
                                return dto;
                            });
                })
                .doOnError(response->System.out.println("Error while fetching question "+response))
                .doOnSuccess(response->{
                    ViewCountEvent viewCountEvent = new ViewCountEvent(id,"question",LocalDateTime.now());
                    kafkaEventProducer.publishViewCountEvent(viewCountEvent);
                });
    }

    @Override
    public Flux<QuestionResponseDTO> getAllQuestions(String cursor, int size) {
        Pageable pageable = PageRequest.of(0,size);

        if(!CursorUtils.isValidCursor(cursor)){
            return questionRepository.findTop100ByOrderByCreatedAtAsc()
                    .map(QuestionAdapter::toQuestionResponseDTO)
                    .doOnError(error -> System.out.println("Error fetching questions: " + error))
                    .doOnComplete(() -> System.out.println("Questions fetched successfully"));
        }else{
            LocalDateTime cursorTimeStamp = CursorUtils.parseCursor(cursor);
            return questionRepository.findByCreatedAtGreaterThanOrderByCreatedAtAsc(cursorTimeStamp,pageable)
                    .map(QuestionAdapter::toQuestionResponseDTO)
                    .doOnError(error -> System.out.println("Error fetching questions: " + error))
                    .doOnComplete(() -> System.out.println("Questions fetched successfully"));
        }

    }

    @Override
    public Mono<?> deleteQuestionById(String id) {
         return questionRepository.deleteById(id)
                .doOnSuccess(response->System.out.println("Question deleted Successfully"+id))
                .doOnError(response->System.out.println("Cant delete question with given id"+id));

    }

    @Override
    public Flux<QuestionResponseDTO> searchQuestion(String question, int page, int size) {
        return questionRepository.findByTitleOrContentContainingIgnoreCase(question, PageRequest.of(page,size))
                .map(QuestionAdapter::toQuestionResponseDTO)
                .doOnError(error -> System.out.println("Error searching questions: " + error))
                .doOnComplete(() -> System.out.println("Questions searched successfully"));
    }

    @Override
    public Flux<QuestionElasticDocument> searchQuestion(String query) {

        var searchQuery = new NativeQueryBuilder()
                .withQuery(QueryBuilders.multiMatch(m -> m
                        .query(query)
                        .fields("title^3", "content")
                        .fuzziness("AUTO")
                        .minimumShouldMatch("60%")
                ))
                .build();

        return elasticsearchOperations
                .search(searchQuery, QuestionElasticDocument.class)
                .map(SearchHit::getContent);
    }
}
