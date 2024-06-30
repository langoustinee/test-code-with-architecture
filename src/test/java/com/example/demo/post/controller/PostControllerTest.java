package com.example.demo.post.controller;

import com.example.demo.common.domain.exception.ResourceNotFoundException;
import com.example.demo.mock.TestContainer;
import com.example.demo.post.controller.response.PostResponse;
import com.example.demo.post.domain.Post;
import com.example.demo.post.domain.PostUpdate;
import com.example.demo.user.domain.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

import static com.example.demo.user.domain.UserStatus.PENDING;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class PostControllerTest {

    @Test
    @DisplayName("사용자는 게시글 상세정보를 조회할 수 있다.")
    void 사용자는_게시글_상세정보를_조회할_수_있다() {
        // given
        TestContainer testContainer = TestContainer.builder()
                .build();
        User user = User.builder()
                .id(1L)
                .email("lango@test.com")
                .nickname("lango")
                .address("Seoul")
                .status(PENDING)
                .certificationCode("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa")
                .build();
        testContainer.userRepository.save(user);
        testContainer.postRepository.save(Post.builder()
                .id(1L)
                .content("hello spring!")
                .writer(user)
                .createdAt(12345L)
                .build());
        // when
        ResponseEntity<PostResponse> result = testContainer.postController.getPostById(1);
        // then
        assertThat(result.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(200));
        assertThat(result.getBody()).isNotNull();
        assertThat(result.getBody().getContent()).isEqualTo("hello spring!");
        assertThat(result.getBody().getCreatedAt()).isEqualTo(12345L);
        assertThat(result.getBody().getWriter().getId()).isEqualTo(1);
        assertThat(result.getBody().getWriter().getEmail()).isEqualTo("lango@test.com");
        assertThat(result.getBody().getWriter().getNickname()).isEqualTo("lango");
    }

    @Test
    @DisplayName("사용자가 존재하지 않는 게시글 상세정보를 조회할 경우 예외가 발생한다.")
    void 사용자가_존재하지_않는_게시글_상세정보를_조회할_경우_예외가_발생한다() {
        // given
        TestContainer testContainer = TestContainer.builder()
                .build();
        // when & then
        assertThatThrownBy(() -> testContainer.postController.getPostById(2))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    @DisplayName("사용자는 작성한 게시글을 수정할 수 있다.")
    void 사용자는_작성한_게시글을_수정할_수_있다() {
        // given
        TestContainer testContainer = TestContainer.builder()
                .clockHolder(() -> 12345L)
                .build();
        User user = User.builder()
                .id(1L)
                .email("lango@test.com")
                .nickname("lango")
                .address("Seoul")
                .status(PENDING)
                .certificationCode("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa")
                .build();
        testContainer.userRepository.save(user);
        testContainer.postRepository.save(Post.builder()
                .id(1L)
                .content("hello spring!")
                .writer(user)
                .createdAt(12345L)
                .build());
        PostUpdate request = PostUpdate.builder()
                .content("hello java!")
                .build();
        // when
        ResponseEntity<PostResponse> result = testContainer.postController.updatePost(1L, request);
        // then
        assertThat(result.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(200));
        assertThat(result.getBody()).isNotNull();
        assertThat(result.getBody().getContent()).isEqualTo("hello java!");
        assertThat(result.getBody().getModifiedAt()).isEqualTo(12345L);
        assertThat(result.getBody().getWriter().getId()).isEqualTo(1);
        assertThat(result.getBody().getWriter().getEmail()).isEqualTo("lango@test.com");
        assertThat(result.getBody().getWriter().getNickname()).isEqualTo("lango");
    }

}
