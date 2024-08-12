package com.sns.whisper.spring.integration.post;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.sns.whisper.common.factory.UserFactory;
import com.sns.whisper.domain.post.application.PostService;
import com.sns.whisper.domain.post.application.dto.request.PostDeleteServiceRequest;
import com.sns.whisper.domain.post.application.dto.request.PostModifyServiceRequest;
import com.sns.whisper.domain.post.application.dto.request.PostUploadServiceRequest;
import com.sns.whisper.domain.post.domain.Post;
import com.sns.whisper.domain.post.infrastructure.JPAPostRepository;
import com.sns.whisper.domain.user.domain.User;
import com.sns.whisper.domain.user.infrastructure.JPAUserRepository;
import com.sns.whisper.exception.post.NotFoundUserException;
import com.sns.whisper.exception.post.PostNotBelongToUserException;
import com.sns.whisper.spring.integration.IntegrationTest;
import java.util.List;
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
    private JPAUserRepository userRepository;

    @Autowired
    private JPAPostRepository postRepository;


    @Test
    @DisplayName("게시물 업로드에 성공하면 PostId를 반환한다.")
    void uploadPost_Valid_Success() throws Exception {
        //given

        User user = UserFactory.user("testId", "password1234");
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

    @Test
    @DisplayName("회원은 게시물을 수정할 수 있다.")
    void modifyPost_ValidContentAndUser_Success() throws Exception {
        //given
        User user = UserFactory.user("testId", "password1234");
        userRepository.save(user);

        Post post = Post.builder()
                        .content("기존 게시물 내용")
                        .user(user)
                        .build();
        postRepository.save(post);

        //when
        PostModifyServiceRequest serviceRequest = createModifyServiceRequest(post.getId(),
                user.getUserId());

        postService.modifyPost(serviceRequest);

        //then
        assertThat(post.getContent()).isEqualTo(serviceRequest.getContent());
    }

    @Test
    @DisplayName("현재 회원이 작성하지 않은 게시물은 수정할 수 없다.")
    void modifyPost_PostNotBelongToUser_403ExceptionThrown() throws Exception {
        //given
        User user = UserFactory.user("testId", "password1234");
        User currentUser = UserFactory.user("currentUserId", "password12345");

        userRepository.saveAll(List.of(user, currentUser));

        Post post = Post.builder()
                        .content("기존 게시물 내용")
                        .user(user)
                        .build();

        postRepository.save(post);

        //when
        PostModifyServiceRequest serviceRequest = createModifyServiceRequest(
                post.getId(), currentUser.getUserId());

        assertThatThrownBy(() -> postService.modifyPost(serviceRequest))
                .isInstanceOf(PostNotBelongToUserException.class)
                .hasFieldOrPropertyWithValue("httpStatus", HttpStatus.FORBIDDEN)
                .hasMessage("현재 회원이 작성한 글이 아닙니다.");

    }


    @Test
    @DisplayName("로그인 회원과 게시물 작성자가 일치하면 게시물을 삭제할 수 있다.")
    void deletePost_PostBelongToUser_Success() throws Exception {
        //given
        User user = UserFactory.user("testId");

        userRepository.save(user);

        Post post = Post.builder()
                        .content("기존 게시물 내용")
                        .user(user)
                        .images(List.of("www.test.testImage.png"))
                        .build();

        post = postRepository.save(post);

        //when, then
        PostDeleteServiceRequest postDeleteServiceRequest = new PostDeleteServiceRequest(
                post.getId(), "testId");

        assertThatCode(
                () -> postService.deletePost(postDeleteServiceRequest)).doesNotThrowAnyException();
    }


    @Test
    @DisplayName("로그인 회원과 게시물 작성자가 일치하지 않으면, 게시물을 삭제할 수 없다.")
    void deletePost_PostNotBelongToUser_403Exception() throws Exception {
        //given
        User user = UserFactory.user("testId");
        User currentUser = UserFactory.user("currentUserId");

        userRepository.saveAll(List.of(user, currentUser));

        userRepository.save(user);

        Post post = Post.builder()
                        .content("기존 게시물 내용")
                        .user(user)
                        .images(List.of("www.test.testImage.png"))
                        .build();

        post = postRepository.save(post);

        //when, then
        PostDeleteServiceRequest postDeleteServiceRequest = new PostDeleteServiceRequest(
                post.getId(), currentUser.getUserId());

        assertThatThrownBy(() -> postService.deletePost(postDeleteServiceRequest))
                .isInstanceOf(PostNotBelongToUserException.class)
                .hasFieldOrPropertyWithValue("httpStatus", HttpStatus.FORBIDDEN)
                .hasMessage("현재 회원이 작성한 글이 아닙니다.");
    }


    private PostModifyServiceRequest createModifyServiceRequest(Long postId, String userId) {
        return new PostModifyServiceRequest(postId, userId, "새로운 게시물 내용");
    }


    private PostUploadServiceRequest createServiceRequest() {
        List<MultipartFile> testImages = List.of(new MockMultipartFile("images",
                "image1.png", "image/png", "images".getBytes()));

        return PostUploadServiceRequest.builder()
                                       .userId("testId")
                                       .content("새로운 게시물입니다.")
                                       .images(testImages)
                                       .build();
    }


}
