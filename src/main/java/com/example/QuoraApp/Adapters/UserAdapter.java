package com.example.QuoraApp.Adapters;

import com.example.QuoraApp.DTO.UserResponseDTO;
import com.example.QuoraApp.Models.User;

public class UserAdapter {
    public static UserResponseDTO toUserResponseDTO(User user){
        return UserResponseDTO.builder()
                .id(user.getId())
                .userName(user.getUserName())
                .email(user.getEmail())
                .followingsCount(user.getFollowingsCount())
                .followersCount(user.getFollowersCount())
                .createdAt(user.getCreatedAt())
                .build();
    }
}
