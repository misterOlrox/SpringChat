package com.olrox.chat.config;

import com.olrox.chat.service.sending.MessageSenderFactory;
import org.springframework.beans.factory.config.ServiceLocatorFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MessageSenderConfig {

    @Bean("messageSenderFactory")
    public ServiceLocatorFactoryBean serviceLocatorFactoryBean() {
        ServiceLocatorFactoryBean factoryBean = new ServiceLocatorFactoryBean();
        factoryBean.setServiceLocatorInterface(MessageSenderFactory.class);
        return factoryBean;
    }
}
