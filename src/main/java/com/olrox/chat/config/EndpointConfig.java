package com.olrox.chat.config;

import com.olrox.chat.controller.ChatWebSocketEndpoint;
import com.olrox.chat.service.sending.MessageSenderFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

@Configuration
public class EndpointConfig {

    @Autowired
    private ApplicationContext context;

    @Bean
    public ChatWebSocketEndpoint chatWebSocketEndpoint() {
        return new ChatWebSocketEndpoint(context.getBean(MessageSenderFactory.class));
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
