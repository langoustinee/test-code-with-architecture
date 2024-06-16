package com.example.demo.service;

import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.UserEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.AFTER_TEST_METHOD;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_METHOD;

@SpringBootTest
@TestPropertySource("classpath:test-application.properties")
@SqlGroup({
        @Sql(value = "/sql/src/test/resources/sql/user-service-test-data.sql", executionPhase = BEFORE_TEST_METHOD),
        @Sql(value = "/sql/src/test/resources/sql/delete-all-data.sql", executionPhase = AFTER_TEST_METHOD)
})
public class UserServiceTest {

    @Autowired
    private UserService userService;

    @Test
    @DisplayName("getByEmail은 ACTIVE 상태의 사용자를 조회한다.")
    void getByEmail은_ACTIVE_상태의_사용자를_조회한다() {
        // given
        String email = "lango@test.com";
        // when
        UserEntity result = userService.getByEmail(email);
        // then
        assertThat(result.getNickname()).isEqualTo("lango");
    }

    @Test
    @DisplayName("getByEmail은 PENDING 상태의 사용자를 조회할 수 없다.")
    void getByEmail은_PENDING_상태의_사용자를_조회할_수_없다() {
        // given
        String email = "sonny@test.com";
        // when & then
        assertThatThrownBy(() -> userService.getByEmail(email))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    @DisplayName("getById는 ACTIVE 상태의 사용자를 조회한다.")
    void getById는_ACTIVE_상태의_사용자를_조회한다() {
        // given
        // when
        UserEntity result = userService.getById(1);
        // then
        assertThat(result.getNickname()).isEqualTo("lango");
    }

    @Test
    @DisplayName("getById는 PENDING 상태의 사용자를 조회할 수 없다.")
    void getById는_PENDING_상태의_사용자를_조회할_수_없다() {
        // given
        // when & then
        assertThatThrownBy(() -> userService.getById(2))
                .isInstanceOf(ResourceNotFoundException.class);
    }

}
