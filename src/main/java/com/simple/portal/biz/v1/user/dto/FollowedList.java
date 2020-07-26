package com.simple.portal.biz.v1.user.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FollowedList {
    int cnt;
    List<Long> followed_users;
}
