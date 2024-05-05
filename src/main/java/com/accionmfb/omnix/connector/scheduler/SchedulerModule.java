package com.accionmfb.omnix.connector.scheduler;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration

@Import({
        BrokerMessageRetryScheduler.class
})
public class SchedulerModule {
}
