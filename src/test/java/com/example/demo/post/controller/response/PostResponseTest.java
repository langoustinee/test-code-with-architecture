package com.example.demo.post.controller.response;

import com.example.demo.post.domain.Post;
import com.example.demo.user.domain.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Clock;

import static com.example.demo.user.domain.UserStatus.ACTIVE;
import static org.assertj.core.api.Assertions.assertThat;

public class PostResponseTest {

    @Test
    @DisplayName("Post로 PostResponse 객체를 생성할 수 있다.")
    void Post로_PostResponse_객체를_생성할_수_있다() {
        // given
        User user = User.builder()
                .email("lango@test.com")
                .nickname("lango")
                .address("Seoul")
                .status(ACTIVE)
                .certificationCode("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa")
                .build();
        Post post = Post.builder()
                .content("Hello World!")
                .createdAt(Clock.systemUTC().millis())
                .writer(user)
                .build();
        // when
        PostResponse postResponse = PostResponse.from(post);

        // then
        assertThat(postResponse.getContent()).isEqualTo("Hello World!");
        assertThat(postResponse.getWriter().getEmail()).isEqualTo("lango@test.com");
        assertThat(postResponse.getWriter().getNickname()).isEqualTo("lango");
        assertThat(postResponse.getWriter().getStatus()).isEqualTo(ACTIVE);
    }

}
