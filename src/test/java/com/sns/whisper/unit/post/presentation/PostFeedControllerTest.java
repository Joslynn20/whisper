package com.sns.whisper.unit.post.presentation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.sns.whisper.common.factory.PostFactory;
import com.sns.whisper.domain.post.application.dto.request.UserFeedServiceRequest;
import com.sns.whisper.domain.post.application.dto.response.PostServiceResponse;
import com.sns.whisper.unit.ControllerTest;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;
import org.springframework.test.web.servlet.ResultActions;


public class PostFeedControllerTest extends ControllerTest {

    @Nested
    @DisplayName("내 Feed를 조회할 때,")
    class Discribe_readMyFeed {

        @Test
        @DisplayName("로그인한 회원은 조회에 성공한다.")
        void readMyFeed_LoginUser_Success() throws Exception {
            //given
            List<PostServiceResponse> postServiceResponses = PostFactory.mockPostServiceResponses();
            given(postFeedService.searchUserFeed(any(UserFeedServiceRequest.class),
                    anyString())).willReturn(
                    postServiceResponses);

            //when
            ResultActions resultActions = mockMvc.perform(multipart(HttpMethod.GET, "/api/posts/my")
                    .param("page", "0")
                    .param("limit", "3"));

            // then
            resultActions.andExpect(status().isOk());

            String body = resultActions.andReturn()
                                       .getResponse()
                                       .getContentAsString();

            verify(postFeedService, times(1)).searchUserFeed(any(UserFeedServiceRequest.class),
                    anyString());

            assertThat(body).contains(objectMapper.writeValueAsString(postServiceResponses));
        }

        @Test
        @DisplayName("로그인하지 않은 회원은 예외가 발생한다.")
        void readMyFeed_GuestUser_ExceptionThrown() throws Exception {
            //given
            given(loginService.getCurrentUserId()).willReturn(null);

            //when, then
            mockMvc.perform(multipart(HttpMethod.GET, "/api/posts/my")
                           .param("page", "0")
                           .param("limit", "3"))
                   .andDo(print())
                   .andExpect(status().isUnauthorized());

            verify(postFeedService, never()).searchUserFeed(any(UserFeedServiceRequest.class),
                    anyString());
        }
    }

    
}
