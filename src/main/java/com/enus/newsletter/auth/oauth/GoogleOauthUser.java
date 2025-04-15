package com.enus.newsletter.auth.oauth;

import java.util.Map;

import com.enus.newsletter.db.entity.UserEntity;

public class GoogleOauthUser extends AbsOauthUserInfo {

    public GoogleOauthUser(Map<String, Object> attributes) {
        super(attributes);
    }

    @Override
    public String getId() {
        return attributes.get("sub").toString();
    }

    @Override
    public UserEntity toEntity() {
        String email = attributes.get("email") != null ? attributes.get("email").toString() : "";
        String firstName = attributes.get("given_name") != null ? attributes.get("given_name").toString() : "";
        String lastName = attributes.get("family_name") != null ? attributes.get("family_name").toString() : "";

        return UserEntity.builder()
            .provider("google")
            .providerId(getId())
            .email(email)
            .firstName(firstName)
            .lastName(lastName)
            .isOauthUser(true)
            .build();
    }

}
