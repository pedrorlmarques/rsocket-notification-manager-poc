package com.example.notifcationservice.repository;

import com.example.notifcationservice.domain.Notification;
import com.example.notifcationservice.domain.NotificationType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.ActiveProfiles;
import reactor.test.StepVerifier;

@DataMongoTest
@ActiveProfiles("test")
class NotificationRepositoryTest {

    @Autowired
    private NotificationRepository notificationRepository;


    @Test
    void testSaveNotification() {

        StepVerifier.create(this.notificationRepository
                        .save(new Notification(null, "", "", "", NotificationType.SUCCESS.name())))
                .expectSubscription()
                .expectNextCount(1)
                .verifyComplete();

    }
}