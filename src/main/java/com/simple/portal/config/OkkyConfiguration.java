package com.simple.portal.config;

import com.simple.portal.biz.v1.board.exception.InputRequiredException;
import com.simple.portal.common.ApiResponse;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.freemarker.FreeMarkerAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.BindingResult;

@Configuration
@EnableAutoConfiguration(exclude = { FreeMarkerAutoConfiguration.class })
public class OkkyConfiguration {

    @Bean
    public ApiResponse apiResponse() { //  response bean
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setCode("200");
        apiResponse.setMsg("success");
        return apiResponse;
    }

}
