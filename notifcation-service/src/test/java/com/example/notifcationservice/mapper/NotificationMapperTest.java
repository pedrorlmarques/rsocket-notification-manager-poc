package com.example.notifcationservice.mapper;

import com.example.notifcationservice.domain.NotificationDTO;
import com.example.notifcationservice.domain.NotificationType;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class NotificationMapperTest {

    @Test
    void testGivenNotificationDTOItShouldConvertToNotification() {

        var notificationDTO = new NotificationDTO("8a4dade4-6660-11eb-ae93-0242ac130002",
                "WMA06KZZ9LM857901", "asd13", NotificationType.SUCCESS);

        var notification = NotificationMapper.convertNotificationDTOtoNotification(notificationDTO);

        assertThat(notification).isNotNull();
        assertThat(notification.notificationType()).isNotNull().isEqualTo(notificationDTO.notificationType().name());
        assertThat(notification.userId()).isNotNull().isEqualTo(notificationDTO.userId());
        assertThat(notification.header()).isNotNull().isEqualTo(notificationDTO.header());
        assertThat(notification.body()).isNotNull().isEqualTo(notificationDTO.body());

    }

    @Test
    void testGivenNullNotificationDTOItShouldConvertToNull() {
        assertThat(NotificationMapper.convertNotificationDTOtoNotification(null)).isNull();
    }

}
