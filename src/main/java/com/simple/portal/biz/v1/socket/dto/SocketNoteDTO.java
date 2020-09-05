package com.simple.portal.biz.v1.socket.dto;

import com.simple.portal.biz.v1.board.dto.BasePageDTO;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SocketNoteDTO {
    private long noteNotReadCount;
}
