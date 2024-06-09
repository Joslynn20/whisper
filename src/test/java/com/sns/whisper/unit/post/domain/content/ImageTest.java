package com.sns.whisper.unit.post.domain.content;

import static org.assertj.core.api.Assertions.assertThat;

import com.sns.whisper.domain.post.domain.Post;
import com.sns.whisper.domain.post.domain.content.Image;
import java.lang.reflect.Field;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class ImageTest {

    @Test
    @DisplayName("이미지와 연관관계가 있는 Post를 바인딩한다.")
    void belongTo() throws Exception {
        //given
        Post post = Post.builder()
                        .id(1L)
                        .build();
        Image testImage = new Image("testImageUrl");

        //when
        testImage.belongTo(post);

        //then
        Field postField = Image.class.getDeclaredField("post");
        postField.setAccessible(true);
        Post fieldPost = (Post) postField.get(testImage);

        assertThat(post).isEqualTo(fieldPost);
    }
}
