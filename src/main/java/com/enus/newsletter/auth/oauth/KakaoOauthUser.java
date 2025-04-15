package com.enus.newsletter.auth.oauth;

import java.util.Map;

public class KakaoOauthUser extends AbsOauthUserInfo {

    public KakaoOauthUser(Map<String, Object> attributes) {
        super(attributes);
    }

    @Override
    public String getId() {
        return attributes.get("id").toString();
    }

}
