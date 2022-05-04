package com.example.notifcationservice.configuration;


import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@ConfigurationProperties(value = "client-manager-notification")
@ConstructorBinding
public record ClientManagerNotificationProperties(String host, Integer port) {

}
