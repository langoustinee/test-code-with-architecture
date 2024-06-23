package com.example.demo.post.controller;

import com.example.demo.post.domain.PostUpdate;
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

import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.AFTER_TEST_METHOD;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_METHOD;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
@SqlGroup({
        @Sql(value = "/sql/post-controller-test-data.sql", executionPhase = BEFORE_TEST_METHOD),
        @Sql(value = "/sql/delete-all-data.sql", executionPhase = AFTER_TEST_METHOD)
})
public class PostControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper om = new ObjectMapper();

    @Test
    @DisplayName("사용자는 게시글 상세정보를 조회할 수 있다.")
    void 사용자는_게시글_상세정보를_조회할_수_있다() throws Exception {
        // given
        // when
        // then
        mockMvc.perform(get("/api/posts/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.content").value("helloworld"));
    }

    @Test
    @DisplayName("사용자가 존재하지 않는 게시글 상세정보를 조회할 경우 예외가 발생한다.")
    void 사용자가_존재하지_않는_게시글_상세정보를_조회할_경우_예외가_발생한다() throws Exception {
        // given
        // when
        // then
        mockMvc.perform(get("/api/posts/12345"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Posts에서 ID 12345를 찾을 수 없습니다."));
    }

    @Test
    @DisplayName("사용자는 작성한 게시글을 수정할 수 있다.")
    void 사용자는_작성한_게시글을_수정할_수_있다() throws Exception {
        // given
        PostUpdate request = PostUpdate.builder()
                .content("hello spring!")
                .build();
        // when
        // then
        mockMvc.perform(put("/api/posts/1")
                        .header("EMAIL", "lango@test.com")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(request))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.content").value("hello spring!"));
    }


}
