package com.example.demo.user.controller;

import com.example.demo.common.domain.exception.CertificationCodeNotMatchedException;
import com.example.demo.common.domain.exception.ResourceNotFoundException;
import com.example.demo.mock.TestContainer;
import com.example.demo.user.controller.response.MyProfileResponse;
import com.example.demo.user.controller.response.UserResponse;
import com.example.demo.user.domain.User;
import com.example.demo.user.domain.UserUpdate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

import static com.example.demo.user.domain.UserStatus.ACTIVE;
import static com.example.demo.user.domain.UserStatus.PENDING;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class UserControllerTest {

    @Test
    @DisplayName("사용자 조회 API를 호출하면 개인정보를 제외한 특정 사용자의 정보를 응답받을 수 있다.")
    void 사용자_조회_API를_호출하면_개인정보를_제외한_특정_사용자의_정보를_응답받을_수_있다() {
        // given
        TestContainer testContainer = TestContainer.builder().build();
        testContainer.userRepository.save(User.builder()
                .id(1L)
                .email("lango@test.com")
                .nickname("lango")
                .address("Seoul")
                .status(ACTIVE)
                .certificationCode("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa")
                .build()
        );
        // when
        ResponseEntity<UserResponse> result = testContainer.userController.getById(1);
        // then
        assertThat(result.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(200));
        assertThat(result.getBody().getId()).isEqualTo(1);
        assertThat(result.getBody().getEmail()).isEqualTo("lango@test.com");
        assertThat(result.getBody().getNickname()).isEqualTo("lango");
        assertThat(result.getBody().getStatus()).isEqualTo(ACTIVE);
    }

    @Test
    @DisplayName("존재하지 않는 사용자의 아이디로 API를 호출하면 응답 상태코드는 404를 반환한다.")
    void 존재하지_않는_사용자의_아이디로_API를_호출하면_응답_상태코드는_404를_반환한다() {
        // given
        TestContainer testContainer = TestContainer.builder().build();
        // when & then
        assertThatThrownBy(() -> testContainer.userController.getById(1))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    @DisplayName("사용자는 인증 코드로 인증 활성화 API를 호출하면 계정을 활성화시킬 수 있다.")
    void 사용자는_인증_코드로_인증_활성화_API를_호출하면_계정을_활성화시킬_수_있다() {
        // given
        TestContainer testContainer = TestContainer.builder().build();
        testContainer.userRepository.save(User.builder()
                .id(1L)
                .email("lango@test.com")
                .nickname("lango")
                .address("Seoul")
                .status(PENDING)
                .certificationCode("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa")
                .build()
        );
        // when
        ResponseEntity<Void> result = testContainer.userController
                .verifyEmail(1L, "aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa");
        // then
        assertThat(result.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(302));
        assertThat(testContainer.userRepository.getById(1L).getStatus()).isEqualTo(ACTIVE);
    }

    @Test
    @DisplayName("사용자가 인증 코드로 인증 활성화 API를 호출할 때 인증 코드가 일치하지 않을 경우 권한 없음 예외를 반환한다.")
    void 사용자가_인증_코드로_인증_API를_호출할_때_인증_코드가_일치하지_않을_경우_권한_없음_예외를_반환한다() {
        // given
        TestContainer testContainer = TestContainer.builder().build();
        testContainer.userRepository.save(User.builder()
                .id(1L)
                .email("lango@test.com")
                .nickname("lango")
                .address("Seoul")
                .status(PENDING)
                .certificationCode("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa")
                .build()
        );
        // when & then
        assertThatThrownBy(() -> testContainer.userController
                .verifyEmail(1L, "aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaab"))
                .isInstanceOf(CertificationCodeNotMatchedException.class);
    }

    @Test
    @DisplayName("사용자는 내 정보 조회 API를 호출하면 개인정보인 주소도 응답으로 받을 수 있다.")
    void 사용자는_내_정보_조회_API를_호출하면_개인정보인_주소도_응답으로_받을_수_있다() {
        // given
        TestContainer testContainer = TestContainer.builder()
                .clockHolder(() -> 12345L)
                .build();
        testContainer.userRepository.save(User.builder()
                .id(1L)
                .email("lango@test.com")
                .nickname("lango")
                .address("Seoul")
                .status(ACTIVE)
                .certificationCode("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa")
                .build()
        );
        // when
        ResponseEntity<MyProfileResponse> result = testContainer.userController.getMyInfo("lango@test.com");
        // then
        assertThat(result.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(200));
        assertThat(result.getBody()).isNotNull();
        assertThat(result.getBody().getEmail()).isEqualTo("lango@test.com");
        assertThat(result.getBody().getNickname()).isEqualTo("lango");
        assertThat(result.getBody().getAddress()).isEqualTo("Seoul");
        assertThat(result.getBody().getLastLoginAt()).isEqualTo(12345L);
        assertThat(result.getBody().getStatus()).isEqualTo(ACTIVE);
    }

    @Test
    @DisplayName("사용자는 내 정보 수정 API를 호출하면 내 정보를 수정할 수 있다.")
    void 사용자는_내_정보_수정_API를_호출하면_내_정보를_수정할_수_있다() {
        // given
        TestContainer testContainer = TestContainer.builder()
                .build();
        testContainer.userRepository.save(User.builder()
                .id(1L)
                .email("lango@test.com")
                .nickname("lango")
                .address("Seoul")
                .status(ACTIVE)
                .certificationCode("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa")
                .build()
        );
        // when
        ResponseEntity<MyProfileResponse> result = testContainer.userController.updateMyInfo(
                "lango@test.com",
                UserUpdate.builder()
                        .address("Pangyo")
                        .nickname("lango-new")
                        .build()
        );
        // then
        assertThat(result.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(200));
        assertThat(result.getBody()).isNotNull();
        assertThat(result.getBody().getEmail()).isEqualTo("lango@test.com");
        assertThat(result.getBody().getNickname()).isEqualTo("lango-new");
        assertThat(result.getBody().getAddress()).isEqualTo("Pangyo");
        assertThat(result.getBody().getStatus()).isEqualTo(ACTIVE);
    }

}
