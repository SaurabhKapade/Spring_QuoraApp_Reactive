package com.example.QuoraApp.Repositories;

import com.example.QuoraApp.DTO.UserResponseDTO;
import com.example.QuoraApp.Models.User;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.data.mongodb.repository.Update;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface UserRepository extends ReactiveMongoRepository<User,String> {
    Mono<Boolean> existsByUserName(String userName);
    Mono<Boolean> existsByEmail(String email);
    Mono<User> findByEmail(String email);

    @Query("{ '_id': ?0 }")
    @Update("{ '$inc': { 'followersCount': 1 } }")
    Mono<Void> incrementFollowers(String userId);

    @Query("{ '_id': ?0 }")
    @Update("{ '$inc': { 'followingsCount': 1 } }")
    Mono<Void> incrementFollowings(String userId);

    @Query("{ '_id': ?0 }")
    @Update("{ '$inc': { 'followersCount': -1 } }")
    Mono<Void> decrementFollowers(String userId);

    @Query("{ '_id': ?0 }")
    @Update("{ '$inc': { 'followingsCount': -1 } }")
    Mono<Void> decrementFollowings(String userId);
}
