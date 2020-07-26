package com.simple.portal.biz.v1.user.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FollowedList { // 팔로워 출력할때 사용
    int cnt;
    List<FollowData> followed_users;
}
