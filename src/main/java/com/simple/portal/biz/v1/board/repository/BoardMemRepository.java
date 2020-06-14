package com.simple.portal.biz.v1.board.repository;

import com.simple.portal.biz.v1.board.entity.BoardEntity;
import lombok.Data;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@Profile({"default", "dev"})
@Repository
@Data
public class BoardMemRepository {

    private List<BoardEntity> list = new ArrayList<>();

    @PostConstruct
    public void init() {
        BoardEntity boardEntity;
        for(long i=0; i<10; i++) {
            boardEntity = new BoardEntity();
            boardEntity.setId(i);
            boardEntity.setTitle("제목입니다. " + i);
            boardEntity.setContents("내용입니다. " + i);
            list.add(boardEntity);
        }
    }
}
