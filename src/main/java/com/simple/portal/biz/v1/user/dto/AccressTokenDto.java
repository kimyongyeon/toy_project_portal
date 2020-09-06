package com.simple.portal.biz.v1.user.dto;

import lombok.*;

import javax.persistence.Access;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AccressTokenDto {
    String userId;
    char Role;
    Boolean expired; // 해당 토큰이 만료됬는지 ?
    Long tokenExpiredRemainTime; // 토큰 유효시간 까지 남은 시간.
}
