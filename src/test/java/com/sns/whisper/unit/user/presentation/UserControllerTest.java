package com.sns.whisper.unit.user.presentation;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.sns.whisper.domain.user.application.dto.request.UserSignUpServiceRequest;
import com.sns.whisper.unit.ControllerTest;
import java.time.LocalDate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.http.HttpMethod;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

public class UserControllerTest extends ControllerTest {

    @Test
    @DisplayName("유효한 회원 정보는 신규 회원가입을 할 수 있다.")
    void signUp_ValidUser_Success() throws Exception {
        //given
        MultiValueMap<String, String> params = getParams();

        MockMultipartFile profileImage = new MockMultipartFile("profileImage",
                "profileImage.png", "image/png", "profileImage".getBytes());

        //when, then
        mockMvc.perform(multipart(HttpMethod.POST, "/api/users").file(profileImage)
                                                                .params(params)
               )
               .andDo(print())
               .andExpect(status().isCreated())
               .andExpect(jsonPath("$.message").value("회원가입에 성공했습니다."));

        verify(userService).signUp(any(UserSignUpServiceRequest.class));
    }

    @ParameterizedTest
    @ValueSource(strings = {"profileImage.exe", "profileImage.pdf", "profileImage.dll"})
    @DisplayName("유효하지 않은 파일 형식는 회원 프로필로 등록할 수 없다.")
    void signUp_NotValidFile_400Exception(String file) throws Exception {
        //given
        MultiValueMap<String, String> params = getParams();

        MockMultipartFile profileImage = new MockMultipartFile("profileImage",
                file, "image/png", "profileImage.exe".getBytes());

        //when, then
        mockMvc.perform(multipart(HttpMethod.POST, "/api/users").file(profileImage)
                                                                .params(params)
               )
               .andDo(print())
               .andExpect(status().isBadRequest());

        verify(userService, never()).signUp(any(UserSignUpServiceRequest.class));
    }

    @ParameterizedTest
    @ValueSource(strings = {"image/gif", "image/tiff", "image/x-MS-bmp"})
    @DisplayName("유효하지 않은 이미지 형식은 회원 프로필로 등록할 수 없다.")
    void signUp_NotValidContentType_400Exception(String contentType) throws Exception {
        //given
        MultiValueMap<String, String> params = getParams();

        MockMultipartFile profileImage = new MockMultipartFile("profileImage",
                "profileImage.png", contentType, "profileImage.exe".getBytes());

        //when, then
        mockMvc.perform(multipart(HttpMethod.POST, "/api/users").file(profileImage)
                                                                .params(params)
               )
               .andDo(print())
               .andExpect(status().isBadRequest());

        verify(userService, never()).signUp(any(UserSignUpServiceRequest.class));

    }

    @Test
    @DisplayName("유효한 회원 정보를 입력하면, 로그인 할 수 있다.")
    void login_ValidUser_Success() throws Exception {
        //given
        String userId = "userId1234";
        String password = "password1234";

        //when, then
        mockMvc.perform(post("/api/users/login").param("userId", userId)
                                                .param("password", password))
               .andDo(print())
               .andExpect(status().isOk());

        verify(userService).login(anyString(), anyString());
    }


    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"", " "})
    @DisplayName("로그인 요청 시 유효한 회원 아이디를 입력하지 않으면, 예외가 발생한다.")
    void login_NotValidUserId_ExceptionThrown(String userId) throws Exception {
        //given
        String password = "password1234";

        //when, then
        mockMvc.perform(post("/api/users/login").param("userId", userId)
                                                .param("password", password))
               .andDo(print())
               .andExpect(status().isBadRequest());

        verify(userService, never()).login(anyString(), anyString());
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"", " "})
    @DisplayName("로그인 요청 시 유효한 비밀번호를 입력하지 않으면, 예외가 발생한다.")
    void login_NotValidPassword_ExceptionThrown(String password) throws Exception {
        //given
        String userId = "user1234";

        //when, then
        mockMvc.perform(post("/api/users/login").param("userId", userId)
                                                .param("password", password))
               .andDo(print())
               .andExpect(status().isBadRequest());

        verify(userService, never()).login(anyString(), anyString());
    }


    private MultiValueMap<String, String> getParams() {

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("userId", "userId12");
        params.add("password", "password1234");
        params.add("email", "email@gmail.com");
        params.add("birth", LocalDate.of(1997, 11, 12)
                                     .toString());
        params.add("profileMessage", "프로필 메세지");

        return params;
    }

}
