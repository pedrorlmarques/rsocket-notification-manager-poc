package com.example.notifcationservice.client;


import com.example.notifcationservice.domain.Notification;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.retrosocket.RSocketClient;
import reactor.core.publisher.Mono;

@RSocketClient
public interface ClientManagerNotificationClient {

    @MessageMapping("notification-sender")
    Mono<Void> sendNotification(Notification notification);
}
