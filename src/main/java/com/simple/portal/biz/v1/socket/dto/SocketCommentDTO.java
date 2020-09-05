package com.simple.portal.biz.v1.socket.dto;

import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class SocketCommentDTO {
    private List boardId;
    private int commentCnt = 0;
}
