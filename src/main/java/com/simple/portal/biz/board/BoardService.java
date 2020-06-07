package com.simple.portal.biz.board;

import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
public class BoardService {

    private List<BoardEntity> list = new ArrayList<>();
    AtomicInteger atomicInteger = new AtomicInteger();

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

    public List list() {
        return this.list;
    }

    public BoardEntity detail(BoardEntity boardEntity) {
        return list.stream().filter(b -> b.getId() == boardEntity.getId()).findFirst().get();
    }

    public void insert(BoardEntity boardEntity) {
        boardEntity.setId(atomicInteger.addAndGet(list.size()+1));
        list.add(boardEntity);
    }

    public void update(BoardEntity boardEntity) {
        list.stream().filter(b -> b.getId() == boardEntity.getId())
                .map(b -> {
                    b.setTitle(boardEntity.getTitle());
                    b.setContents(boardEntity.getContents());
                    return b;
                });
    }

    public void delete(BoardEntity boardEntity) {
        List newList = list.stream().filter(b -> b.getId() != boardEntity.getId()).collect(Collectors.toList());
        list = null;
        list = newList;
    }

}
