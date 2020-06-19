package com.simple.portal.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    /**
     * hasIpAddress(ip) – 접근자의 IP주소가 매칭 하는지 확인합니다.
     * hasRole(role) – 역할이 부여된 권한(Granted Authority)과 일치하는지 확인합니다.
     * hasAnyRole(role) – 부여된 역할 중 일치하는 항목이 있는지 확인합니다.
     * ex) access = "hasAnyRole(‘ROLE_USER’,’ROLE_ADMIN’)"
     * permitAll – 모든 접근자를 항상 승인합니다.
     * denyAll – 모든 사용자의 접근을 거부합니다.
     * anonymous – 사용자가 익명 사용자인지 확인합니다.
     * authenticated – 인증된 사용자인지 확인합니다.
     * rememberMe – 사용자가 remember me를 사용해 인증했는지 확인합니다.
     * fullyAuthenticated – 사용자가 모든 크리덴셜을 갖춘 상태에서 인증했는지 확인합니다.
     *
     * @param http
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().and()
            .csrf()
            .disable()// rest api이므로 csrf 보안이 필요없으므로 disable처리.
            .authorizeRequests()
            .antMatchers("/v1/api/**", "/h2-console/**").permitAll()
            .antMatchers("/user").hasAuthority("USER")
            .antMatchers("/admin").hasAuthority("ADMIN")
            .anyRequest().authenticated()
            .and()
    //               .formLogin()
    //                    .and()
            .logout()
        ;
        http.headers().frameOptions().disable();

    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/v2/api-docs", "/configuration/ui",
                "/swagger-resources", "/configuration/security",
                "/swagger-ui.html", "/webjars/**", "/swagger/**", "/websocket-client.html", "/websocket-client",
                "/websocket", "/sockjs", "/websockethandler/**", "/webjars/**", "/greetings/**");
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}
