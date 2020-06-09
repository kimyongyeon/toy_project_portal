package com.simple.portal;

import com.mysema.query.jpa.impl.JPAQuery;
import com.simple.portal.biz.v1.board.dto.BoardDTO;
import com.simple.portal.biz.v1.board.entity.BoardEntity;
import com.simple.portal.biz.v1.board.entity.QBoardEntity;
import com.simple.portal.biz.v1.board.service.BoardService;
import com.simple.portal.common.storage.StorageProperties;
import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.asm.Advice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.concurrent.Executor;

@EnableAsync
@EnableJpaAuditing
@EnableConfigurationProperties(StorageProperties.class)
@SpringBootApplication
@Slf4j
public class PortalApplication implements ApplicationRunner, CommandLineRunner {

    @Autowired
    BoardService boardService;



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
        for(int i=0; i<10; i++) {
            BoardDTO boardDTO = new BoardDTO();
            boardDTO.setTitle("title " + i);
            boardDTO.setContents("contents: " + i);
            boardDTO.setWriter("writer: " + i);
            boardService.insert(boardDTO);
        }

    }

    @Override
    public void run(String... args) throws Exception {

    }
}
