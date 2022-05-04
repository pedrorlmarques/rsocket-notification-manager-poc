package com.example.notifcationservice.domain;


public record NotificationDTO(String userId,
                              String header,
                              String body,
                              NotificationType notificationType) {
}
