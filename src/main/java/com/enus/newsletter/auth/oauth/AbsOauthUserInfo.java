package com.enus.newsletter.auth.oauth;

import java.util.Map;

import com.enus.newsletter.db.entity.UserEntity;

import lombok.Getter;

@Getter
public abstract class AbsOauthUserInfo {
    protected Map<String, Object> attributes;

    public AbsOauthUserInfo(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    public abstract String getId();
    public String getEmail() {
        return attributes.get("email") != null ? attributes.get("email").toString() : "";
    }
    public abstract UserEntity toEntity();
}
