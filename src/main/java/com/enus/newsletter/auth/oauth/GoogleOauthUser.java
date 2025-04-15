package com.enus.newsletter.auth.oauth;

import java.util.Map;

public class GoogleOauthUser extends AbsOauthUserInfo {

    public GoogleOauthUser(Map<String, Object> attributes) {
        super(attributes);
    }

    @Override
    public String getId() {
        return attributes.get("sub").toString();
    }

}
