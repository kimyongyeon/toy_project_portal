package com.simple.portal;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@SpringBootTest(properties = {"testId=userId", "testName=userName"})
@Transactional
@AutoConfigureMockMvc
@Slf4j
public class BoardRestTemplateTest {

    @Value("${testId}")
    private String testId;

    @Value("${testName}")
    private String testName;

    @Autowired
    MockMvc mvc;

    @Autowired
    private WebApplicationContext ctx;

    // 한글깨짐 현상
    @BeforeEach() //Junit4의 @Before
    public void setup() {
        this.mvc = MockMvcBuilders.webAppContextSetup(ctx)
                .addFilters(new CharacterEncodingFilter("UTF-8", true))
                .alwaysDo(print()).build();
    }


    @Test
    void getBoard() throws Exception {

        log.info("##### Properties 테스트 #####");
        log.info("testId : " + testId);
        log.info("testName : " + testName);

        /******** START : MOC MVC test **********/
        log.info("******** START : MOC MVC test **********");
        mvc.perform(get("/v1/api/board/page"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andDo(print());
        log.info("******** END : MOC MVC test **********");
        /******** END : MOC MVC test **********/}

}
