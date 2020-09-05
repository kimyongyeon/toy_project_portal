package com.simple.portal.biz.v1.socket.dto;

import com.simple.portal.biz.v1.board.dto.BasePageDTO;
import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SocketFollowDTO {
    private List userId;
    private int newFollowingCount;
}
