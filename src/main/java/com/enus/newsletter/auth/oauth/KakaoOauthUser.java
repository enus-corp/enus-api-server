package com.enus.newsletter.auth.oauth;

import java.util.Map;

import com.enus.newsletter.db.entity.UserEntity;

public class KakaoOauthUser extends AbsOauthUserInfo {

    public KakaoOauthUser(Map<String, Object> attributes) {
        super(attributes);
    }

    @Override
    public String getId() {
        return attributes.get("id").toString();
    }

    @Override
    public UserEntity toEntity() {
        String email = attributes.get("email") != null ? attributes.get("email").toString() : "";
        
        return UserEntity.builder()
            .provider("kakao")
            .providerId(getId())
            .email(email)
            .isOauthUser(true)
            .build();
    }

}
