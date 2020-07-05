package com.simple.portal.biz.v1.user.entity;


import com.sun.istack.Nullable;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Date;

@Entity // 테이블과 매핑됨
@Table(name="user")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 값 자동 생성 ( IDENTITY는 기본 키 생성을 데이터베이스에 위임하는 방식이다.)
    private Long id; // 기본키(PK)로 지정

    @Column(name="user_id")
    @Pattern(regexp = "^[_a-z0-9-]+(.[_a-z0-9-]+)*@(?:\\w+\\.)+\\w+$",
            message="아이디(이메일) 형식에 맞지 않습니다.")
    @NotBlank(message="아이디는 필수 입력값입니다.")
    private String userId;

    @NotBlank(message="닉네임은 필수 입력값 입니다.")
    @Size(min=2, max=8, message="닉네임을 2~8자 사이로 입력해주세요.")
    private String nickname;

    @NotBlank(message="비밀번호는 필수 입력값입니다.")
    private String password;

    @Column(name="git_addr")
    private String gitAddr;

    @Column(name="profile_img")
    private String profileImg;

    @Column(name="activity_score")
    private int activityScore;

    @Column(columnDefinition = "CHAR(1) DEFAULT 'N'")
    private char authority; // 'Y', 'N'

    @Nullable
    private LocalDateTime created;

    @Nullable
    private LocalDateTime updated;

    @Builder
    public UserEntity(String userId, String nickname, String password, String gitAddr, String profileImg, int activityScore, char authority, LocalDateTime created, LocalDateTime updated) {
        this.userId = userId;
        this.nickname = nickname;
        this.password = password;
        this.gitAddr = gitAddr;
        this.profileImg = profileImg;
        this.activityScore = activityScore;
        this.authority = authority;
        this.created = created;
        this.updated = updated;
    }

    @Override
    public String toString( ) {
        return "id : " + this.id + "\n"
                + "user_id : " + this.userId + "\n"
                + "nickname : " + this.nickname + "\n"
                + "password : " + this.password + "\n"
                + "git_addr : " + this.gitAddr + "\n"
                + "profile_img : " + this.profileImg + "\n"
                + "activity_score : " + this.activityScore + "\n"
                + "authority : " + this.authority + "\n"
                + "created : " + this.created + "\n"
                + "updated : " + this.updated + "\n";
    };
}