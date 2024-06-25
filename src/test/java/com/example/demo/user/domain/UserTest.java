package com.example.demo.user.domain;

import com.example.demo.common.domain.exception.CertificationCodeNotMatchedException;
import com.example.demo.mock.TestClockHolder;
import com.example.demo.mock.TestUuidHolder;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.example.demo.user.domain.UserStatus.ACTIVE;
import static com.example.demo.user.domain.UserStatus.PENDING;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class UserTest {

    @Test
    @DisplayName("UserCreate 객체로 생성할 수 있다.")
    void UserCreate_객체로_생성할_수_있다() {
        // given
        UserCreate request = UserCreate.builder()
                .email("lango@test.com")
                .nickname("lango")
                .address("Seoul")
                .build();
        // when
        User user = User.from(request, new TestUuidHolder("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa"));
        // then
        assertThat(user.getId()).isEqualTo(null);
        assertThat(user.getEmail()).isEqualTo("lango@test.com");
        assertThat(user.getNickname()).isEqualTo("lango");
        assertThat(user.getAddress()).isEqualTo("Seoul");
        assertThat(user.getStatus()).isEqualTo(PENDING);
        assertThat(user.getCertificationCode()).isEqualTo("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa");
    }

    @Test
    @DisplayName("UserUpadte 객체로 데이터를 수정할 수 있다.")
    void UserUpadte_객체로_데이터를_수정할_수_있다() {
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
        UserUpdate request = UserUpdate.builder()
                .nickname("lango-u")
                .address("Pangyo")
                .build();
        // when
        user = user.update(request);
        // then
        assertThat(user.getId()).isEqualTo(1L);
        assertThat(user.getEmail()).isEqualTo("lango@test.com");
        assertThat(user.getNickname()).isEqualTo("lango-u");
        assertThat(user.getAddress()).isEqualTo("Pangyo");
        assertThat(user.getStatus()).isEqualTo(ACTIVE);
        assertThat(user.getCertificationCode()).isEqualTo("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa");
        assertThat(user.getLastLoginAt()).isEqualTo(100L);
    }

    @Test
    @DisplayName("로그인을 할 수 있고 로그인할 경우 마지막 로그인 시간이 변경된다.")
    void 로그인을_할_수_있고_로그인할_경우_마지막_로그인_시간이_변경된다() {
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
        user = user.login(new TestClockHolder(12345L));
        // then
        assertThat(user.getLastLoginAt()).isEqualTo(12345L);
    }

    @Test
    @DisplayName("유효한 인증 코드로 계정을 활성화시킬 수 있다.")
    void 유효한_인증_코드로_계정을_활성화시킬_수_있다() {
        // given
        User user = User.builder()
                .id(1L)
                .email("lango@test.com")
                .nickname("lango")
                .address("Seoul")
                .status(PENDING)
                .certificationCode("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa")
                .lastLoginAt(100L)
                .build();
        // when
        user = user.certification("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa");
        // then
        assertThat(user.getStatus()).isEqualTo(ACTIVE);
    }

    @Test
    @DisplayName("잘못된 인증 코드로 계정 활설화를 시도할 경우 예외가 발생한다.")
    void 잘못된_인증_코드로_계정_활설화를_시도할_경우_예외가_발생한다() {
        // given
        User user = User.builder()
                .id(1L)
                .email("lango@test.com")
                .nickname("lango")
                .address("Seoul")
                .status(PENDING)
                .certificationCode("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa")
                .lastLoginAt(100L)
                .build();
        // when & then
        assertThatThrownBy(() -> user.certification("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaab"))
                .isInstanceOf(CertificationCodeNotMatchedException.class);
    }

}
