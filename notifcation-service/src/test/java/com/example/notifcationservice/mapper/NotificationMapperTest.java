package com.example.notifcationservice.mapper;

import com.example.notifcationservice.domain.NotificationDTO;
import com.example.notifcationservice.domain.NotificationType;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class NotificationMapperTest {

    @Test
    void testGivenNotificationDTOItShouldConvertToNotification() {

        var notificationDTO = new NotificationDTO();
        notificationDTO.setUserId("8a4dade4-6660-11eb-ae93-0242ac130002");
        notificationDTO.setHeader("WMA06KZZ9LM857901");
        notificationDTO.setBody("asd13");
        notificationDTO.setNotificationType(NotificationType.SUCCESS);

        var notification = NotificationMapper.convertNotificationDTOtoNotification(notificationDTO);

        assertThat(notification).isNotNull();
        assertThat(notification.getNotificationType()).isNotNull().isEqualTo(notificationDTO.getNotificationType().name());
        assertThat(notification.getUserId()).isNotNull().isEqualTo(notificationDTO.getUserId());
        assertThat(notification.getHeader()).isNotNull().isEqualTo(notificationDTO.getHeader());
        assertThat(notification.getBody()).isNotNull().isEqualTo(notificationDTO.getBody());

    }

    @Test
    void testGivenNullNotificationDTOItShouldConvertToNull() {
        assertThat(NotificationMapper.convertNotificationDTOtoNotification(null)).isNull();
    }

}
