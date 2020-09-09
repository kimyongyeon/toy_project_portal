package com.simple.portal.biz.v1.user.entity;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.sun.istack.Nullable;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Date;

@Entity // 테이블과 매핑됨
@Table(name="user", indexes= {@Index(name="user_id_index", columnList = "user_id", unique = true)})
@Getter
@Setter
@NoArgsConstructor
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 값 자동 생성 ( IDENTITY는 기본 키 생성을 데이터베이스에 위임하는 방식이다.)
    private Long id; // 기본키(PK)로 지정

    @Column(name="user_id", nullable=false, updatable = false, unique = true)
    private String userId;

    @Column(nullable = false, updatable = false)
    private String email;

    @Column(nullable = true)
    private String nickname;

    @Column(nullable = false)
    private String password;

    @Column(name="git_addr", nullable = true)
    private String gitAddr;

    @Column(name="profile_img", nullable = true)
    private String profileImg;

    @Column(name="activity_score")
    private int activityScore;

    @Column(columnDefinition = "CHAR(1) DEFAULT 'N'")
    private char authority; // 'Y', 'N'

    @Column(nullable = false)
    private String platform;

    @Column(nullable = true)
    private String created;

    @Column(nullable = true)
    private String updated;

    @Column(name="last_login_time", nullable = true)
    private String lastLoginTime;

    @Builder
    public UserEntity(Long id, String userId, String email, String nickname, String password, String gitAddr, String profileImg, int activityScore, char authority, String platform, String created, String updated, String lastLoginTime) {
        this.id = id;
        this.userId = userId;
        this.email = email;
        this.nickname = nickname;
        this.password = password;
        this.gitAddr = gitAddr;
        this.profileImg = profileImg;
        this.platform = platform;
        this.activityScore = activityScore;
        this.authority = authority;
        this.created = created;
        this.updated = updated;
        this.lastLoginTime = lastLoginTime;
    }

    @Override
    public String toString( ) {
        return "id : " + this.id + "\n"
                + "user_id : " + this.userId + "\n"
                + "email : " + this.email + "\n"
                + "nickname : " + this.nickname + "\n"
                + "password : " + this.password + "\n"
                + "git_addr : " + this.gitAddr + "\n"
                + "profile_img : " + this.profileImg + "\n"
                + "platform : " + this.platform + "\n"
                + "activity_score : " + this.activityScore + "\n"
                + "authority : " + this.authority + "\n"
                + "created : " + this.created + "\n"
                + "updated : " + this.updated + "\n"
                + "lastLoginTime : " + this.lastLoginTime + "\n";
    };
}