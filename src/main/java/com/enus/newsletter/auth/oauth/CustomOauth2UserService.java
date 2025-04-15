package com.enus.newsletter.auth.oauth;

import java.util.Map;
import java.util.Optional;

import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.enus.newsletter.db.entity.UserEntity;
import com.enus.newsletter.db.repository.imp.IUserRepository;
import com.enus.newsletter.interfaces.CustomUserDetailsImpl;

import lombok.extern.slf4j.Slf4j;

@Slf4j(topic = "CUSTOM_OAUTH2_USER_SERVICE")
@Service
public class CustomOauth2UserService extends DefaultOAuth2UserService{

    private final IUserRepository userRepository;

    public CustomOauth2UserService(IUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        log.info("[loadUser()] called");
        OAuth2User oauth2User = super.loadUser(userRequest);

        return this.process(userRequest, oauth2User);
    }

    private OAuth2User process(OAuth2UserRequest userRequest, OAuth2User oauth2User) {
        // get client registration id (ex: google, kakao, naver)
        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        // oauth2 user name attribute name (ex: sub for google, id for kakao and naver)
        String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();
        Map<String, Object> attributes = oauth2User.getAttributes();

        log.info("[process()] OAuth2 Provider Registraion Id: {}", registrationId);
        log.info("[process()] OAuth2 User Name Attribute Name: {}", userNameAttributeName);
        log.info("[process()] OAuth2 User Attributes: {}", attributes);

        AbsOauthUserInfo oauthUserInfo = OAuthUserInfoFactory.getOauthUserInfo(registrationId, attributes);
        UserEntity user = saveOrUpdate(oauthUserInfo, registrationId);
        CustomUserDetailsImpl userDetails = new CustomUserDetailsImpl(user);
        
        log.info("[process()] Successfully processed OAuth2 User: {}", user.getEmail());

        return new DefaultOAuth2User(userDetails.getAuthorities(), attributes, userNameAttributeName);
    }

    protected UserEntity saveOrUpdate(AbsOauthUserInfo oauthUserInfo, String registrationId) {
        Optional<UserEntity> userOptional = userRepository.findByProviderId(oauthUserInfo.getId());
        UserEntity user;

        if (userOptional.isPresent()) {
            user = userOptional.get();
            if (!registrationId.equals(user.getProvider())) {
                throw new OAuth2AuthenticationException("Oauth Provider does not match");
            }
            log.info("[saveOrUpdate()] User already exists: {}", user.getUsername());
            // check if email has changed
            if (!user.getEmail().equals(oauthUserInfo.getEmail())) {
                // mark the updated email as updated
                user.setEmail(oauthUserInfo.getEmail());
                user.setEmailUpdated(true);
            }
        } else {
            user = oauthUserInfo.toEntity();
        }
        return userRepository.save(user);
    }
    
}
