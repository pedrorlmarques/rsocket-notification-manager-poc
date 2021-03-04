package com.example.notifcationservice.utils;

import com.example.notifcationservice.client.ClientManagerNotificationClient;
import com.example.notifcationservice.domain.NotificationDTO;
import com.example.notifcationservice.mapper.NotificationMapper;
import com.example.notifcationservice.repository.NotificationRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j2;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import reactor.kafka.receiver.ReceiverRecord;

import java.util.function.Function;

@Log4j2
public final class NotificationUtils {

    private NotificationUtils() {
        //private constructor
    }

    /*
        Since Read Value from Jackson is a Blocking call we should wrap the operation on a different thread
        https://projectreactor.io/docs/core/release/reference/#faq.wrap-blocking
     */
    public static Mono<NotificationDTO> convertToNotification(final String notification) {
        return Mono.fromCallable(() -> new ObjectMapper().readValue(notification, NotificationDTO.class))
                .subscribeOn(Schedulers.boundedElastic());
    }

    /*
        Function to persist and redirect the notification to the client manager service and then commits the offset
     */
    public static Function<ReceiverRecord<String, String>, Mono<Void>> handleNotification(
            final ClientManagerNotificationClient clientManagerNotificationClient,
            final NotificationRepository notificationRepository) {

        return record -> convertToNotification(record.value())
                .doOnNext(notification -> log.info("Received {}", notification))
                .map(NotificationMapper::convertNotificationDTOtoNotification)
                .flatMap(notificationRepository::save)
                .flatMap(clientManagerNotificationClient::sendNotification)
                .then(record.receiverOffset().commit());
    }
}
