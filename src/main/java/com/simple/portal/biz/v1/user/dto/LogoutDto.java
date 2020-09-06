package com.simple.portal.biz.v1.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LogoutDto {

    @NotBlank(message="아이디는 필수값 입니다.")
    String userId;
    @NotBlank(message="accessToken은 필수값 입니다.")
    String accessToken;
    @NotBlank(message="refreshToken은 필수값 입니다.")
    String refreshToken;

    @Override
    public String toString( ) {
        return "userId : " + this.userId + "\n"
                +"accessToken : " + this.accessToken + "\n"
                +"refreshToken : " + this.refreshToken + "\n";
    }
}
