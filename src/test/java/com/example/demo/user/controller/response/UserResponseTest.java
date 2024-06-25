package com.example.demo.user.controller.response;

import com.example.demo.user.domain.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.example.demo.user.domain.UserStatus.ACTIVE;
import static org.assertj.core.api.Assertions.assertThat;

public class UserResponseTest {

    @Test
    @DisplayName("User로 UserResponse 객체를 생성할 수 있다.")
    void User로_UserResponse_객체를_생성할_수_있다() {
        // given
        User user = User.builder()
                .id(1L)
                .email("lango@test.com")
                .nickname("lango")
                .address("Seoul")
                .status(ACTIVE)
                .certificationCode("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa")
                .lastLoginAt(100L)
                .build();
        // when
        UserResponse userResponse = UserResponse.from(user);
        // then
        assertThat(userResponse.getId()).isEqualTo(1);
        assertThat(userResponse.getEmail()).isEqualTo("lango@test.com");
        assertThat(userResponse.getNickname()).isEqualTo("lango");
        assertThat(userResponse.getStatus()).isEqualTo(ACTIVE);
        assertThat(userResponse.getLastLoginAt()).isEqualTo(100L);
    }

}
