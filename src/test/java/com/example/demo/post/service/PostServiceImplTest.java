package com.example.demo.post.service;

import com.example.demo.mock.FakePostRepository;
import com.example.demo.mock.FakeUserRepository;
import com.example.demo.mock.TestClockHolder;
import com.example.demo.post.domain.Post;
import com.example.demo.post.domain.PostCreate;
import com.example.demo.post.domain.PostUpdate;
import com.example.demo.user.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.example.demo.user.domain.UserStatus.ACTIVE;
import static com.example.demo.user.domain.UserStatus.PENDING;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class PostServiceImplTest {

    private PostServiceImpl postServiceImpl;

    @BeforeEach
    void setUp() {
        FakePostRepository fakePostRepository = new FakePostRepository();
        FakeUserRepository fakeUserRepository = new FakeUserRepository();
        this.postServiceImpl = PostServiceImpl.builder()
                .postRepository(fakePostRepository)
                .userRepository(fakeUserRepository)
                .clockHolder(new TestClockHolder(12345L))
                .build();
        User user1 = User.builder()
                .id(1L)
                .email("lango@test.com")
                .nickname("lango")
                .address("Seoul")
                .certificationCode("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa")
                .status(ACTIVE)
                .lastLoginAt(0L)
                .build();
        User user2 = User.builder()
                .id(2L)
                .email("sonny@test.com")
                .nickname("sonny")
                .address("Pangyo")
                .certificationCode("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaab")
                .status(PENDING)
                .lastLoginAt(0L)
                .build();
        fakeUserRepository.save(user1);
        fakeUserRepository.save(user2);
        fakePostRepository.save(Post.builder()
                .id(1L)
                .content("helloworld")
                .createdAt(12345L)
                .modifiedAt(0L)
                .writer(user1)
                .build());
    }

    @Test
    @DisplayName("getById는 존재하는 게시물을 반환한다.")
    void getById는_존재하는_게시물을_반환한다() {
        // given
        // when
        Post post = postServiceImpl.getById(1);

        // then
        assertThat(post.getContent()).isEqualTo("helloworld");
        assertThat(post.getWriter().getEmail()).isEqualTo("lango@test.com");
    }

    @Test
    void postCreateDto_를_이용하여_게시물을_생성할_수_있다() {
        // given
        PostCreate postCreate = PostCreate.builder()
                .writerId(1)
                .content("foobar")
                .build();

        // when
        Post post = postServiceImpl.create(postCreate);

        // then
        assertThat(post.getId()).isNotNull();
        assertThat(post.getContent()).isEqualTo("foobar");
        assertThat(post.getCreatedAt()).isEqualTo(12345L);
    }

    @Test
    void postUpdateDto_를_이용하여_게시물을_수정할_수_있다() {
        // given
        PostUpdate postUpdate = PostUpdate.builder()
                .content("hello world :)")
                .build();

        // when
        postServiceImpl.update(1, postUpdate);

        // then
        Post post = postServiceImpl.getById(1);
        System.out.println(post);
        assertThat(post.getContent()).isEqualTo("hello world :)");
        assertThat(post.getModifiedAt()).isEqualTo(12345L);
    }

}
