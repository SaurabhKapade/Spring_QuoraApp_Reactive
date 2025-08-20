package com.example.QuoraApp.Repositories;

import com.example.QuoraApp.Models.Like;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LikeRepository extends ReactiveMongoRepository<Like,String> {
}
