package com.example.demo.user.controller;

import com.example.demo.user.domain.UserUpdate;
import com.example.demo.user.infrastructure.UserEntity;
import com.example.demo.user.infrastructure.UserJpaRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.web.servlet.MockMvc;

import static com.example.demo.user.domain.UserStatus.ACTIVE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.AFTER_TEST_METHOD;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_METHOD;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
@SqlGroup({
        @Sql(value = "/sql/user-controller-test-data.sql", executionPhase = BEFORE_TEST_METHOD),
        @Sql(value = "/sql/delete-all-data.sql", executionPhase = AFTER_TEST_METHOD)
})
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserJpaRepository userJpaRepository;

    private final ObjectMapper om = new ObjectMapper();

    @Test
    @DisplayName("사용자 조회 API를 호출하면 개인정보를 제외한 특정 사용자의 정보를 응답받을 수 있다.")
    void 사용자_조회_API를_호출하면_개인정보를_제외한_특정_사용자의_정보를_응답받을_수_있다() throws Exception {
        // given
        // when
        // then
        mockMvc.perform(get("/api/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.email").value("lango@test.com"))
                .andExpect(jsonPath("$.nickname").value("lango"))
                .andExpect(jsonPath("$.address").doesNotExist())
                .andExpect(jsonPath("$.status").value("ACTIVE"));
    }

    @Test
    @DisplayName("존재하지 않는 사용자의 아이디로 API를 호출하면 응답 상태코드는 404를 반환한다.")
    void 존재하지_않는_사용자의_아이디로_API를_호출하면_응답_상태코드는_404를_반환한다() throws Exception {
        // given
        // when
        // then
        mockMvc.perform(get("/api/users/12345"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Users에서 ID 12345를 찾을 수 없습니다."));
    }

    @Test
    @DisplayName("사용자는 인증 코드로 인증 활성화 API를 호출하면 계정을 활성화시킬 수 있다.")
    void 사용자는_인증_코드로_인증_활성화_API를_호출하면_계정을_활성화시킬_수_있다() throws Exception {
        // given
        // when
        // then
        mockMvc.perform(get("/api/users/2/verify")
                        .queryParam("certificationCode", "aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaab")
                )
                .andExpect(status().isFound());
        UserEntity userEntity = userJpaRepository.findById(2L).get();
        assertThat(userEntity.getStatus()).isEqualTo(ACTIVE);
    }

    @Test
    @DisplayName("사용자가 인증 코드로 인증 활성화 API를 호출할 때 인증 코드가 일치하지 않을 경우 권한 없음 예외를 반환한다.")
    void 사용자가_인증_코드로_인증_API를_호출할_때_인증_코드가_일치하지_않을_경우_권한_없음_예외를_반환한다() throws Exception {
        // given
        // when
        // then
        mockMvc.perform(get("/api/users/2/verify")
                        .queryParam("certificationCode", "aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaab1")
                )
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("사용자는 내 정보 조회 API를 호출하면 개인정보인 주소도 응답으로 받을 수 있다.")
    void 사용자는_내_정보_조회_API를_호출하면_개인정보인_주소도_응답으로_받을_수_있다() throws Exception {
        // given
        // when
        // then
        mockMvc.perform(get("/api/users/me")
                        .header("EMAIL", "lango@test.com")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.email").value("lango@test.com"))
                .andExpect(jsonPath("$.nickname").value("lango"))
                .andExpect(jsonPath("$.address").value("Seoul"))
                .andExpect(jsonPath("$.status").value("ACTIVE"));
    }

    @Test
    @DisplayName("사용자는 내 정보 수정 API를 호출하면 내 정보를 수정할 수 있다.")
    void 사용자는_내_정보_수정_API를_호출하면_내_정보를_수정할_수_있다() throws Exception {
        // given
        UserUpdate request = UserUpdate.builder()
                .nickname("lango-new")
                .address("Pangyo")
                .build();
        // when
        // then
        mockMvc.perform(put("/api/users/me")
                        .header("EMAIL", "lango@test.com")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(request))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.email").value("lango@test.com"))
                .andExpect(jsonPath("$.nickname").value("lango-new"))
                .andExpect(jsonPath("$.address").value("Pangyo"))
                .andExpect(jsonPath("$.status").value("ACTIVE"));
    }

}
