package com.sns.whisper.unit.post.domain.content;

import static org.assertj.core.api.Assertions.assertThat;

import com.sns.whisper.common.factory.UserFactory;
import com.sns.whisper.domain.post.domain.Post;
import com.sns.whisper.domain.post.domain.content.Image;
import com.sns.whisper.domain.post.infrastructure.JPAPostRepository;
import com.sns.whisper.domain.user.domain.User;
import com.sns.whisper.domain.user.infrastructure.JPAUserRepository;
import com.sns.whisper.global.config.JpaConfiguration;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@Import(value = JpaConfiguration.class)
@DataJpaTest
public class PostRepositoryTest {

    @Autowired
    private JPAUserRepository userRepository;

    @Autowired
    private JPAPostRepository postRepository;

    @Autowired
    private TestEntityManager testEntityManager;

    @Test
    @DisplayName("Post를 삭제하면, 해당 게시물의 이미지도 함께 삭제된다.")
    void delete_WhenDeletePost_ImagesDeletedTogether() throws Exception {
        //given
        User user = UserFactory.user("testId");
        userRepository.save(user);
        List<String> imageUrls = List.of("testImage1.png", "testImage2.png");

        Post post = Post.builder()
                        .images(imageUrls)
                        .content("게시물 내용")
                        .user(user)
                        .build();

        postRepository.save(post);

        testEntityManager.flush();
        testEntityManager.clear();

        //when
        List<Image> beforeImages = testEntityManager.getEntityManager()
                                                    .createQuery(
                                                            "select i from Image i where i.post = :post",
                                                            Image.class)
                                                    .setParameter("post", post)
                                                    .getResultList();

        postRepository.deleteById(post.getId());

        testEntityManager.flush();
        testEntityManager.clear();

        List<Image> afterImages = testEntityManager.getEntityManager()
                                                   .createQuery(
                                                           "select i from Image i where i.post = :post",
                                                           Image.class)
                                                   .setParameter("post", post)
                                                   .getResultList();

        //then
        assertThat(beforeImages).hasSize(2);
        assertThat(afterImages).isEmpty();
    }
}
