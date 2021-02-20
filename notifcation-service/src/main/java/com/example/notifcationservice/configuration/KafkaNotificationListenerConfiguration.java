package com.example.notifcationservice.configuration;

import com.example.notifcationservice.client.ClientManagerNotificationClient;
import com.example.notifcationservice.repository.NotificationRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.Disposable;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;
import reactor.kafka.receiver.KafkaReceiver;
import reactor.kafka.receiver.ReceiverOptions;

import java.time.Duration;
import java.util.Collections;

import static com.example.notifcationservice.utils.NotificationUtils.convertToNotification;

@Configuration
@Log4j2
public class KafkaNotificationListenerConfiguration {

    private final NotificationRepository notificationRepository;
    private final Scheduler notificationScheduler;
    private final ClientManagerNotificationClient clientManagerNotificationClient;

    public KafkaNotificationListenerConfiguration(NotificationRepository notificationRepository,
                                                  ClientManagerNotificationClient clientManagerNotificationClient) {
        this.notificationRepository = notificationRepository;
        this.clientManagerNotificationClient = clientManagerNotificationClient;
        this.notificationScheduler = Schedulers.newSingle("notification-scheduler", true);
    }

    @Bean
    Disposable notificationKafkaListener(final KafkaProperties kafkaProperties,
                                         @Value("${notification.topic}") final String topicName) {

        return KafkaReceiver.create(ReceiverOptions.
                <String, String>create(kafkaProperties.getConsumerProps())
                .subscription(Collections.singleton(topicName))
                .commitInterval(Duration.ZERO))
                .receive()
                .publishOn(this.notificationScheduler)
                .concatMap(record -> convertToNotification(record.value())
                        .doOnNext(notification -> log.info("Received {}", notification))
                        .flatMap(this.notificationRepository::save)
                        .flatMapMany(clientManagerNotificationClient::sendNotification)
                        .then(record.receiverOffset().commit())
                )
                .retry()
                .doOnCancel(this.notificationScheduler::dispose)
                .subscribe();
    }


}
