package com.example.demo.post.domain;

import com.example.demo.user.domain.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.example.demo.user.domain.UserStatus.ACTIVE;
import static com.example.demo.user.domain.UserStatus.PENDING;
import static java.util.UUID.randomUUID;
import static org.assertj.core.api.Assertions.assertThat;

public class PostTest {

    @Test
    @DisplayName("PostCreate를 통해 게시글을 생성할 수 있다.")
    void PostCreate를_통해_게시글을_생성할_수_있다() {
        // given
        PostCreate request = PostCreate.builder()
                .writerId(1)
                .content("hello spring!")
                .build();
        User user = User.builder()
                .email("lango@test.com")
                .nickname("lango")
                .address("Seoul")
                .status(ACTIVE)
                .certificationCode("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa")
                .build();
        // when
        Post post = Post.from(user, request);
        // then
        assertThat(post.getContent()).isEqualTo("hello spring!");
        assertThat(post.getWriter().getEmail()).isEqualTo("lango@test.com");
        assertThat(post.getWriter().getNickname()).isEqualTo("lango");
        assertThat(post.getWriter().getAddress()).isEqualTo("Seoul");
        assertThat(post.getWriter().getStatus()).isEqualTo(ACTIVE);
        assertThat(post.getWriter().getCertificationCode()).isEqualTo("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa");
    }

}
