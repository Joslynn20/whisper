package com.sns.whisper.unit.user.presentation;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.sns.whisper.domain.post.application.dto.request.PostUploadServiceRequest;
import com.sns.whisper.unit.ControllerTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.http.HttpMethod;
import org.springframework.mock.web.MockMultipartFile;

public class PostControllerTest extends ControllerTest {

    @Test
    @DisplayName("로그인한 회원은 게시물을 작성할 수 있다.")
    void uploadPost_LoginUser_Success() throws Exception {
        //given

        String content = "새로운 게시물입니다.";

        MockMultipartFile image1 = new MockMultipartFile("images",
                "image1.png", "image/png", "images".getBytes());

        MockMultipartFile image2 = new MockMultipartFile("images",
                "image2.png", "image/png", "images".getBytes());

        //when, then
        mockMvc.perform(multipart(HttpMethod.POST, "/api/posts").file(image1)
                                                                .file(image2)
                                                                .param("content", content)
               )
               .andDo(print())
               .andExpect(status().isCreated())
               .andExpect(jsonPath("$.message").value("게시물을 업로드했습니다."));

        verify(postService).uploadPost(any(PostUploadServiceRequest.class));

    }

    @Test
    @DisplayName("게시물 업로드 시, 이미지를 5장을 초과하여 첨부할 수 없다.")
    void uploadPost_ImagesOver5_400Exception() throws Exception {
        //given
        String content = "새로운 게시물입니다.";

        MockMultipartFile image = new MockMultipartFile("images",
                "image1.png", "image/png", "images".getBytes());

        //when, then
        mockMvc.perform(multipart(HttpMethod.POST, "/api/posts").file(image)
                                                                .file(image)
                                                                .file(image)
                                                                .file(image)
                                                                .file(image)
                                                                .file(image)
                                                                .param("content", content)
               )
               .andDo(print())
               .andExpect(status().isBadRequest());

        verify(postService, never()).uploadPost(any(PostUploadServiceRequest.class));

    }


    @ParameterizedTest
    @ValueSource(strings = {"image/gif", "image/tiff", "image/x-MS-bmp"})
    @DisplayName("유효하지 않은 이미지 형식은 첨부할 수 없다.")
    void uploadPost_NotValidContentType_400Exception(String contentType) throws Exception {
        //given
        String content = "새로운 게시물입니다.";
        MockMultipartFile image = new MockMultipartFile("images",
                "image.png", contentType, "image.png".getBytes());

        //when, then
        mockMvc.perform(multipart(HttpMethod.POST, "/api/posts").file(image)
                                                                .param("content", content)
               )
               .andDo(print())
               .andExpect(status().isBadRequest());

        verify(postService, never()).uploadPost(any(PostUploadServiceRequest.class));

    }


    @ParameterizedTest
    @ValueSource(strings = {"image.exe", "image.pdf", "image.dll"})
    @DisplayName("유효하지 않은 파일 형식은 첨부할 수 없다.")
    void uploadPost_NotValidExtension_400Exception(String file) throws Exception {
        //given
        String content = "새로운 게시물입니다.";
        MockMultipartFile image = new MockMultipartFile("images",
                file, "image/png", file.getBytes());

        //when, then
        mockMvc.perform(multipart(HttpMethod.POST, "/api/posts").file(image)
                                                                .param("content", content)
               )
               .andDo(print())
               .andExpect(status().isBadRequest());

        verify(postService, never()).uploadPost(any(PostUploadServiceRequest.class));

    }

    @Test
    @DisplayName("게시물 내용은 500자를 초과할 수 없다.")
    void uploadPost_ContentOver500_400Exception() throws Exception {
        //given
        String content = "a".repeat(501);

        MockMultipartFile image = new MockMultipartFile("images",
                "image1.png", "image/png", "images".getBytes());

        //when, then
        mockMvc.perform(multipart(HttpMethod.POST, "/api/posts").file(image)
                                                                .param("content", content)
               )
               .andDo(print())
               .andExpect(status().isBadRequest());

        verify(postService, never()).uploadPost(any(PostUploadServiceRequest.class));

    }

}
