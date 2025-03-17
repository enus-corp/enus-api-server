package com.enus.newsletter.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebsocketConfig implements WebSocketMessageBrokerConfigurer {

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    @Value("${spring.activemq.user}")
    private String activeMqUser;

    @Value("${spring.activemq.password}")
    private String activeMqPassword;

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        // client will subscribe to this endpoint
        registry.enableStompBrokerRelay("/topic", "/queue")
                .setRelayHost("localhost")
                .setRelayPort(61613) // default stop port
                .setClientLogin(activeMqUser)
                .setClientPasscode(activeMqPassword)
                .setSystemLogin(activeMqUser)
                .setSystemPasscode(activeMqPassword)
                .setSystemHeartbeatReceiveInterval(10000)
                .setSystemHeartbeatSendInterval(10000);

        // client will send messages to this endpoint
        registry.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // client will connect to this endpoint
        // TODO. Should not use setAllowedOriginPatterns("*") in production
        registry.addEndpoint("/ws")
                .setAllowedOriginPatterns("*")
                .withSockJS();
    }

}