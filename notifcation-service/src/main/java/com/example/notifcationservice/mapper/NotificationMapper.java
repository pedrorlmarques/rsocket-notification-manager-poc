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

        var notification = new Notification();
        notification.setUserId(notificationDTO.getUserId());
        notification.setBody(notificationDTO.getBody());
        notification.setHeader(notificationDTO.getHeader());
        notification.setNotificationType(notificationDTO.getNotificationType().name());

        return notification;
    }

}
