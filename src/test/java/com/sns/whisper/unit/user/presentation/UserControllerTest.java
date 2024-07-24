package com.sns.whisper.unit.user.presentation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.sns.whisper.domain.user.application.dto.request.AuthUserForUserRequest;
import com.sns.whisper.domain.user.application.dto.request.FollowServiceRequest;
import com.sns.whisper.domain.user.application.dto.request.UserSignUpServiceRequest;
import com.sns.whisper.domain.user.application.dto.response.FollowServiceResponse;
import com.sns.whisper.domain.user.application.dto.response.UserSearchServiceResponse;
import com.sns.whisper.unit.ControllerTest;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.ArgumentCaptor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpMethod;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.ResultActions;
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

        ArgumentCaptor<String> valueCapture = ArgumentCaptor.forClass(String.class);

        //when, then
        doNothing().when(loginService)
                   .login(valueCapture.capture(), valueCapture.capture());
        mockMvc.perform(post("/api/users/login").param("userId", userId)
                                                .param("password", password))
               .andDo(print())
               .andExpect(status().isOk());

        assertThat(valueCapture.getAllValues()).isEqualTo(List.of(userId, password));
        verify(loginService, times(1)).login(anyString(), anyString());
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

        verify(loginService, never()).login(anyString(), anyString());
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

        verify(loginService, never()).login(anyString(), anyString());
    }

    @Test
    @DisplayName("회원은 다른 회원을 팔로우할 수 있다.")
    void followUser_ValidUser_Success() throws Exception {
        //given
        FollowServiceResponse responseDto = new FollowServiceResponse(1, true);

        given(userService.followUser(any(FollowServiceRequest.class))).willReturn(responseDto);

        //when
        ResultActions perform = mockMvc.perform(post("/api/users/{userId}/followings", "testId1"))
                                       .andDo(print());

        String body = perform.andExpect(status().isCreated())
                             .andReturn()
                             .getResponse()
                             .getContentAsString();

        //then
        assertThat(body).contains(objectMapper.writeValueAsString(responseDto));
        verify(userService, times(1)).followUser(any(FollowServiceRequest.class));
    }

    @Test
    @DisplayName("회원은 다른 회원을 언팔로우할 수 있다.")
    void unfollowUser_LoginUser_Success() throws Exception {
        //given
        FollowServiceResponse responseDto = new FollowServiceResponse(0, false);

        given(userService.unfollowUser(any(FollowServiceRequest.class))).willReturn(responseDto);

        //when
        ResultActions perform = mockMvc.perform(delete("/api/users/{userId}/followings", "testId1"))
                                       .andDo(print());

        String body = perform.andExpect(status().isOk())
                             .andReturn()
                             .getResponse()
                             .getContentAsString();

        //then
        assertThat(body).contains(objectMapper.writeValueAsString(responseDto));
        verify(userService, times(1)).unfollowUser(any(FollowServiceRequest.class));
    }

    @Test
    @DisplayName("회원은 특정 회원의 팔로잉 목록을 조회할 수 있다.")
    void searchFollowing_ValidUser_Success() throws Exception {
        //given
        List<UserSearchServiceResponse> searchServiceResponses = List.of(
                new UserSearchServiceResponse("test-image1.png", "testId1", true),
                new UserSearchServiceResponse("test-image2.png", "testId2", false),
                new UserSearchServiceResponse("test-image3.png", "testId", null));

        given(userService.searchFollowings(any(Pageable.class), anyString(),
                any(AuthUserForUserRequest.class))).willReturn(searchServiceResponses);

        //when
        ResultActions resultActions = mockMvc.perform(
                get("/api/users/{userId}/followings", "testId4")
                        .param("page", "0")
                        .param("limit", "0"));

        //then
        resultActions.andExpect(status().isOk())
                     .andExpect(jsonPath("$['data'][*].profileImage",
                             contains("test-image1.png", "test-image2.png",
                                     "test-image3.png")))
                     .andExpect(jsonPath("$['data'][*].userId",
                             contains("testId1", "testId2", "testId")))
                     .andExpect(jsonPath("$['data'][*].following", contains(true, false, null)));

        verify(loginService, times(1)).getCurrentUserId();
        verify(userService, times(1)).searchFollowings(any(), anyString(), any());
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
