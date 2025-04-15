package com.enus.newsletter.auth.oauth;

import java.util.Map;

import com.enus.newsletter.db.entity.UserEntity;
import com.enus.newsletter.enums.Gender;

public class NaverOauthUser extends AbsOauthUserInfo {

    public NaverOauthUser(Map<String, Object> attributes) {
        super(attributes);
    }

    @Override
    public String getId() {
        return attributes.get("id").toString();
    }

    @Override
    public UserEntity toEntity() {
        String email = attributes.get("email") != null ? attributes.get("email").toString() : "";
        String genderString = attributes.get("gender") != null ? attributes.get("gender").toString() : "";
        Gender gender = Gender.fromString(genderString);

        return UserEntity.builder()
            .provider("naver")
            .providerId(getId())
            .email(email)
            .gender(gender)
            .isOauthUser(true)
            .build();
    }

}
