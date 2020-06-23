package com.simple.portal.biz.v1.board.service;

/**
 * 비슷한 기능 모음
 * - 좋아요/싫어요
 */
public interface BaseService {
    // 좋아요
    void setLikeTransaction(Long id);
    // 싫어요
    void setDisLikeTransaction(Long id);
    // 증가
    Long increase(Long curVal);
    // 감소
    Long decrease(Long currVal);
}
