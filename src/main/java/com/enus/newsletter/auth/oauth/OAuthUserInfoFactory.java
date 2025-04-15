package com.enus.newsletter.auth.oauth;

import java.util.Map;

public class OAuthUserInfoFactory {
    public static AbsOauthUserInfo getOauthUserInfo(String registrationId, Map<String, Object> attributes) {
        switch (registrationId) {
            case "google":
                return new GoogleOauthUser(attributes);
            case "kakao":
                return new KakaoOauthUser(attributes);
            case "naver":
                return new NaverOauthUser(attributes);
            default:
                throw new IllegalArgumentException("Invalid OAuth2 provider: " + registrationId);
        }
    }
}
