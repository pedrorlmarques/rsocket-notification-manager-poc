package com.example.notifcationservice.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.retrosocket.EnableRSocketClients;

@Configuration
@EnableRSocketClients(basePackages = "com.example.notifcationservice.client")
public class RSocketConfiguration {

    @Bean
    RSocketRequester rSocketRequester(final RSocketRequester.Builder builder,
                                      @Value("${notification.host}") String host,
                                      @Value("${notification.port}") int port) {
        return builder.tcp(host, port);
    }
}
