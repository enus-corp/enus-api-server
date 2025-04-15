package com.enus.newsletter.auth.oauth;

import java.util.Optional;

import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.enus.newsletter.db.entity.UserEntity;
import com.enus.newsletter.db.repository.imp.IUserRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j(topic = "OAUTH2_SERVICE")
@Service
public class CustomOauth2UserService extends DefaultOAuth2UserService{

    private final IUserRepository userRepository;

    public CustomOauth2UserService(IUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oauth2User = super.loadUser(userRequest);
        String email = oauth2User.getAttribute("email");
        String name = oauth2User.getAttribute("name");

        return oauth2User;
    }

    private OAuth2User process(OAuth2UserRequest userRequest, OAuth2User oauth2User) {
        // get client registration id (ex: google, kakao, naver)
        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        AbsOauthUserInfo oauthUserInfo = OAuthUserInfoFactory.getOauthUserInfo(registrationId, oauth2User.getAttributes());
        Optional<UserEntity> userInfo = userRepository.findByOauthId(oauthUserInfo.getId());
        UserEntity user;

        if (userInfo.isPresent()) {
            user = userInfo.get();
            // if (!registraionId.equals(user.getProvider())) {
            //     throw new OAuth2AuthenticationException("Oauth Provider does not match");
            // }
        } else {
            // create new user
        }

    }
    
}
