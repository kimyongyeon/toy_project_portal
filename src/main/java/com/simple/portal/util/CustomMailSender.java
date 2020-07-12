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

@Component
public class CustomMailSender {

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private SpringTemplateEngine springTemplateEngine;

    public void sendMail(String userId) throws MessagingException, IOException {

        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        // 메일 제목 설정
        helper.setSubject("Okky 회원 가입 완료 메일 !");
        //수신자 설정
        helper.setTo(userId);
        //템플릿에 전달할 데이터 설정
        Context context = new Context();
        context.setVariable("user_id", userId);
        //메일 내용 설정 : 템플릿 프로세스
        String html = springTemplateEngine.process("mail-template", context);
        helper.setText(html, true);

        // 메일 보내기
        javaMailSender.send(message);
    }
}