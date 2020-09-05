package com.simple.portal.biz.v1.board.dto;

import com.simple.portal.biz.v1.board.entity.BOARD_TYPE;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BoardSearchDTO extends BasePageDTO {
    private String keyword = "";
    private String gb; // title, contents, writer
    private String boardType; // 게시판 타입

}
