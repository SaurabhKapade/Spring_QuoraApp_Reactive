package com.example.QuoraApp.Repositories;

import com.example.QuoraApp.Models.Answer;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface AnswerRepository extends ReactiveMongoRepository<Answer,String> {

    Flux<Answer> findAnswerByQuestionId(String questionId);
}
