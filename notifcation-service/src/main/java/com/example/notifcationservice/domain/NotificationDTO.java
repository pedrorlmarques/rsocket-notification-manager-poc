package com.example.notifcationservice.domain;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NotificationDTO {

    private String userId;
    private String header;
    private String body;
    private NotificationType notificationType;
}
