package com.simple.portal;

import com.google.gson.Gson;
import com.simple.portal.biz.v1.note.dto.NoteDTO;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@Transactional
@SpringBootTest
@AutoConfigureMockMvc
@Slf4j
public class NoteRestTemplateTest {

    @Autowired
    MockMvc mvc;

    @BeforeEach()
    void 초기화() {
    }

    @Test
    void 보낸쪽지목록() throws Exception {
        mvc.perform(get("/v1/api/note/send?userId=superman"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andDo(print());
    }

    @Test
    void 받은쪽지목록() throws Exception {
        mvc.perform(get("/v1/api/note/receive?userId=batman"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andDo(print());
    }

    @Test
    void 쪽지상세보기() throws Exception {

        mvc.perform(get("/v1/api/note/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andDo(print());
    }

    @Test
    void 쪽지쓰기() throws Exception {
        mvc.perform(post("/v1/api/note")
                .content(new Gson().toJson(
                        NoteDTO.builder()
                                .title("쪽지 보냅니다.")
                                .contents("쪽지 내용은 없어요.")
                                .viewPoint(0)
                                .build()
                )))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andDo(print());
    }

    @Test
    void 쪽지수정() throws Exception {
        mvc.perform(put("/v1/api/note")
                .content(new Gson().toJson(
                        NoteDTO.builder()
                                .build()
                )))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andDo(print());
    }

    @Test
    void 쪽지삭제() throws Exception {
        mvc.perform(delete("/v1/api/note")
                .content(new Gson().toJson(
                        NoteDTO.builder()
                                .build()
                )))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andDo(print());
    }

}
