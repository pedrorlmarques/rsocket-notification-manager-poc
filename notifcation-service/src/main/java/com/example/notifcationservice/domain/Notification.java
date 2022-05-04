package com.example.notifcationservice.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public record Notification(@Id String id,
                           String userId,
                           String header,
                           String body,
                           String notificationType) {
}
