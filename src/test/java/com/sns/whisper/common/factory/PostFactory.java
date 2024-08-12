package com.sns.whisper.common.factory;

import com.sns.whisper.domain.comment.application.dto.response.CommentServiceResponse;
import com.sns.whisper.domain.post.application.dto.response.PostServiceResponse;
import com.sns.whisper.domain.post.domain.Post;
import com.sns.whisper.domain.user.domain.User;
import java.time.LocalDateTime;
import java.util.List;

public class PostFactory {

    public static Post post(User user) {
        return createPost(null, user);
    }

    public static Post post(Long id, User user) {
        return createPost(id, user);
    }

    public static List<PostServiceResponse> mockPostServiceResponses() {
        CommentServiceResponse comment1 = CommentServiceResponse.builder()
                                                                .id(1L)
                                                                .content("comment Content 1")
                                                                .authorUserId("commentAuthor1")
                                                                .profileImageUrl(
                                                                        "www.test.testImage1.png")
                                                                .build();
        CommentServiceResponse comment2 = CommentServiceResponse.builder()
                                                                .id(2L)
                                                                .content("comment Content 2")
                                                                .authorUserId("commentAuthor2")
                                                                .profileImageUrl(
                                                                        "www.test.testImage2.png")
                                                                .build();
        CommentServiceResponse comment3 = CommentServiceResponse.builder()
                                                                .id(3L)
                                                                .content("comment Content 3")
                                                                .authorUserId("commentAuthor3")
                                                                .profileImageUrl(
                                                                        "www.test.testImage3.png")
                                                                .build();

        PostServiceResponse postServiceResponse = PostServiceResponse.builder()
                                                                     .id(1L)
                                                                     .content("post content")
                                                                     .profileImage(
                                                                             "www.test.testImage.png")
                                                                     .authorUserId("postAuthor")
                                                                     .imageUrls(
                                                                             List.of("www.test.postImage1.png",
                                                                                     "www.test.postImage2.png"))
                                                                     .comments(List.of(comment1,
                                                                             comment2, comment3))
                                                                     .createdAt(LocalDateTime.now())
                                                                     .updatedAt(LocalDateTime.now())
                                                                     .build();
        return List.of(postServiceResponse);
    }

    private static Post createPost(Long id, User user) {
        return MockPost.builder()
                       .id(id)
                       .user(user)
                       .build();
    }

}
