package com.accionmfb.omnix.connector.repository;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration

@Import({
        BrokerOutboxRepository.class
})
public class RepositoryModule {
}
