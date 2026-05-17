package com.example.QuoraApp.Service;


import com.example.QuoraApp.Adapters.QuestionAdapter;
import com.example.QuoraApp.DTO.QuestionResponseDTO;
import com.example.QuoraApp.DTO.UserResponseDTO;
import com.example.QuoraApp.Models.Question;
import com.example.QuoraApp.Repositories.QuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Range;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class FeedService {
    private final UserService userService;
    private final QuestionRepository questionRepository;
    private final ReactiveRedisTemplate<String,String> redisTemplate;
    public Mono<Void> fanOutQuestion(Question question){
        String authorId = question.getUserId();
        String postId = question.getId();
        double timeStamp = System.currentTimeMillis();

        return userService.getFollowersByUserId(authorId)
                .map(UserResponseDTO::getId)
                .flatMap(userId ->
                    redisTemplate.opsForZSet()
                            .add("feed:"+userId,postId,timeStamp)
                ).then();

    }


    public Flux<QuestionResponseDTO> getFeed(String userId, int page, int size){
        long start = (long) page * size;
        long end = start + size -1;
        return redisTemplate.opsForZSet()
                .reverseRange("feed:"+userId, Range.closed(start,end))
                .collectList()
                .flatMapMany(postIds->{
                    if(postIds.isEmpty()){
                        return generateFeedFromDB(userId,page,size);// generate feed from db
                    }
                    return Flux.fromIterable(postIds)
                            .concatMap(questionRepository::findById)
                            .map(QuestionAdapter::toQuestionResponseDTO);
                });
    }

    public Flux<QuestionResponseDTO> generateFeedFromDB(String userId,int page,int size){
        long start = (long) page * size;
        long end = start + size -1;
        String key = "feed:"+userId;

        return redisTemplate.opsForZSet()
                .reverseRange(key,Range.closed(start,end))
                .collectList()
                .flatMapMany(postIds->{
                    if(!postIds.isEmpty()){
                        return questionRepository.findAllById(postIds)
                                .collectMap(Question::getId)
                                .flatMapMany(map->
                                    Flux.fromIterable(postIds)
                                            .map(map::get)
                                            .filter(Objects::nonNull)
                                            .map(QuestionAdapter::toQuestionResponseDTO)

                                );
                    }

                    return questionRepository
                            .findAllByOrderByUpVotesDescCreatedAtDesc(PageRequest.of(page, size))
                            .map(QuestionAdapter::toQuestionResponseDTO);
                });
    }

}
