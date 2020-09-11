package com.simple.portal.biz.v1.board.repository;

import com.simple.portal.biz.v1.board.entity.FeelEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface FeelRepository extends JpaRepository<FeelEntity, Long> {

    // 현재 유저의 해당게시물에 대한 감정 개수 조회
    FeelEntity findByUserIdAndBoardIdAndFeelState(String userId, Long boardId, String feelState);

    // 선택한 게시물의 감정별 총 개수 조회
    @Query(value="SELECT SUM(COUNT) as cnt FROM tb_board_feel WHERE FEEL_STATE=? AND BOARD_ID=?", nativeQuery= true)
    Long selectTocalCount(String feelState, Long boardId);
}
