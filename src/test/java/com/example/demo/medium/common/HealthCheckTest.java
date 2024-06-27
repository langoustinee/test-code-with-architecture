package com.example.demo.medium.common;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
public class HealthCheckTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("헬스 체크 API 엔드포인트 호출시 응답 상태코드는 200을 반환한다.")
    void 헬스_체크_API_엔드포인트_호출시_응답_상태코드는_200을_반환한다() throws Exception {
        // given
        // when
        // then
        mockMvc.perform(MockMvcRequestBuilders.get("/health_check.html"))
                .andExpect(status().isOk());
    }

}
