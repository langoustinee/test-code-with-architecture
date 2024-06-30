package com.example.demo.user.controller;

import com.example.demo.mock.TestContainer;
import com.example.demo.user.controller.response.UserResponse;
import com.example.demo.user.domain.UserCreate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

import static com.example.demo.user.domain.UserStatus.PENDING;
import static org.assertj.core.api.Assertions.assertThat;

public class UserCreateControllerTest {

    @Test
    @DisplayName("회원가입 API를 호출하면 새로운 사용자를 등록할 수 있고 회원가입된 사용자는 PENDING 상태가 된다.")
    void 회원가입_API를_호출하면_새로운_사용자를_등록할_수_있고_회원가입된_사용자는_PENDING_상태가_된다() throws Exception {
        // given
        TestContainer testContainer = TestContainer.builder()
                .uuidHolder(() -> "aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa")
                .build();
        UserCreate request = UserCreate.builder()
                .email("lango@test.com")
                .nickname("lango")
                .address("Pangyo")
                .build();
        // when
        ResponseEntity<UserResponse> result = testContainer.userCreateController.create(request);
        // then
        assertThat(result.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(201));
        assertThat(result.getBody()).isNotNull();
        assertThat(result.getBody().getEmail()).isEqualTo("lango@test.com");
        assertThat(result.getBody().getNickname()).isEqualTo("lango");
        assertThat(result.getBody().getLastLoginAt()).isNull();
        assertThat(result.getBody().getStatus()).isEqualTo(PENDING);
        assertThat(testContainer.userRepository.getById(1L).getCertificationCode()).isEqualTo("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa");
    }

}
