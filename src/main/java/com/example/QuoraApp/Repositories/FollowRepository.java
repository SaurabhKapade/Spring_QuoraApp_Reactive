package com.example.QuoraApp.Repositories;

import com.example.QuoraApp.DTO.UserResponseDTO;
import com.example.QuoraApp.Models.Follow;
import com.example.QuoraApp.Models.User;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface FollowRepository extends ReactiveMongoRepository<Follow,String> {
    Mono<Follow> findByFollowerIdAndFolloweeId(String followerId,String followeeId);
    Flux<Follow> findByFolloweeId(String followeeId);
    Flux<Follow> findByFollowerId(String followerId);
}
