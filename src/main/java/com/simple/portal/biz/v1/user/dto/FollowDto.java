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
public class FollowDto {

    @NotNull(message = "팔로잉 유저 id는 필수 값 입니다.")
    private Long following_id; // 팔로잉 하는 유저 id
    @NotNull(message = "팔로워 유저 id는 필수 값 입니다.")
    private Long followed_id; // 팔로우 되는 유저 id


    @Override
    public String toString( ) {
        return "followed_ing : " + this.following_id + "\n"
                + "followed_id : " + this.followed_id + "\n";
    };
}
