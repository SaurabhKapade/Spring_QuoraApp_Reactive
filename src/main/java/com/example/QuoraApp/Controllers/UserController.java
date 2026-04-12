package com.example.QuoraApp.Controllers;

import com.example.QuoraApp.DTO.FollowRequestDTO;
import com.example.QuoraApp.DTO.UserRequestDTO;
import com.example.QuoraApp.DTO.UserResponseDTO;
import com.example.QuoraApp.Models.User;
import com.example.QuoraApp.Service.IUserService;
import com.example.QuoraApp.Service.QuestionService;
import com.example.QuoraApp.Service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {
    private final IUserService userService;

    @PostMapping
    public Mono<UserResponseDTO> registerUser(@ModelAttribute UserRequestDTO userRequestDTO){
        System.out.println("Yayyyyy req aa gayi");
        return userService.registerUser(userRequestDTO);
    }

    @GetMapping("/ping")
    public String test(){
        return "pong";
    }

    @GetMapping("/{userId}")
    public Mono<UserResponseDTO> findUserById(@PathVariable String userId){
        return userService.getUserById(userId);
    }
    @GetMapping("/{userId}/followers")
    public Flux<UserResponseDTO> getFollowersByUserId(@PathVariable String userId){
        return userService.getFollowersByUserId(userId);
    }
    @GetMapping("/{userId}/followings")
    public Flux<UserResponseDTO> getFollowingsByUserId(@PathVariable String userId){
        return userService.getFollowingsByUserId(userId);
    }

    @PostMapping("/follow")
    public Mono<Void> followUser(@ModelAttribute FollowRequestDTO followRequestDTO){
        return userService.follow(followRequestDTO);
    }
    @PostMapping("/unFollow")
    public Mono<Void> unFollowUser(@ModelAttribute FollowRequestDTO followRequestDTO){
        return userService.unFollow(followRequestDTO);
    }

    @GetMapping("/all")
    public Flux<UserResponseDTO> getAllUsers(){
        return userService.getAllUsers();
    }

}
