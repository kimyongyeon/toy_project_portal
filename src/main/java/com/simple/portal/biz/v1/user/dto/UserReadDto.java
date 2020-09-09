package com.simple.portal.biz.v1.user.dto;

import com.sun.istack.Nullable;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserReadDto { // 클라이언트에 뿌려줄 값

    private Long id; // 기본키(PK)로 지정
    private String userId;
    private String email;

    @Column(nullable = true)
    private String nickname;

    private String gitAddr;
    private String profileImg;
    private int activityScore;
    private char authority; // 'Y', 'N'
    private String platform;

    @Nullable
    private String created;

    @Nullable
    private String updated;

    // following, followed 리스트는 특정 조회 즉, 내정보 확인할때만 필요.
    @Nullable
    private FollowedList followedList;

    @Nullable
    private FollowingList followingList;

    @Override
    public String toString( ) {
        return "id : " + this.id + "\n"
                + "user_id : " + this.userId + "\n"
                + "email : " + this.email + "\n"
                + "nickname : " + this.nickname + "\n"
                + "git_addr : " + this.gitAddr + "\n"
                + "profile_img : " + this.profileImg + "\n"
                + "activity_score : " + this.activityScore + "\n"
                + "authority : " + this.authority + "\n"
                + "platform : " + this.platform + "\n"
                + "created : " + this.created + "\n"
                + "updated : " + this.updated + "\n";
    };
}
