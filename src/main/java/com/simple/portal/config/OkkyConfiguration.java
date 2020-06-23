package com.simple.portal.config;

import com.simple.portal.biz.v1.board.exception.InputRequiredException;
import com.simple.portal.common.ApiResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.BindingResult;

@Configuration
public class OkkyConfiguration {
    @Bean
    public ApiResponse apiResponse() { //
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setCode("200");
        apiResponse.setMsg("success");
        return apiResponse;
    }

}
