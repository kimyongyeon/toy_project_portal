package com.simple.portal.biz.v1.user.security;

public enum SocialType {
    FACEBOOK("facebook"),
    GOOGLE("google"),
    NAVER("naver"),
    GITHUB("github");

    private final String Role_Prefix = "ROLE_";
    private String name;

    SocialType(String name) { this.name = name;}

    public String getRoleType( ) {
        return Role_Prefix + name.toUpperCase();
    }

    public String getValue() {return name;}

    public boolean isEqual(String authority) {
        return this.getRoleType().equals(authority);
    }
}