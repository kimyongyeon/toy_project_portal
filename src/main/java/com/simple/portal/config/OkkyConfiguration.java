package com.simple.portal.config;

import com.simple.portal.common.ApiResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OkkyConfiguration {

    @Bean
    public ApiResponse apiResponse() { //  response bean
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setCode("200");
        apiResponse.setMsg("success");
        return apiResponse;
    }
}
