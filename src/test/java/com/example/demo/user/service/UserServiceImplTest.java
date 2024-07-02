package com.example.demo.user.service;

import com.example.demo.common.domain.exception.CertificationCodeNotMatchedException;
import com.example.demo.common.domain.exception.ResourceNotFoundException;
import com.example.demo.mock.FakeMailSender;
import com.example.demo.mock.FakeUserRepository;
import com.example.demo.mock.TestClockHolder;
import com.example.demo.mock.TestUuidHolder;
import com.example.demo.user.domain.User;
import com.example.demo.user.domain.UserCreate;
import com.example.demo.user.domain.UserUpdate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.example.demo.user.domain.UserStatus.ACTIVE;
import static com.example.demo.user.domain.UserStatus.PENDING;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class UserServiceImplTest {

    private UserServiceImpl userServiceImpl;

    @BeforeEach
    void init() {
        FakeMailSender fakeMailSender = new FakeMailSender();
        FakeUserRepository fakeUserRepository = new FakeUserRepository();
        this.userServiceImpl = UserServiceImpl.builder()
                .userRepository(fakeUserRepository)
                .uuidHolder(new TestUuidHolder("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa"))
                .clockHolder(new TestClockHolder(12345L))
                .certificationService(new CertificationService(fakeMailSender))
                .build();
        fakeUserRepository.save(User.builder()
                .id(1L)
                .email("lango@test.com")
                .nickname("lango")
                .address("Seoul")
                .certificationCode("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa")
                .status(ACTIVE)
                .lastLoginAt(0L)
                .build());
        fakeUserRepository.save(User.builder()
                .id(2L)
                .email("sonny@test.com")
                .nickname("sonny")
                .address("Pangyo")
                .certificationCode("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaab")
                .status(PENDING)
                .lastLoginAt(0L)
                .build());
    }

    @Test
    @DisplayName("getByEmail은 ACTIVE 상태의 사용자를 조회한다.")
    void getByEmail은_ACTIVE_상태의_사용자를_조회한다() {
        // given
        String email = "lango@test.com";
        // when
        User user = userServiceImpl.getByEmail(email);
        // then
        assertThat(user.getNickname()).isEqualTo("lango");
    }

    @Test
    @DisplayName("getByEmail은 PENDING 상태의 사용자를 조회할 수 없다.")
    void getByEmail은_PENDING_상태의_사용자를_조회할_수_없다() {
        // given
        String email = "sonny@test.com";
        // when & then
        assertThatThrownBy(() -> userServiceImpl.getByEmail(email))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    @DisplayName("getById는 ACTIVE 상태의 사용자를 조회한다.")
    void getById는_ACTIVE_상태의_사용자를_조회한다() {
        // given
        // when
        User user = userServiceImpl.getById(1);
        // then
        assertThat(user.getNickname()).isEqualTo("lango");
    }

    @Test
    @DisplayName("getById는 PENDING 상태의 사용자를 조회할 수 없다.")
    void getById는_PENDING_상태의_사용자를_조회할_수_없다() {
        // given
        // when & then
        assertThatThrownBy(() -> userServiceImpl.getById(2))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    @DisplayName("UserCreateDto를 이용해 사용자를 생성할 수 있다.")
    void UserCreateDto를_이용해_사용자를_생성할_수_있다() {
        // given
        UserCreate userCreate = UserCreate.builder()
                .email("woogi@test.com")
                .address("Gyeongi")
                .nickname("woogi")
                .build();
        // when
        User user = userServiceImpl.create(userCreate);
        // then
        assertThat(user.getId()).isNotNull();
        assertThat(user.getStatus()).isEqualTo(PENDING);
        assertThat(user.getCertificationCode()).isEqualTo("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa");
    }

    @Test
    @DisplayName("UserUpdateDto를 이용해 사용자를 수정할 수 있다.")
    void UserUpdateDto를_이용해_사용자를_수정할_수_있다() {
        // given
        UserUpdate updateDto = UserUpdate.builder()
                .address("Jeju")
                .nickname("lango-2")
                .build();
        // when
        userServiceImpl.update(1, updateDto);
        // then
        User user = userServiceImpl.getById(1);
        assertThat(user.getId()).isNotNull();
        assertThat(user.getAddress()).isEqualTo("Jeju");
        assertThat(user.getNickname()).isEqualTo("lango-2");
    }

    @Test
    @DisplayName("사용자가 로그인하면 마지막 로그인 시간이 변경된다.")
    void 사용자가_로그인하면_마지막_로그인_시간이_변경된다() {
        // given
        // when
        userServiceImpl.login(1);
        // then
        User user = userServiceImpl.getById(1);
        assertThat(user.getLastLoginAt()).isEqualTo(12345L);
    }

    @Test
    @DisplayName("PENDING 상태의 사용자는 인증 코드로 ACTIVE 상태로 변경될 수 있다.")
    void PENDING_상태의_사용자는_인증_코드로_ACTIVE_상태로_변경될_수_있다() {
        // given
        // when
        userServiceImpl.verifyEmail(2, "aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaab");
        // then
        User user = userServiceImpl.getById(2);
        assertThat(user.getStatus()).isEqualTo(ACTIVE);
    }

    @Test
    @DisplayName("PENDING 상태의 사용자는 잘못된 인증 코드로 인증을 시도하면 예외를 응답받는다.")
    void PENDING_상태의_사용자는_잘못된_인증_코드로_인증을_시도하면_예외를_응답받는다() {
        // given
        // when
        // then
        assertThatThrownBy(() -> userServiceImpl.verifyEmail(2, "aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaab2"))
                .isInstanceOf(CertificationCodeNotMatchedException.class);
    }

}
