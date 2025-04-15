package com.enus.newsletter.auth.oauth;

import java.util.Map;

public class NaverOauthUser extends AbsOauthUserInfo {

    public NaverOauthUser(Map<String, Object> attributes) {
        super(attributes);
    }

    @Override
    public String getId() {
        return attributes.get("id").toString();
    }

}
