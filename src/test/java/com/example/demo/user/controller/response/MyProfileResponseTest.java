package com.example.demo.user.controller.response;

import com.example.demo.user.domain.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.example.demo.user.domain.UserStatus.ACTIVE;
import static org.assertj.core.api.Assertions.assertThat;

public class MyProfileResponseTest {

    @Test
    @DisplayName("User로 MyProfileResponse 객체를 생성할 수 있다.")
    void User로_MyProfileResponse_객체를_생성할_수_있다() {
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
        MyProfileResponse myProfileResponse = MyProfileResponse.from(user);
        // then
        assertThat(myProfileResponse.getId()).isEqualTo(1);
        assertThat(myProfileResponse.getEmail()).isEqualTo("lango@test.com");
        assertThat(myProfileResponse.getNickname()).isEqualTo("lango");
        assertThat(myProfileResponse.getAddress()).isEqualTo("Seoul");
        assertThat(myProfileResponse.getStatus()).isEqualTo(ACTIVE);
        assertThat(myProfileResponse.getLastLoginAt()).isEqualTo(100L);
    }

}
