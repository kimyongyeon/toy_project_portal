package com.simple.portal.biz.v1.socket.dto;

import lombok.*;

import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class SocketFollowDTO {
    private List followId;
    private int newFollowingCount = 0;
}
