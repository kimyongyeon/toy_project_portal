package com.simple.portal.config;

import com.simple.portal.common.ApiResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OkkyConfiguration {
    @Bean
    public ApiResponse successApiResponse() { // 성공 케이스 response
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setCode("200");
        apiResponse.setMsg("success");
        return apiResponse;
    }

    @Bean
    public ApiResponse errorApiResponse() { // 실패 케이스 response
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setCode("500");
        apiResponse.setMsg("error");
        return apiResponse;
    }
}
