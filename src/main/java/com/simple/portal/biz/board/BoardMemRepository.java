package com.simple.portal.biz.board;

import lombok.Data;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Profile("default")
@Repository
@Data
public class BoardMemRepository {

    private List<BoardEntity> list = new ArrayList<>();

    @PostConstruct
    public void init() {
        BoardEntity boardEntity;
        for(int i=0; i<10; i++) {
            boardEntity = new BoardEntity();
            boardEntity.setId(i);
            boardEntity.setTitle("제목입니다. " + i);
            boardEntity.setContents("내용입니다. " + i);
            list.add(boardEntity);
        }
    }
}
