package com.simple.portal.biz.v1.user.service;

import com.simple.portal.biz.v1.user.UserConst;
import com.simple.portal.biz.v1.user.exception.UpdateProfileImgFailedException;
import com.simple.portal.biz.v1.user.exception.UserNotFoundException;
import com.simple.portal.biz.v1.user.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;

@Service
@Slf4j
public class ProfileService {

    private S3Service s3Service;
    private UserRepository userRepository;

    public ProfileService(S3Service s3Service, UserRepository userRepository) {
        this.s3Service = s3Service;
        this.userRepository = userRepository;
    }

    @Transactional
    public void updateProfileImgService(String userId, MultipartFile file) {
        try {
            if(!userRepository.existsUserByUserId(userId)) throw new Exception(UserConst.NO_USER); // 존재하지 않는 유저일 경우 exception 처리
            String imgPath = s3Service.upload(userId, file); // s3에 이미지 업로드
            userRepository.updateProfileImg(userId, imgPath); // 이미지 저장경로 디비에 저장
        } catch (Exception e) {
            log.error("[ProfileService] updateProfileImgService Error : " + e.getMessage());
            if(e.getMessage().equals(UserConst.NO_USER)) throw new UserNotFoundException();
            else throw new UpdateProfileImgFailedException();
        }
    }
}
