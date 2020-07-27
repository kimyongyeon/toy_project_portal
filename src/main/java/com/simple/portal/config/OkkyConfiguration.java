package com.simple.portal.config;

import com.simple.portal.common.ApiResponse;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.freemarker.FreeMarkerAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

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

    @Bean
    public WebMvcConfigurer webMvcConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOrigins("*")
                        .allowedMethods(HttpMethod.POST.name())
                        .allowedMethods(HttpMethod.GET.name())
                        .allowedMethods(HttpMethod.PUT.name())
                        .allowedMethods(HttpMethod.DELETE.name())
                        .allowCredentials(false)
                        .maxAge(3600);
            }
        };
    }

}
