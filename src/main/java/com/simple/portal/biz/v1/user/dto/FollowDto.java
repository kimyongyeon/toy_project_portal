package com.simple.portal.biz.v1.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FollowDto { // 팔로잉 팔로우 할때 파라미터로 받음

    @NotNull(message = "팔로잉 유저 id는 필수 값 입니다.")
    private String following_id; // 팔로잉 하는 유저 id
    @NotNull(message = "팔로워 유저 id는 필수 값 입니다.")
    private String followed_id; // 팔로우 되는 유저 id


    @Override
    public String toString( ) {
        return "following_id : " + this.following_id + "\n"
                + "followed_id : " + this.followed_id + "\n";
    };
}
