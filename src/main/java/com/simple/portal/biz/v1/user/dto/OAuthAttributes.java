package com.simple.portal.biz.v1.user.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.LinkedHashMap;
import java.util.Map;

@Getter
public class OAuthAttributes {
    private Map<String, Object> attributes;
    private String nameAttributeKey;
    private String name;
    private String email;
    private String picture;

    @Builder
    public OAuthAttributes(Map<String, Object> attributes,
                           String nameAttributeKey, String name,
                           String email, String picture) {
        this.attributes = attributes;
        this.nameAttributeKey= nameAttributeKey;
        this.name = name;
        this.email = email;
        this.picture = picture;
    }

    public static OAuthAttributes of(String registrationId,
                                     String userNameAttributeName,
                                     Map<String, Object> attributes) {

        if("naver".equals(registrationId)) return ofNaver(attributes);
        else if("github".equals(registrationId)) return ofGithub(userNameAttributeName, attributes);
        else if ("facebook".equals(registrationId))  return ofFaceBook(userNameAttributeName, attributes);
        return ofGoogle(userNameAttributeName, attributes); // 구글
    }

    // 구글
    private static OAuthAttributes ofGoogle(String userNameAttributeName,
                                            Map<String, Object> attributes) {

        return OAuthAttributes.builder()
                .name((String) attributes.get("name"))
                .email((String) attributes.get("email"))
                .picture((String) attributes.get("picture"))
                .attributes(attributes)
                .nameAttributeKey(userNameAttributeName)
                .build();
    }

    // 페이스북  -> 이미지 파싱 필요
    private static OAuthAttributes ofFaceBook(String userNameAttributeName,
                                              Map<String, Object> attributes) {

        //  picture={data={height=50, is_silhouette=false, url=https://platform-lookaside.fbsbx.com/platform/profilepic/?asid=3051485381636996&height=50&width=50&ext=1600003018&hash=AeRTziYd2gCIfv4X, width=50}}}
        //  picture, data가 linkedHashMap으로 구현되어있음....

        LinkedHashMap<String, Object> facebookPicture = (LinkedHashMap<String, Object>) attributes.get("picture");
        LinkedHashMap<String, Object> facebookPictureData = (LinkedHashMap<String, Object>) facebookPicture.get("data");

        return OAuthAttributes.builder()
                .name((String) attributes.get("name"))
                .email((String) attributes.get("email"))
                .picture(facebookPictureData.get("url").toString())
                .attributes(attributes)
                .nameAttributeKey(userNameAttributeName)
                .build();
    }

    // 깃허브 -> login : 닉네임, avatar_url : 프로필 이미지, email: 이메일 정보
    private static OAuthAttributes ofGithub(String userNameAttributeName,
                                            Map<String, Object> attributes) {
        return OAuthAttributes.builder()
                .name((String) attributes.get("bio"))
                .email((String) attributes.get("email"))
                .picture((String) attributes.get("avatar_url"))
                .attributes(attributes)
                .nameAttributeKey(userNameAttributeName)
                .build();
    }

    //네이버
    private static OAuthAttributes ofNaver(Map<String, Object> attributes) {
        Map<String, Object> response = (Map<String, Object>) attributes.get("response");

        return OAuthAttributes.builder()
                .name((String) response.get("name"))
                .email((String) response.get("email"))
                .picture((String) response.get("profile_image"))
                .attributes(response)
                .nameAttributeKey("id")
                .build();
    }
}