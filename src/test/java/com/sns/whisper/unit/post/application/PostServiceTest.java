package com.sns.whisper.unit.post.application;

import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.sns.whisper.common.factory.PostFactory;
import com.sns.whisper.common.factory.UserFactory;
import com.sns.whisper.domain.post.application.PostService;
import com.sns.whisper.domain.post.application.dto.request.PostDeleteServiceRequest;
import com.sns.whisper.domain.post.application.dto.request.PostModifyServiceRequest;
import com.sns.whisper.domain.post.application.dto.request.PostUploadServiceRequest;
import com.sns.whisper.domain.post.domain.Post;
import com.sns.whisper.domain.post.domain.repository.ImageStorage;
import com.sns.whisper.domain.post.domain.repository.PostRepository;
import com.sns.whisper.domain.user.domain.User;
import com.sns.whisper.domain.user.domain.respository.UserRepository;
import com.sns.whisper.exception.post.NotFoundPostException;
import com.sns.whisper.exception.post.NotFoundUserException;
import com.sns.whisper.exception.post.PostNotBelongToUserException;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

@ExtendWith(MockitoExtension.class)
public class PostServiceTest {

    @InjectMocks
    private PostService postService;

    @Mock
    private ImageStorage imageStorage;

    @Mock
    private PostRepository postRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ApplicationEventPublisher eventPublisher;


    @Test
    @DisplayName("회원은 게시물을 업로드할 수 있다.")
    void uploadPost_ValidUser_Success() throws Exception {
        //given

        User user = UserFactory.user(1L, "testId");

        Post savedPost = Post.builder()
                             .id(1L)
                             .build();

        PostUploadServiceRequest serviceRequest = createRequest();

        given(userRepository.findUserByUserId(serviceRequest.getUserId())).willReturn(
                Optional.of(user));
        given(imageStorage.storeImages(anyList(), anyString())).willReturn(
                extractImageUrlsFrom(serviceRequest));
        given(postRepository.save(any(Post.class))).willReturn(savedPost);

        //when
        Long postId = postService.uploadPost(serviceRequest);

        //then
        assertThat(postId).isNotNull();

        verify(userRepository, times(1)).findUserByUserId(serviceRequest.getUserId());
        verify(imageStorage, times(1)).storeImages(serviceRequest.getImages(),
                serviceRequest.getUserId());
        verify(postRepository, times(1)).save(any(Post.class));
    }

    @Test
    @DisplayName("회원 아이디에 해당하는 회원을 찾지 못할 경우 게시물을 업로드할 수 없다.")
    void uploadPost_NotFoundUser_404Exception() throws Exception {
        //given
        PostUploadServiceRequest serviceRequest = createRequest();
        given(userRepository.findUserByUserId(serviceRequest.getUserId())).willReturn(
                Optional.empty());

        //when, then
        assertThatCode(() ->
                postService.uploadPost(serviceRequest)).isInstanceOf(NotFoundUserException.class)
                                                       .hasFieldOrPropertyWithValue("httpStatus",
                                                               HttpStatus.NOT_FOUND)
                                                       .hasMessage("유효하지 않은 회원입니다.");

    }

    @Test
    @DisplayName("회원은 자신이 작성한 게시물의 내용을 수정할 수 있다.")
    void modifyPost_ValidContentAndUser_Success() throws Exception {
        //given
        User user = UserFactory.user(1L, "testId");
        Post savedPost = Post.builder()
                             .user(user)
                             .content("기존 게시물 내용입니다.")
                             .id(1L)
                             .build();

        PostModifyServiceRequest postModifyServiceRequest = new PostModifyServiceRequest(
                savedPost.getId(),
                user.getUserId(),
                "수정된 게시물 내용입니다.");

        given(postRepository.findById(anyLong())).willReturn(Optional.of(savedPost));
        given(userRepository.findUserByUserId(anyString())).willReturn(Optional.of(user));

        //when
        postService.modifyPost(postModifyServiceRequest);

        //then

        assertThat(savedPost.getContent()).isEqualTo(postModifyServiceRequest.getContent());

        verify(postRepository, times(1)).findById(postModifyServiceRequest.getPostId());
        verify(userRepository, times(1)).findUserByUserId(postModifyServiceRequest.getUserId());

    }

    @Test
    @DisplayName("현재 회원이 작성하지 않은 게시물은 수정할 수 없다.")
    void modifyPost_PostNotBelongToUser_403ExceptionThrown() throws Exception {
        //given
        User currentUser = UserFactory.user(1L, "differentId");
        User savedUser = UserFactory.user(2L, "testId");
        Post savedPost = Post.builder()
                             .user(savedUser)
                             .content("기존 게시물 내용입니다.")
                             .id(1L)
                             .build();

        PostModifyServiceRequest postModifyServiceRequest = createModifyRequest(savedPost,
                currentUser);

        given(postRepository.findById(anyLong())).willThrow(new PostNotBelongToUserException());
        given(userRepository.findUserByUserId(anyString())).willReturn(Optional.of(savedUser));

        //when, then
        assertThatThrownBy(() -> postService.modifyPost(postModifyServiceRequest))
                .isInstanceOf(PostNotBelongToUserException.class)
                .hasFieldOrPropertyWithValue("httpStatus", HttpStatus.FORBIDDEN)
                .hasMessage("현재 회원이 작성한 글이 아닙니다.");

        verify(postRepository, times(1)).findById(savedPost.getId());
        verify(userRepository, times(1)).findUserByUserId(currentUser.getUserId());

    }

    @DisplayName("deletePost 메소드는")
    @Nested
    class Describe_deletePost {

        @DisplayName("존재하지 않는 게시물일 때,")
        @Nested
        class Context_NotExistedPost {

            @Test
            @DisplayName("삭제할 수 없다.")
            void deletePost_NotExistedPost_404Exception() throws Exception {
                //given
                PostDeleteServiceRequest serviceRequest = new PostDeleteServiceRequest(1L,
                        "testId");
                given(postRepository.findById(anyLong())).willReturn(Optional.empty());

                //when, then
                assertThatThrownBy(() -> postService.deletePost(serviceRequest)).isInstanceOf(
                                                                                        NotFoundPostException.class)
                                                                                .hasFieldOrPropertyWithValue(
                                                                                        "httpStatus",
                                                                                        HttpStatus.NOT_FOUND);

                verify(postRepository, times(1)).findById(anyLong());
                verify(userRepository, never()).findUserByUserId(anyString());
                verify(postRepository, never()).delete(any());
            }
        }

        @DisplayName("존재하지 않는 회원일 때,")
        @Nested
        class Context_NotValidUser {

            @Test
            @DisplayName("삭제할 수 없다.")
            void deletePost_NotValidUser_404Exception() throws Exception {
                //given
                User writer = UserFactory.user(1L, "testId");
                PostDeleteServiceRequest serviceRequest = new PostDeleteServiceRequest(1L,
                        "testId");
                Post post = PostFactory.post(1L, writer);

                given(postRepository.findById(anyLong())).willReturn(Optional.of(post));

                //when, then
                assertThatThrownBy(() -> postService.deletePost(serviceRequest)).isInstanceOf(
                                                                                        NotFoundUserException.class)
                                                                                .hasFieldOrPropertyWithValue(
                                                                                        "httpStatus",
                                                                                        HttpStatus.NOT_FOUND);

                verify(postRepository, times(1)).findById(anyLong());
                verify(userRepository, times(1)).findUserByUserId(anyString());
                verify(postRepository, never()).delete(any());
            }
        }

        @Nested
        @DisplayName("로그인 회원과 게시물 작성자가 일치하지 않으면")
        class Context_PostNotBelongToUser {

            @Test
            @DisplayName("게시물을 삭제할 수 없다. -403 예외")
            void deletePost_PostNotBelongToUser_403Exception() throws Exception {
                //given
                User writer = UserFactory.user(1L, "writer");
                User loginUser = UserFactory.user(2L, "loginUser");

                Post post = PostFactory.post(1L, writer);

                given(postRepository.findById(anyLong())).willReturn(Optional.of(post));
                given(userRepository.findUserByUserId(anyString())).willReturn(
                        Optional.of(loginUser));

                //when, then
                PostDeleteServiceRequest serviceRequest = new PostDeleteServiceRequest(1L,
                        "testId");

                assertThatThrownBy(() -> postService.deletePost(serviceRequest)).isInstanceOf(
                                                                                        PostNotBelongToUserException.class)
                                                                                .hasFieldOrPropertyWithValue(
                                                                                        "httpStatus",
                                                                                        HttpStatus.FORBIDDEN);

                verify(postRepository, times(1)).findById(anyLong());
                verify(userRepository, times(1)).findUserByUserId(anyString());
                verify(postRepository, never()).delete(any(Post.class));
            }
        }

        @Nested
        @DisplayName("로그인 회원과 작성자가 일치하면,")
        class Context_PostBelongToUser {

            @Test
            @DisplayName("게시물을 삭제할 수 있다.")
            void deletePost_PostBelongToUser_Success() throws Exception {
                //given
                User writer = UserFactory.user(1L, "writer");
                User loginUser = UserFactory.user(1L, "writer");

                Post post = PostFactory.post(1L, writer);

                given(postRepository.findById(anyLong())).willReturn(Optional.of(post));
                given(userRepository.findUserByUserId(anyString())).willReturn(
                        Optional.of(loginUser));
                willDoNothing().given(postRepository)
                               .delete(any(Post.class));

                PostDeleteServiceRequest serviceRequest = new PostDeleteServiceRequest(1L,
                        loginUser.getUserId());

                //when
                postService.deletePost(serviceRequest);

                //then
                verify(postRepository, times(1)).findById(anyLong());
                verify(userRepository, times(1)).findUserByUserId(anyString());
                verify(postRepository, times(1)).delete(any(Post.class));
            }
        }
    }

    private PostModifyServiceRequest createModifyRequest(Post post, User user) {
        return new PostModifyServiceRequest(
                post.getId(),
                user.getUserId(),
                "수정된 게시물 내용입니다.");
    }

    private PostUploadServiceRequest createRequest() {
        List<MultipartFile> testImages = List.of(new MockMultipartFile("images",
                "image1.png", "image/png", "images".getBytes()));

        return PostUploadServiceRequest.builder()
                                       .userId("testUser")
                                       .content("새로운 게시물입니다.")
                                       .images(testImages)
                                       .build();
    }

    private List<String> extractImageUrlsFrom(PostUploadServiceRequest requestDto) {
        return requestDto.getImages()
                         .stream()
                         .map(MultipartFile::getName)
                         .map(name -> String.format("http://testImages.test/%s", name))
                         .collect(toList());
    }
}
