package com.simple.portal.biz.v1.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class FollowData { // 팔로우/팔로잉 유저들 출력할때 pk와 닉네임 같이 출력
    private String userId;
    private String profileImg;
}
