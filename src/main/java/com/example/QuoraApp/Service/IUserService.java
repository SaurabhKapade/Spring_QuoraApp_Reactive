package com.example.QuoraApp.Service;

import com.example.QuoraApp.DTO.FollowRequestDTO;
import com.example.QuoraApp.DTO.UserRequestDTO;
import com.example.QuoraApp.DTO.UserResponseDTO;
import com.example.QuoraApp.Models.User;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface IUserService {
    Mono<UserResponseDTO> registerUser(UserRequestDTO userRequestDTO);
    Mono<UserResponseDTO> getUserById(String userId);
    Flux<UserResponseDTO> getAllUsers();
    Mono<Void> follow(FollowRequestDTO req);
    Flux<UserResponseDTO> getFollowersByUserId(String userId);
    Flux<UserResponseDTO> getFollowingsByUserId(String userId);
    Mono<Void> unFollow(FollowRequestDTO req);
}
