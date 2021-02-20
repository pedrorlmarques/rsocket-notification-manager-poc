package com.example.notifcationservice.configuration;


import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@ConfigurationProperties(value = "client-manager-notification")
@ConstructorBinding
public class ClientManagerNotificationProperties {

    private final String host;
    private final Integer port;

    public ClientManagerNotificationProperties(String host, Integer port) {
        this.host = host;
        this.port = port;
    }

    public String getHost() {
        return host;
    }

    public Integer getPort() {
        return port;
    }

}
