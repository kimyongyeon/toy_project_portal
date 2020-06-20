package com.simple.portal.biz.v1.user.repository;

import com.simple.portal.biz.v1.user.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

    public Boolean existsUserByUserId(String userId); // 아이디 검사
    public Boolean findByPassword(String password); // 비밀번호 검사
    public UserEntity findByUserId(String userId);
}