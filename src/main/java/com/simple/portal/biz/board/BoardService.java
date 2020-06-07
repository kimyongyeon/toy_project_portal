package com.simple.portal.biz.board;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
public class BoardService {

    AtomicInteger atomicInteger = new AtomicInteger();

    @Autowired
    BoardMemRepository boardMemRepository;

    public List list() {
        return boardMemRepository.getList();
    }

    public BoardEntity detail(BoardEntity boardEntity) {
        return boardMemRepository.getList().stream().filter(b -> b.getId() == boardEntity.getId()).findFirst().get();
    }

    public void insert(BoardEntity boardEntity) {
        boardEntity.setId(atomicInteger.addAndGet(boardMemRepository.getList().size()+1));
        boardMemRepository.getList().add(boardEntity);
    }

    public void update(BoardEntity boardEntity) {
        boardMemRepository.getList().stream().filter(b -> b.getId() == boardEntity.getId())
                .map(b -> {
                    b.setTitle(boardEntity.getTitle());
                    b.setContents(boardEntity.getContents());
                    return b;
                });
    }

    public void delete(BoardEntity boardEntity) {
        boardMemRepository.getList().stream().filter(b -> b.getId() != boardEntity.getId()).collect(Collectors.toList());
    }

}
