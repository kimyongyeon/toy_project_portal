package com.simple.portal.biz.v1.user.dto;

import com.sun.istack.Nullable;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDto { // 클라이언트에 뿌려줄 값
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 값 자동 생성 ( IDENTITY는 기본 키 생성을 데이터베이스에 위임하는 방식이다.)
    private Long id; // 기본키(PK)로 지정

    @NotBlank(message="아이디는 필수 입력값입니다.")
    private String userId;

    @NotBlank(message="닉네임은 필수 입력값 입니다.")
    @Size(min=2, max=8, message="닉네임을 2~8자 사이로 입력해주세요.")
    private String nickname;

    private String gitAddr;

    private String profileImg;

    private int activityScore;

    private char authority; // 'Y', 'N'

    @Nullable
    private String updated;

    @Override
    public String toString( ) {
        return "id : " + this.id + "\n"
                + "user_id : " + this.userId + "\n"
                + "nickname : " + this.nickname + "\n"
                + "git_addr : " + this.gitAddr + "\n"
                + "profile_img : " + this.profileImg + "\n"
                + "activity_score : " + this.activityScore + "\n"
                + "authority : " + this.authority + "\n"
                + "updated : " + this.updated + "\n";
    };
}
