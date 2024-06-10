package com.sns.whisper.spring.integration.post;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

import com.sns.whisper.common.factory.UserFactory;
import com.sns.whisper.domain.post.application.PostService;
import com.sns.whisper.domain.post.application.dto.request.PostUploadServiceRequest;
import com.sns.whisper.domain.post.infrastructure.JPAPostRepository;
import com.sns.whisper.domain.user.domain.User;
import com.sns.whisper.domain.user.domain.respository.UserRepository;
import com.sns.whisper.exception.post.NotFoundUserException;
import com.sns.whisper.spring.integration.IntegrationTest;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

public class PostServiceIntegrationTest extends IntegrationTest {

    @Autowired
    private PostService postService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JPAPostRepository postRepository;

    @AfterEach
    public void tearDown() {
        postRepository.deleteAllInBatch();
    }

    @Test
    @DisplayName("게시물 업로드에 성공하면 PostId를 반환한다.")
    void uploadPost_Valid_Success() throws Exception {
        //given

        User user = UserFactory.user(1L, "testUser");
        userRepository.save(user);

        PostUploadServiceRequest serviceRequest = createServiceRequest();

        //when
        Long postId = postService.uploadPost(serviceRequest);

        //then
        assertThat(postId).isNotNull();
    }

    @Test
    @DisplayName("회원 아이디에 해당하는 회원을 찾을 수 없을 경우 예외가 발생한다.")
    void uploadPost_NotValidUser_ThrownException() throws Exception {
        //given

        PostUploadServiceRequest serviceRequest = createServiceRequest();

        //when, then
        assertThatCode(() -> postService.uploadPost(serviceRequest)).isInstanceOf(
                                                                            NotFoundUserException.class)
                                                                    .hasFieldOrPropertyWithValue(
                                                                            "httpStatus",
                                                                            HttpStatus.NOT_FOUND)
                                                                    .hasMessage("유효하지 않은 회원입니다.");
    }


    private PostUploadServiceRequest createServiceRequest() {
        List<MultipartFile> testImages = List.of(new MockMultipartFile("images",
                "image1.png", "image/png", "images".getBytes()));

        return PostUploadServiceRequest.builder()
                                       .userId("testUser")
                                       .content("새로운 게시물입니다.")
                                       .images(testImages)
                                       .build();
    }


}
