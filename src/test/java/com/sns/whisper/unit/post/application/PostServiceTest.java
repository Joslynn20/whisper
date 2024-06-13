package com.sns.whisper.unit.post.application;

import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.sns.whisper.common.factory.UserFactory;
import com.sns.whisper.domain.post.application.PostService;
import com.sns.whisper.domain.post.application.dto.request.PostUploadServiceRequest;
import com.sns.whisper.domain.post.domain.Post;
import com.sns.whisper.domain.post.domain.repository.ImageStorage;
import com.sns.whisper.domain.post.domain.repository.PostRepository;
import com.sns.whisper.domain.user.domain.User;
import com.sns.whisper.domain.user.domain.respository.UserRepository;
import com.sns.whisper.exception.post.NotFoundUserException;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
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
