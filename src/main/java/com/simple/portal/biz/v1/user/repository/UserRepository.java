package com.simple.portal.biz.v1.user.repository;

import com.simple.portal.biz.v1.user.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

    public Boolean existsUserByUserId(String userId); // 아이디 검사
    public Boolean findByPassword(String password); // 비밀번호 검사
    public UserEntity findByUserId(String userId);

    // 유저 권한 업데이트
    @Query(value="update user set authority='Y' where userId =:user_id", nativeQuery = true)
    boolean updateUserAuth(@Param("user_id") String user_id);

    // 유저 권한 체크
    @Query(value="select authority from user where userId =:user_id", nativeQuery = true)
    char checkUserAuth(@Param("user_id") String user_id);
}