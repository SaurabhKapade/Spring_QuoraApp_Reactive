package com.example.QuoraApp.Controllers;

import com.example.QuoraApp.Config.Security.JwtUtil;
import com.example.QuoraApp.DTO.FollowRequestDTO;
import com.example.QuoraApp.DTO.LoginRequestDTO;
import com.example.QuoraApp.DTO.UserRequestDTO;
import com.example.QuoraApp.DTO.UserResponseDTO;
import com.example.QuoraApp.Models.User;
import com.example.QuoraApp.Service.QuestionService;
import com.example.QuoraApp.Service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {
    private final  UserService userService;
    private final ReactiveAuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    @PostMapping("/register")
    public Mono<UserResponseDTO> registerUser(@ModelAttribute UserRequestDTO userRequestDTO){
        System.out.println("Yayyyyy req aa gayi");
        return userService.registerUser(userRequestDTO);
    }

    @PostMapping("/login")
    public Mono<?> loginUser(@ModelAttribute LoginRequestDTO loginRequestDTO){
        Authentication authToken = new UsernamePasswordAuthenticationToken(
                loginRequestDTO.getEmail(),
                loginRequestDTO.getPassword()
        );
        return authenticationManager.authenticate(authToken)
                .map(authentication -> {
                    UserDetails userDetails = (UserDetails) authentication.getPrincipal();
                    String token = jwtUtil.generateToken(userDetails.getUsername());

                    ResponseCookie cookie = ResponseCookie.from("authToken",token)
                            .httpOnly(true)
                            .secure(false)
                            .path("/")
                            .maxAge(Duration.ofDays(2))
                            .build();

                    System.out.println("cookie is "+ cookie.toString());
                    return ResponseEntity.ok()
                            .header(HttpHeaders.SET_COOKIE, cookie.toString())
                            .body(token);
                });
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
