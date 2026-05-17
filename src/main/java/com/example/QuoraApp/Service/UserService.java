package com.example.QuoraApp.Service;

import com.example.QuoraApp.Adapters.UserAdapter;
import com.example.QuoraApp.DTO.FollowRequestDTO;
import com.example.QuoraApp.DTO.UserRequestDTO;
import com.example.QuoraApp.DTO.UserResponseDTO;
import com.example.QuoraApp.Models.Follow;
import com.example.QuoraApp.Models.User;
import com.example.QuoraApp.Repositories.FollowRepository;
import com.example.QuoraApp.Repositories.QuestionRepository;
import com.example.QuoraApp.Repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UserService  implements ReactiveUserDetailsService {
    private final UserRepository userRepository;
    private final FollowRepository followRepository;
    private final QuestionRepository questionRepository;
    private final PasswordEncoder passwordEncoder;

    public Mono<UserResponseDTO> registerUser(UserRequestDTO userRequestDTO) {
        return userRepository.existsByUserName(userRequestDTO.getUserName())
                .flatMap(exists ->{
                    if(exists){
                        return Mono.error(new IllegalArgumentException("UserName already exists"));
                    }
                    return userRepository.existsByEmail(userRequestDTO.getEmail());
                })
                .flatMap(exists->{
                    if(exists){
                        return Mono.error(new IllegalArgumentException("Email already exists"));
                    }

                    User user = User.builder()
                            .userName(userRequestDTO.getUserName())
                            .email(userRequestDTO.getEmail())
                            .password(passwordEncoder.encode(userRequestDTO.getPassword()))
                            .followersCount(0)
                            .followingsCount(0)
                            .bio("You can chang this")
                            .createdAt(LocalDateTime.now())
                            .updatedAt(LocalDateTime.now())
                            .build();

                    return userRepository.save(user);
                }).map(UserAdapter::toUserResponseDTO)
                .doOnSuccess(msg->System.out.println("user Registered successfully"))
                .doOnError(msg->System.out.println("Cant register user"));
    }

    public Mono<UserResponseDTO> getUserById(String id){
        return userRepository.findById(id)
                .map(UserAdapter::toUserResponseDTO)
                .flatMap(userDto ->
                        questionRepository.findByUserIdOrderByCreatedAtDesc(id)
                                .map(com.example.QuoraApp.Adapters.QuestionAdapter::toQuestionResponseDTO)
                                .collectList()
                                .map(questions -> {
                                    userDto.setQuestions(questions);
                                    return userDto;
                                })
                )
                .doOnError(error-> System.out.println("cant find User"));
    }


    public Flux<UserResponseDTO> getAllUsers() {
        return userRepository.findAll()
                .map(UserAdapter::toUserResponseDTO)
                .doOnError(error->System.out.println("error while getting users"));
    }

    public Mono<Void> follow(FollowRequestDTO req) {

        if (req.getFollowerId().equals(req.getFolloweeId())) {
            return Mono.error(new IllegalArgumentException("User cannot follow themselves"));
        }

        Follow follow = Follow.builder()
                .followerId(req.getFollowerId())
                .followeeId(req.getFolloweeId())
                .build();

        return followRepository.save(follow)
                .flatMap(saved ->
                        Mono.when(
                                userRepository.incrementFollowers(req.getFollowerId()),
                                userRepository.incrementFollowings(req.getFolloweeId())
                        )
                )
                .onErrorResume(e -> {
                    if (e.getMessage().contains("duplicate key")) {
                        return Mono.empty(); // already followed → DO NOTHING
                    }
                    return Mono.error(e);
                })
                .then();
    }


    public Flux<UserResponseDTO> getFollowersByUserId(String userId) {
        return followRepository.findByFolloweeId(userId)
                .flatMap(follow->userRepository.findById(follow.getFollowerId()))
                .map(UserAdapter::toUserResponseDTO)
                .doOnError(error-> System.out.println("error while fetching followers "+ error));
    }


    public Flux<UserResponseDTO> getFollowingsByUserId(String userId) {
        return followRepository.findByFollowerId(userId)
                .flatMap(follow->userRepository.findById(follow.getFolloweeId()))
                .map(UserAdapter::toUserResponseDTO)
                .doOnError(error->System.out.println("error while fetching follwings" + error));
    }


    public Mono<Void> unFollow(FollowRequestDTO req) {
        String follower = req.getFollowerId();
        String followee = req.getFolloweeId();

        System.out.println(follower+"->"+followee);

        return followRepository.findByFollowerIdAndFolloweeId(follower, followee)
                .switchIfEmpty(Mono.error(new RuntimeException("Follow relation not found")))
                .flatMap(edge ->
                        followRepository.delete(edge)
                                .then(userRepository.findById(follower)
                                        .switchIfEmpty(Mono.error(new RuntimeException("Follower not found")))
                                        .flatMap(user -> {
                                            int count = user.getFollowingsCount() == null ? 0 : user.getFollowingsCount();
                                            user.setFollowingsCount(Math.max(0, count - 1));
                                            return userRepository.save(user);
                                        })
                                )
                                .then(userRepository.findById(followee)
                                        .switchIfEmpty(Mono.error(new RuntimeException("Followee not found")))
                                        .flatMap(user -> {
                                            int count = user.getFollowersCount() == null ? 0 : user.getFollowersCount();
                                            user.setFollowersCount(Math.max(0, count - 1));
                                            return userRepository.save(user);
                                        })
                                )
                )
                .doOnError(e -> System.out.println("ERROR: " + e.getMessage()))
                .then();
    }


    @Override
    public Mono<UserDetails> findByUsername(String email)  {
        return userRepository.findByEmail(email)
                .switchIfEmpty(Mono.error(new UsernameNotFoundException("user not found")))
                .map(user-> org.springframework.security.core.userdetails.User.builder()
                        .username(user.getEmail())
                        .password(user.getPassword())
                        .authorities("USER")
                        .build()
                );
    }
}
