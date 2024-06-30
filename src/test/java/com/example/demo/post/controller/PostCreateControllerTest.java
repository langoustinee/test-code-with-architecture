package com.example.demo.post.controller;

import com.example.demo.mock.TestContainer;
import com.example.demo.post.controller.response.PostResponse;
import com.example.demo.post.domain.PostCreate;
import com.example.demo.user.domain.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

import static com.example.demo.user.domain.UserStatus.PENDING;
import static org.assertj.core.api.Assertions.assertThat;

public class PostCreateControllerTest {

    @Test
    @DisplayName("사용자는 새로운 게시글을 등록할 수 있다.")
    void 사용자는_새로운_게시글을_등록할_수_있다() throws Exception {
        // given
        TestContainer testContainer = TestContainer.builder()
                .clockHolder(() -> 12345L)
                .build();
        testContainer.userRepository.save(User.builder()
                .id(1L)
                .email("lango@test.com")
                .nickname("lango")
                .address("Seoul")
                .status(PENDING)
                .certificationCode("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa")
                .build()
        );
        PostCreate request = PostCreate.builder()
                .writerId(1)
                .content("hello spring!")
                .build();
        // when
        ResponseEntity<PostResponse> result = testContainer.postCreateController.create(request);
        // then
        assertThat(result.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(201));
        assertThat(result.getBody()).isNotNull();
        assertThat(result.getBody().getContent()).isEqualTo("hello spring!");
        assertThat(result.getBody().getWriter().getId()).isEqualTo(1);
        assertThat(result.getBody().getWriter().getEmail()).isEqualTo("lango@test.com");
        assertThat(result.getBody().getCreatedAt()).isEqualTo(12345L);
        assertThat(result.getBody().getWriter().getNickname()).isEqualTo("lango");
    }

}
