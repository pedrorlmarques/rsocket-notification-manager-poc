package com.example.notifcationservice.utils;

import com.example.notifcationservice.domain.Notification;
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import org.junit.jupiter.api.Test;
import reactor.test.StepVerifier;

class NotificationUtilsTest {

    @Test
    void testGivenNotificationStringExpectNotificationObject() {

        var notificationString = "{\n" +
                "  \"userId\" : \"8a4dade4-6660-11eb-ae93-0242ac130002\",\n" +
                "  \"header\" : \"WMA06KZZ9LM857901\",\n" +
                "  \"body\" : \"asd13\"\n" +
                "}";

        var expectedNotification = new Notification();
        expectedNotification.setUserId("8a4dade4-6660-11eb-ae93-0242ac130002");
        expectedNotification.setHeader("WMA06KZZ9LM857901");
        expectedNotification.setBody("asd13");

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

        var expectedNotification = new Notification();
        expectedNotification.setUserId("8a4dade4-6660-11eb-ae93-0242ac130002");
        expectedNotification.setHeader("WMA06KZZ9LM857901");
        expectedNotification.setBody("asd13");

        StepVerifier.create(NotificationUtils.convertToNotification(notificationString).log())
                .expectSubscription()
                .expectError(UnrecognizedPropertyException.class)
                .verify();
    }
}
