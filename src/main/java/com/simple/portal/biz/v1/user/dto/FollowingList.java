package com.simple.portal.biz.v1.user.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FollowingList { // 팔로잉 출력할때 사용
    int cnt;
    List<FollowData> following_users;
}
