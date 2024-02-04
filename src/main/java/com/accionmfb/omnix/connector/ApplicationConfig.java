package com.accionmfb.omnix.connector;

import com.accionmfb.omnix.connector.modules.t24.processor.AbstractOfsGenerator;
import io.nats.client.Connection;
import io.nats.client.Message;
import io.nats.client.Nats;
import io.nats.client.impl.NatsMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.kafka.core.KafkaTemplate;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class ApplicationConfig {

    private final List<AbstractOfsGenerator> ofsGenerators;
    private final KafkaTemplate<String, String> kafkaTemplate;

    @EventListener(value = ApplicationStartedEvent.class)
    public void testNatsConnection() throws IOException, InterruptedException {
        Connection natsConnection = Nats.connect("nats://10.10.0.81:4222");
        System.out.println(natsConnection.getStatus());

        natsConnection.publish("subject-1", "This is a message".getBytes(StandardCharsets.UTF_8));
        kafkaTemplate.send("topic-1", "message");
    }
}
