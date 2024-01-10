package com.chatApp.chatApp.config;

import io.sentry.spring.jakarta.EnableSentry;
import org.springframework.context.annotation.Configuration;

@EnableSentry(dsn = "https://81ad7a9ba361a1561aa451c59f32f5d9@o4506360051400704.ingest.sentry.io/4506360068702208")
@Configuration
class SentryConfiguration {

}
