package com.simple.portal.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.IOException;

// 회원가입 안내 메일 or 신규 비밀번호 안내 메일
@Component
public class CustomMailSender {

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private SpringTemplateEngine springTemplateEngine;

    public void sendJoinMail(String title, String userId, String email) throws MessagingException, IOException {

        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        // 메일 제목 설정
        helper.setSubject(title);
        //수신자 설정
        helper.setTo(email);
        //템플릿에 전달할 데이터 설정
        Context context = new Context();
        context.setVariable("user_id", userId);
        //메일 내용 설정 : 템플릿 프로세스
        String html = springTemplateEngine.process("mail-template", context);
        helper.setText(html, true);

        // 메일 보내기
        javaMailSender.send(message);
    }

    public void sendNewPwMail(String title, String userEmail, String userId, String newPassword) throws MessagingException {

        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        // 메일 제목 설정
        helper.setSubject(title);
        //수신자 설정
        helper.setTo(userEmail);
        //템플릿에 전달할 데이터 설정
        Context context = new Context();
        context.setVariable("user_id", userId);
        context.setVariable("new_password", newPassword);
        //메일 내용 설정 : 템플릿 프로세스
        String html = springTemplateEngine.process("mail-new-password-template", context);
        helper.setText(html, true);

        // 메일 보내기
        javaMailSender.send(message);
    }
}