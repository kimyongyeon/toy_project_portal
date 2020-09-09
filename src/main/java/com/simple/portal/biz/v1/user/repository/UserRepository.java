package com.simple.portal.biz.v1.user.repository;

import com.simple.portal.biz.v1.user.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

    public Boolean existsUserByUserId(String userId); // 아이디 검사
    public Boolean findByPassword(String password); // 비밀번호 검사
    public UserEntity findByUserId(String userId);
    public Boolean existsByEmail(String email); // 이메일 검사
    public UserEntity findByEmailAndPlatform(String email, String platform); // email, platform ( oauth 로그인 key값 )

    // 유저 권한 업데이트
    @Modifying
    @Transactional
    @Query(value="update user set authority = 'Y' where user_id = ?1", nativeQuery = true)
    void updateUserAuth(String user_id);

    // 유저 권한 체크
    @Query(value="select authority from user where user_id = ?1", nativeQuery = true)
    char checkUserAuth(String user_id);

    //유저 비밀번호 업데이트
    @Modifying
    @Transactional
    @Query(value="update user set password = :newPassword where user_id = :userId", nativeQuery = true)
    void updatePassword(String userId, String newPassword);

    // 유저 pk id 리스트를 통해 id에 대한 닉네임 조회

    // 유저 활동점수 업데이트
    @Modifying
    @Transactional
    @Query(value="update user set activity_score = :score where user_id = :userId", nativeQuery = true)
    void updateActivityScore(String userId, int score);

    // 유저 가장 최근 로그인 시간 업데이트
    @Modifying
    @Transactional
    @Query(value="update user set last_login_time = :lastLoginTime where user_id = :userId", nativeQuery = true)
    void updateLastLoginTime(String userId, String lastLoginTime);

    //프로필 이미지경로 업데이트
    // 유저 가장 최근 로그인 시간 업데이트
    @Modifying
    @Transactional
    @Query(value="update user set profile_img = :imgUrl where user_id = :userId", nativeQuery = true)
    void updateProfileImg(String userId, String imgUrl);

}