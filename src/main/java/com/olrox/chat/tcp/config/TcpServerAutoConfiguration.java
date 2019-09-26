package com.olrox.chat.tcp.config;

import com.olrox.chat.tcp.Server;
import com.olrox.chat.tcp.TcpControllerBeanPostProcessor;
import com.olrox.chat.tcp.TcpServer;
import com.olrox.chat.tcp.TcpServerAutoStarterApplicationListener;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(TcpServerProperties.class)
@ConditionalOnProperty(prefix = "com.olrox.tcp-server", name = {"port", "auto-start"})
public class TcpServerAutoConfiguration {

    @Bean
    TcpServerAutoStarterApplicationListener tcpServerAutoStarterApplicationListener() {
        return new TcpServerAutoStarterApplicationListener();
    }

    @Bean
    TcpControllerBeanPostProcessor tcpControllerBeanPostProcessor() {
        return new TcpControllerBeanPostProcessor();
    }

    @Bean
    Server server(){
        return new TcpServer();
    }
}
