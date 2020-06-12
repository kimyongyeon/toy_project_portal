package com.simple.portal;

import com.simple.portal.biz.v1.board.entity.BoardEntity;
import com.simple.portal.biz.v1.board.entity.CommentEntity;
import com.simple.portal.biz.v1.board.service.BoardService;
import com.simple.portal.biz.v1.board.service.CommentService;
import com.simple.portal.common.storage.StorageProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import java.util.concurrent.Executor;

@EnableAsync
@EnableJpaAuditing
@EnableConfigurationProperties(StorageProperties.class)
@SpringBootApplication
@Slf4j
public class PortalApplication implements ApplicationRunner {

    @Autowired
    BoardService boardService;

    @Autowired
    CommentService commentService;

    @Autowired
    EntityManagerFactory emf;

    @Bean
    public Executor asyncThreadTaskExecutor() {
        ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
        threadPoolTaskExecutor.setCorePoolSize(8);
        threadPoolTaskExecutor.setMaxPoolSize(8);
        threadPoolTaskExecutor.setThreadNamePrefix("portal-pool");
        return threadPoolTaskExecutor;
    }


    public static void main(String[] args) {
        SpringApplication.run(PortalApplication.class, args);
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        for(int i=0; i<3; i++) {
            BoardEntity boardEntity = new BoardEntity();
            CommentEntity commentEntity = new CommentEntity();
            save(boardEntity, commentEntity, i);
        }
    }

    public void jpaSave(BoardEntity boardEntity, CommentEntity commentEntity, int i) {

        boardEntity.setTitle("board title:"+i);
        boardEntity.setContents("board contents:"+i);
        boardEntity.setWriter("board writer:"+i);
        boardService.save(boardEntity);

        commentEntity.setBoardEntity(boardEntity);
        commentEntity.setTitle("comment title:"+i);
        commentEntity.setContents("comment contents:"+i);
        commentEntity.setWriter("comment writer:"+i);
        commentService.writeComment(commentEntity);
    }

    public void save(BoardEntity boardEntity, CommentEntity commentEntity, int i) {

        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {

            tx.begin();

            boardEntity.setTitle("board title:"+i);
            boardEntity.setContents("board contents:"+i);
            boardEntity.setWriter("board writer:"+i);
            em.persist(boardEntity);

            commentEntity.setTitle("comment title:"+i);
            commentEntity.setContents("comment contents:"+i);
            commentEntity.setWriter("comment writer:"+i);
            em.persist(commentEntity);

            boardEntity.addComment(commentEntity);

            tx.commit();

        } catch (Exception e) {
            log.error("error ", e);
            tx.rollback();
        }

    }

}
