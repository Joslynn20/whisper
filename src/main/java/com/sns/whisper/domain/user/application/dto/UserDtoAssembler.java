package com.sns.whisper.domain.user.application.dto;

import static java.util.stream.Collectors.toList;

import com.sns.whisper.domain.user.application.dto.response.UserSearchServiceResponse;
import com.sns.whisper.domain.user.domain.User;
import java.util.List;

public class UserDtoAssembler {

    public static List<UserSearchServiceResponse> UserSearchResponse(List<User> users) {
        return users.stream()
                    .map(user -> UserSearchServiceResponse.builder()
                                                          .userId(user.getUserId())
                                                          .profileImage(user.getProfileImage())
                                                          .following(null)
                                                          .build())
                    .collect(toList());
    }

    public static List<UserSearchServiceResponse> UserSearchResponse(List<User> users,
            User loginUser) {
        return users.stream()
                    .map(user -> UserSearchServiceResponse.builder()
                                                          .userId(user.getUserId())
                                                          .profileImage(user.getProfileImage())
                                                          .following(loginUser.isFollowing(user))
                                                          .build())
                    .collect(toList());
    }


}
