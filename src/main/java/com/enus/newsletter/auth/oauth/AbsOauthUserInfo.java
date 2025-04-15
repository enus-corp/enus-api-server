package com.enus.newsletter.auth.oauth;

import java.util.Map;

import lombok.Getter;

@Getter
public abstract class AbsOauthUserInfo {
    protected Map<String, Object> attributes;

    public AbsOauthUserInfo(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    public abstract String getId();
}
