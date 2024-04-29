package com.accionmfb.omnix.connector.modules.nats;

import com.accionmfb.omnix.connector.commons.StringValues;
import com.accionmfb.omnix.connector.modules.nats.annotation.OmnixNatsHandler;
import com.accionmfb.omnix.connector.modules.nats.annotation.OmnixNatsListener;
import com.accionmfb.omnix.connector.modules.nats.annotation.PayloadJson;
import com.accionmfb.omnix.connector.modules.nats.annotation.Subject;
import io.nats.client.Connection;
import io.nats.client.Message;
import io.nats.client.impl.Headers;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class NatsConsumerAutoConfiguration {

    private final Connection connection;
    private final ApplicationContext applicationContext;

    @Bean
    public void initSubjectSubscriptionConnection() throws ClassNotFoundException {
        if(Objects.nonNull(connection) && connection.getStatus() == Connection.Status.CONNECTED) {
            log.info("Initializing Nats subject subscriptions...");
            Map<String, Object> beanDefinitions = applicationContext.getBeansWithAnnotation(OmnixNatsHandler.class);
            for (Map.Entry<String, Object> entry : beanDefinitions.entrySet()) {
                String beanName = entry.getKey();
                String springName = applicationContext.getBean(beanName).getClass().getName();
                String actualBeanName = springName.substring(0, springName.indexOf(StringValues.SPRING_CLASS_ENHANCE_SUFFIX));
                Class<?> beanClass = Class.forName(actualBeanName);
                Object bean = applicationContext.getBean(beanClass);
                Arrays.stream(beanClass.getDeclaredMethods())
                        .peek(method -> method.setAccessible(true))
                        .filter(method -> method.isAnnotationPresent(OmnixNatsListener.class))
                        .forEach(method -> {
                            OmnixNatsListener natsListener = method.getDeclaredAnnotation(OmnixNatsListener.class);
                            if (Objects.nonNull(natsListener)) {
                                String subject = natsListener.subject();
                                log.info("Subscribed to Nats subject: '{}'", subject);
                                connection.createDispatcher().subscribe(subject, msg -> subscribeToNatsMessage(bean, method, msg));
                            }
                        });
            }
        }
    }

    private void subscribeToNatsMessage(Object bean, Method method, Message message){
        logConsumerReceiverMessage(message);
        Object[] parameters = getMethodParameterValues(method, message);
        ReflectionUtils.invokeMethod(method, bean, parameters);
    }

    private Object[] getMethodParameterValues(Method method, Message message){
        Parameter[] parameters = method.getParameters();
        int parameterCount = method.getParameterCount();
        Object[] initialParameterValues = new Object[parameterCount];
        String payloadJson = new String(message.getData());
        for(int i = 0; i < parameterCount; i++){
            Parameter parameter = parameters[i];
            if(parameter.getType().isAssignableFrom(Message.class)){
                initialParameterValues[i] = message;
            }else if(parameter.isAnnotationPresent(Subject.class) && parameter.getType().isAssignableFrom(String.class)){
                initialParameterValues[i] = message.getSubject();
            }
            else if(parameter.isAnnotationPresent(PayloadJson.class) && parameter.getType().isAssignableFrom(String.class)){
                initialParameterValues[i] = payloadJson;
            }
            else{
                initialParameterValues[i] = null;
            }
        }
        return initialParameterValues;
    }

    private void logConsumerReceiverMessage(Message message){
        System.out.println();
        log.info("------------------------------- Received Nats message on subject: '{}' -------------------------------", message.getSubject());
        log.info("Connection status: {}", message.getConnection().getStatus().name());
        log.info("Connection URLS: {}", message.getConnection().getServerInfo().getConnectURLs());
        log.info("Subject: {}", message.getSubject());
        log.info("Payload: {}", new String(message.getData()));
        logMessageHeaders(message);
    }

    private void logMessageHeaders(Message message){
        try {
            Map<String, Object> headerMap = new LinkedHashMap<>();
            Headers headers = message.getHeaders();
            headers.forEach(headerMap::put);
            log.info("Headers: {}", headerMap);
        }catch (Exception ignored){}
    }
}
