package com.example.notifcationservice.mapper;

import com.example.notifcationservice.domain.Notification;
import com.example.notifcationservice.domain.NotificationDTO;

public final class NotificationMapper {

    private NotificationMapper() {
        //private method
    }

    public static Notification convertNotificationDTOtoNotification(NotificationDTO notificationDTO) {

        if (notificationDTO == null) {
            return null;
        }

        return new Notification(null, notificationDTO.userId(), notificationDTO.header(), notificationDTO.body(), notificationDTO.notificationType().name());
    }

}
