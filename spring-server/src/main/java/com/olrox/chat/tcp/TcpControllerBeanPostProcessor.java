package com.olrox.chat.tcp;

import com.olrox.chat.tcp.annotation.*;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

@Component
public class TcpControllerBeanPostProcessor implements BeanPostProcessor {
    private Map<String, Class> map = new HashMap<>();

    @Autowired
    private Server server;

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        Class<?> beanClass = bean.getClass();
        if (beanClass.isAnnotationPresent(TcpController.class)) {
            map.put(beanName, beanClass);
        }
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (map.containsKey(beanName)) {
            List<Method> messageMethods = new ArrayList<>();
            List<Method> connectMethods = new ArrayList<>();
            List<Method> disconnectMethods = new ArrayList<>();
            Method[] methods = bean.getClass().getMethods();
            for (Method method : methods) {
                if (method.getAnnotation(OnTcpMessage.class) != null && method.getParameterCount() == 2
                        && method.getParameterTypes()[0] == Connection.class) {
                    messageMethods.add(method);
                } else if (method.getAnnotation(OnTcpConnect.class) != null && method.getParameterCount() == 1
                        && method.getParameterTypes()[0] == Connection.class) {
                    connectMethods.add(method);
                } else if (method.getAnnotation(OnTcpDisconnect.class) != null && method.getParameterCount() == 1
                        && method.getParameterTypes()[0] == Connection.class) {
                    disconnectMethods.add(method);
                }
            }

            server.addListener(new Connection.Listener() {
                @Override
                public void messageReceived(Connection connection, Object message) {
                    String text = new String((byte[]) message).trim();

                    for (Method messageMethod : messageMethods) {
                        try {
                            messageMethod.invoke(bean, connection, text);
                        } catch (IllegalAccessException | InvocationTargetException e) {
                            e.printStackTrace();
                        }
                        return;
                    }
                }

                @Override
                public void connected(Connection connection) {
                    for (Method connectMethod : connectMethods) {
                        try {
                            connectMethod.invoke(bean, connection);
                        } catch (IllegalAccessException | InvocationTargetException e) {
                            e.printStackTrace();
                        }
                        return;
                    }
                }

                @Override
                public void disconnected(Connection connection) {
                    for (Method disconnectMethod : disconnectMethods) {
                        try {
                            disconnectMethod.invoke(bean, connection);
                        } catch (IllegalAccessException | InvocationTargetException e) {
                            e.printStackTrace();
                        }
                        return;
                    }
                }
            });
        }
        return bean;
    }
}
