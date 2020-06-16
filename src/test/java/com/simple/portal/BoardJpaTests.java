package com.simple.portal;

import com.querydsl.core.types.Predicate;
import com.simple.portal.biz.v1.board.entity.QBoardEntity;
import com.simple.portal.biz.v1.board.repository.BoardRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ActiveProfiles("test")
@Transactional
@AutoConfigureMockMvc
@Slf4j
@SpringBootTest(
        properties = { "testId=tonyuserId", "testName=tony" } ,
        //classes = {TestJpaRestController.class, MemberService.class} ,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT

)
public class BoardJpaTests {

    @Autowired
    BoardRepository boardRepository;
    private final QBoardEntity qBoardEntity = QBoardEntity.boardEntity;

    @Test
    public void predicate_test_001() {
        //given
        final Predicate predicate = qBoardEntity.writer.eq("1");
        //when
        final boolean exists = boardRepository.exists(predicate);
        //then
        assertThat(exists).isTrue();
    }

    @Test
    public void predicate_test_002() {
        //given
        final Predicate predicate = qBoardEntity.title.eq("???");
        //when
        final boolean exists = boardRepository.exists(predicate);
        //then
        assertThat(exists).isFalse();
    }

    @Test
    public void predicate_test_003() {
        //given
        final Predicate predicate = qBoardEntity.contents.eq("%test%");
        //when
        final long count = boardRepository.count(predicate);
        //then
        assertThat(count).isGreaterThan(1);
    }


}
