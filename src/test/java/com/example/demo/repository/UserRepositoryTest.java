package com.example.demo.repository;

import com.example.demo.model.UserStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@TestPropertySource("classpath:test-application.properties")
@Sql("/sql/user-repository-test-data.sql")
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("findByIdAndStatus로 사용자 데이터를 조회할 수 있다.")
    void findByIdAndStatus로_사용자_데이터를_조회할_수_있다() {
        // given
        // when
        Optional<UserEntity> result = userRepository.findByIdAndStatus(1, UserStatus.ACTIVE);
        // then
        assertThat(result.isPresent()).isTrue();
    }

    @Test
    @DisplayName("findByIdAndStatus로 사용자 데이터를 조회할 때 데이터가 없다면 Optional empty를 반환한다.")
    void findByIdAndStatus로_사용자_데이터를_조회할_때_데이터가_없다면_Optional_empty를_반환한다() {
        // given
        // when
        Optional<UserEntity> result = userRepository.findByIdAndStatus(1, UserStatus.PENDING);
        // then
        assertThat(result.isEmpty()).isTrue();
    }

    @Test
    @DisplayName("findByEmailAndStatus로 사용자 데이터를 조회할 때 데이터가 없다면 Optional empty를 반환한다.")
    void findByEmailAndStatus로_사용자_데이터를_조회할_때_데이터가_없다면_Optional_empty를_반환한다() {
        // given
        // when
        Optional<UserEntity> result = userRepository.findByEmailAndStatus("lango@test.com", UserStatus.PENDING);
        // then
        assertThat(result.isEmpty()).isTrue();
    }

}
