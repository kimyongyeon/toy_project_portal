package com.simple.portal;

import com.simple.portal.biz.v1.board.entity.BoardEntity;
import com.simple.portal.biz.v1.board.entity.CommentEntity;
import com.simple.portal.biz.v1.board.repository.BoardRepository;
import com.simple.portal.biz.v1.board.service.BoardService;
import com.simple.portal.biz.v1.board.service.CommentService;
import com.simple.portal.biz.v1.note.entity.RecvNoteEntity;
import com.simple.portal.biz.v1.note.entity.SendNoteEntity;
import com.simple.portal.biz.v1.note.repository.RecvNoteRepository;
import com.simple.portal.biz.v1.note.repository.SendNoteRepository;
import com.simple.portal.common.storage.StorageProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import java.util.Random;
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
    BoardRepository boardRepository;

    @Autowired
    RecvNoteRepository recvNoteRepository;

    @Autowired
    SendNoteRepository sendNoteRepository;

    @Autowired
    EntityManagerFactory emf;

    @Value("${spring.profile.value}")
    public String profiles;

    @Bean
    @Profile({"default", "dev"})
    public ServerEndpointExporter serverEndpointExporter() {
        return new ServerEndpointExporter();
    }

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

        if (profiles.equals("local")) {
            // 더미 데이터 생성: 게시글, 댓글, 쪽지
            for(int i=0; i<10; i++) {
                BoardEntity boardEntity = new BoardEntity();
                CommentEntity commentEntity = new CommentEntity();
                jpaSave(boardEntity, commentEntity, i);
            }
        }
//
//        EntityManager em = emf.createEntityManager();
//        BoardEntity boardEntity = em.find(BoardEntity.class, 1L);
//        log.debug(boardEntity.toString());
//
//        /**
//         * orphanRemoval = true 로 설정해둔 컬렉션을 삭제하고 새 값으로 설정하려면,
//         * list.clear(), ARepository.saveAndFlush(a), list.addAll(newList) 를 사용해야 한다.
//         * 안 그러면 A collection with cascade="all-delete-orphan" was no longer referenced by the owning entity instance 발생
//         */
//        List commentList = new ArrayList();
//        for(int i=0; i<10; i++) {
//            CommentEntity commentEntity = new CommentEntity();
//            commentEntity.setBoardEntity(boardEntity);
//            commentEntity.setTitle("comment title:"+i);
//            commentEntity.setContents("comment contents:"+i);
//            commentEntity.setWriter("comment writer:"+i);
//            commentList.add(commentEntity);
//        }
//        boardEntity.getCommentEntityList().clear();
//        boardRepository.saveAndFlush(boardEntity);
//        boardEntity.getCommentEntityList().addAll(commentList); // DB로 값이 가느냐 마느냐

    }

    public void jpaSave(BoardEntity boardEntity, CommentEntity commentEntity, int i) {

        long generatedLong = (int)(Math.random() * 100000 +1);
        boardEntity.setTitle("board title:"+i);
        boardEntity.setContents("board contents:"+i);
        boardEntity.setWriter("board writer:"+i);
        boardEntity.setRowDisLike(generatedLong);
        generatedLong =  (int)(Math.random() * 100000 +1);
        boardEntity.setRowLike(generatedLong);
        generatedLong =  (int)(Math.random() * 100000 +1);
        boardEntity.setViewCount(generatedLong);
        boardEntity.addComment(commentEntity);
        boardRepository.save(boardEntity);

//        commentEntity.setBoardEntity(boardEntity);
        commentEntity.setTitle("comment title:"+i);
        commentEntity.setContents("comment contents:"+i);
        commentEntity.setWriter("comment writer:"+i);
        generatedLong = (int)(Math.random() * 100000 +1);
        commentEntity.setRowLike(generatedLong);
        generatedLong = (int)(Math.random() * 100000 +1);
        commentEntity.setRowDisLike(generatedLong);
        generatedLong = (int)(Math.random() * 100000 +1);
        commentEntity.setViewCount(generatedLong);
        commentService.writeComment(commentEntity);
//        generatedLong =  (int)(Math.random() * 3 +1);
//        for (int j=0; j<generatedLong; j++) {
//            commentEntity.setTitle("comment title:"+i);
//            commentEntity.setContents("comment contents:"+i);
//            commentEntity.setWriter("comment writer:"+i);
//            generatedLong = (int)(Math.random() * 100000 +1);
//            commentEntity.setRowLike(generatedLong);
//            generatedLong = (int)(Math.random() * 100000 +1);
//            commentEntity.setRowDisLike(generatedLong);
//            generatedLong = (int)(Math.random() * 100000 +1);
//            commentEntity.setViewCount(generatedLong);
//            commentService.writeComment(commentEntity);
//        }

        RecvNoteEntity recvNoteEntity = new RecvNoteEntity();
        recvNoteEntity.setTitle("recvNote title: " + i);
        recvNoteEntity.setContents("recvNote content: " + i);
        recvNoteEntity.setDelYn(false);
        recvNoteEntity.setViewPoint(0);
        recvNoteEntity.setRevId("recv:" + i);
        recvNoteEntity.setSendId("send:" +i);
        recvNoteRepository.save(recvNoteEntity);

        SendNoteEntity sendNoteEntity = new SendNoteEntity();
        sendNoteEntity.setTitle("recvNote title: " + i);
        sendNoteEntity.setContents("recvNote content: " + i);
        sendNoteEntity.setDelYn(false);
        sendNoteEntity.setViewPoint(0);
        sendNoteEntity.setRevId("recv:" + i);
        sendNoteEntity.setSendId("send:" +i);
        sendNoteRepository.save(sendNoteEntity);

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
