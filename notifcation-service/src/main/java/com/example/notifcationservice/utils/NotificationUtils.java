package com.example.notifcationservice.utils;

import com.example.notifcationservice.domain.Notification;
import com.fasterxml.jackson.databind.ObjectMapper;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

public final class NotificationUtils {

    private NotificationUtils() {
        throw new IllegalCallerException("Utility class");
    }

    // Since Read Value from Jackson is a Blocking call we should wrap the operation on a different thread
    // https://projectreactor.io/docs/core/release/reference/#faq.wrap-blocking
    public static Mono<Notification> convertToNotification(final String notification) {
        return Mono.fromCallable(() -> new ObjectMapper()
                .readValue(notification, Notification.class))
                .subscribeOn(Schedulers.boundedElastic());
    }
}
