package com.example.notifcationservice.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.retrosocket.EnableRSocketClients;

@Configuration
@EnableRSocketClients(basePackages = "com.example.notifcationservice.client")
public class ClientManagerNotificationRSocketConfiguration {

    @Bean
    RSocketRequester rSocketRequester(final RSocketRequester.Builder builder,
                                      final ClientManagerNotificationProperties clientManagerNotificationProperties) {
        return builder.tcp(clientManagerNotificationProperties.getHost(), clientManagerNotificationProperties.getPort());
    }
}
