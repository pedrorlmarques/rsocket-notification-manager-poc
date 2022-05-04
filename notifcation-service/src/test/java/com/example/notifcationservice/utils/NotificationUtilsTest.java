package com.example.notifcationservice.utils;

import com.example.notifcationservice.domain.NotificationDTO;
import com.example.notifcationservice.domain.NotificationType;
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import org.junit.jupiter.api.Test;
import reactor.test.StepVerifier;

class NotificationUtilsTest {

    @Test
    void testGivenNotificationStringExpectNotificationObject() {

        var notificationString = "{\n" +
                "  \"userId\" : \"8a4dade4-6660-11eb-ae93-0242ac130002\",\n" +
                "  \"header\" : \"WMA06KZZ9LM857901\",\n" +
                "  \"notificationType\" : \"SUCCESS\",\n" +
                "  \"body\" : \"asd13\"\n" +
                "}";

        var expectedNotification = new NotificationDTO("8a4dade4-6660-11eb-ae93-0242ac130002",
                "WMA06KZZ9LM857901",
                "asd13", NotificationType.SUCCESS);

        StepVerifier.create(NotificationUtils.convertToNotification(notificationString).log())
                .expectSubscription()
                .expectNext(expectedNotification)
                .verifyComplete();
    }

    @Test
    void testGivenIncorrectNotificationShouldExpectOnError() {

        var notificationString = "{\n" +
                "  \"userId\" : \"8a4dade4-6660-11eb-ae93-0242ac130002\",\n" +
                "  \"header\" : \"WMA06KZZ9LM857901\",\n" +
                "  \"body123\" : \"asd13\"\n" +
                "}";

        var expectedNotification = new NotificationDTO(
                "8a4dade4-6660-11eb-ae93-0242ac130002", "WMA06KZZ9LM857901", "asd13", null);

        StepVerifier.create(NotificationUtils.convertToNotification(notificationString).log())
                .expectSubscription()
                .expectError(UnrecognizedPropertyException.class)
                .verify();
    }
}
