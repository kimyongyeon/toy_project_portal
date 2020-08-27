package com.simple.portal.config;

import com.simple.portal.biz.v1.user.service.CustomOAuth2UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;

import static com.simple.portal.biz.v1.user.security.SocialType.*;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Value("${spring.profile.value}")
    public String profiles;

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
                .authorizeRequests().antMatchers("/", "/oauth2/**", "/login/**", "/css/**", "/images/**", "/js/**", "/console/**", "/favicon.ico/**") // // url 별 권한 관리를 설정하는 옵션의 시작점. 이게 선언되어야 antMatchers  옵션을 사용할 수 있음.
                .permitAll()  // 위에 있는 경로는 누구나 접근 가능
                .antMatchers("/google").hasAuthority(GOOGLE.getRoleType()) // 관리 대상을 지정하는 옵션. google, facebook, naver는 role을 가지고 있어야만 접근가능. 즉, 인증된 사용자만 가능
                .antMatchers("/facebook").hasAuthority(FACEBOOK.getRoleType())
                .antMatchers("/naver").hasAuthority(NAVER.getRoleType())
                .antMatchers("/github").hasAnyAuthority(GITHUB.getRoleType())
                .antMatchers("/v1/api/**", "/h2-console/**").permitAll()
                .antMatchers("/user").hasAuthority("USER")
                .antMatchers("/admin").hasAuthority("ADMIN")
                .anyRequest().authenticated()// anyRequest( )는 위에서 설정한 것 외의 나머지 경로를 뜻함. authenticated() 메소드를 호출하여 인증된 사용자만 접근 하도록 함. ( 로그인된 사용자 )
                .and().oauth2Login()
                .userInfoEndpoint().userService(new CustomOAuth2UserService())
                .and()
                .defaultSuccessUrl("/loginSuccess")// 성공시 redirect 될 url
                .failureUrl("/loginFailure")// 실패시 redirect 될 url
                .and()
                .exceptionHandling()
                .authenticationEntryPoint(new LoginUrlAuthenticationEntryPoint("/login"));

        if (profiles.equals("local")) {
            http.headers().frameOptions().disable();
        }

    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/v2/api-docs", "/configuration/ui",
                "/swagger-resources", "/configuration/security",
                "/swagger-ui.html", "/webjars/**", "/swagger/**", "/websocket-client.html", "/websocket-client",
                "/websocket", "/sockjs","/stomp", "/websockethandler/**", "/webjars/**", "/greetings/**", "/ws-stomp/**");
    }
    /*
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
     */
}
