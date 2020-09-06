package com.simple.portal.biz.v1.user.service;

import com.simple.portal.biz.v1.user.dto.OAuthAttributes;
import com.simple.portal.biz.v1.user.repository.UserRepository;
import com.simple.portal.common.Interceptor.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2UserAuthority;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

@Slf4j
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();;
        OAuth2User oAuth2User = delegate.loadUser(userRequest);

        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        String userNameAttributeName = userRequest.getClientRegistration()
                .getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();

        OAuthAttributes attributes = OAuthAttributes.
                of(registrationId, userNameAttributeName, oAuth2User.getAttributes());
        /*
        log.info("nickname : " + attributes.getName());
        log.info("email : " +  attributes.getEmail());
        log.info("profile : " + attributes.getPicture());
        log.info("platform : " + attributes.getPlatform());
        log.info("full data : " + attributes.getAttributes());
        */
        // OAuth2User에서 필요한 것들만 파싱
        Map<String, Object> userOauthData = new LinkedHashMap<>();
        if(registrationId.equals("google")) userOauthData.put("sub",  attributes.getNameAttributeKey()); // 구글일때는 키가 sub임
        else userOauthData.put("id",  attributes.getNameAttributeKey());
        userOauthData.put("platform", registrationId);
        userOauthData.put("nickname", attributes.getName());
        userOauthData.put("email", attributes.getEmail());
        userOauthData.put("profile", attributes.getPicture());

        Set<GrantedAuthority> authorities = new LinkedHashSet<>();
        authorities.add(new OAuth2UserAuthority(attributes.getAttributes()));
        OAuth2AccessToken token = userRequest.getAccessToken();
        for (String authority : token.getScopes()) {
            authorities.add(new SimpleGrantedAuthority("SCOPE_" + authority));
        }

        return new DefaultOAuth2User(
                authorities,
                userOauthData,
                attributes.getNameAttributeKey());
    }
}