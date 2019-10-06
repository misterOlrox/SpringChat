package com.olrox.chat.config;

import com.olrox.chat.chatcontroller.ChatWebSocketEndpoint;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

@Configuration
public class EndpointConfig {

    @Bean
    public ChatWebSocketEndpoint chatWebSocketEndpoint() {
        return new ChatWebSocketEndpoint();
    }

    @Bean
    public ServerEndpointExporter endpointExporter() {
        return new ServerEndpointExporter();
    }

    @Bean
    public CustomSpringConfigurator customSpringConfigurator() {
        return new CustomSpringConfigurator();
    }
}
