package com.sns.whisper.domain.user.presentation.request;

import com.sns.whisper.domain.user.application.dto.request.UserSignUpServiceRequest;
import com.sns.whisper.domain.user.presentation.request.validation.AllowedContentType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserSignUpRequest {

    @NotBlank(message = "회원 아이디는 필수 입력 사항입니다.")
    private String userId;

    @NotBlank(message = "비밀번호는 필수 입력 사항입니다.")
    private String password;

    @NotBlank(message = "이메일은 필수 입력 사항입니다.")
    private String email;

    @NotNull(message = "생년월일은 필수 입력 사항입니다.")
    private LocalDate birth;

    private String profileMessage;

    @AllowedContentType(allowedTypes = {"image/jpg", "image/jpeg", "image/png"},
            allowedExtensions = {"jpg", "jpeg", "png"})
    private MultipartFile profileImage;

    @Builder
    private UserSignUpRequest(String userId, String password, String email, LocalDate birth,
            String profileMessage, MultipartFile profileImage) {
        this.userId = userId;
        this.password = password;
        this.email = email;
        this.birth = birth;
        this.profileMessage = profileMessage;
        this.profileImage = profileImage;
    }


    public UserSignUpServiceRequest toServiceRequest(LocalDateTime joinedAt) {

        return UserSignUpServiceRequest.builder()
                                       .userId(userId)
                                       .password(password)
                                       .birth(birth)
                                       .profileImage(profileImage)
                                       .profileMessage(profileMessage)
                                       .joinedAt(joinedAt)
                                       .build();
    }
}
