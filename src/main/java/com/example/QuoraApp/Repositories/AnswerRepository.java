package com.example.QuoraApp.Repositories;

import com.example.QuoraApp.Models.Answer;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AnswerRepository extends ReactiveMongoRepository<Answer,String> {
}
