package com.accionmfb.omnix.connector;

import com.accionmfb.omnix.connector.config.ApplicationConfig;
import com.accionmfb.omnix.connector.modules.http.HttpModule;
import com.accionmfb.omnix.connector.modules.kafka.KafkaModule;
import com.accionmfb.omnix.connector.modules.redis.RedisModule;
import com.accionmfb.omnix.connector.repository.RepositoryModule;
import com.accionmfb.omnix.connector.scheduler.SchedulerModule;
import org.springframework.context.annotation.Import;


@Import({
        HttpModule.class,
        KafkaModule.class,
        RedisModule.class,
        RepositoryModule.class,
        SchedulerModule.class,
        ApplicationConfig.class
})
public class OmnixConnectorBootstrap {
}
