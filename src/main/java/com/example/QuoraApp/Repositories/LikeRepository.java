package com.example.QuoraApp.Repositories;

import com.example.QuoraApp.Models.Vote;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LikeRepository extends ReactiveMongoRepository<Vote,String> {
}
