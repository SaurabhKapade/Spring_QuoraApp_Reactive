package com.example.QuoraApp.Service;

import com.example.QuoraApp.DTO.LikeRequestDTO;
import com.example.QuoraApp.DTO.LikeResponseDTO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ILikeService {
     Mono<LikeResponseDTO>creatLike(LikeRequestDTO likeRequestDTO);

     Mono<LikeResponseDTO>countLikesByTargetIdAndTargetType(String targetId,String targetType);

    Mono<LikeResponseDTO>countDisLikesByTargetIdAndTargetType(String targetId,String targetType);
}
