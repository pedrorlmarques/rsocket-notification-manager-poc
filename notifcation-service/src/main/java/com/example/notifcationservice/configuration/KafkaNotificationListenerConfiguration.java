package com.example.notifcationservice.configuration;

import com.example.notifcationservice.client.ClientManagerNotificationClient;
import com.example.notifcationservice.repository.NotificationRepository;
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

import static com.example.notifcationservice.utils.NotificationUtils.handleNotification;

@Configuration
public class KafkaNotificationListenerConfiguration {

    private final NotificationRepository notificationRepository;
    private final Scheduler notificationScheduler;
    private final ClientManagerNotificationClient clientManagerNotificationClient;
    private final KafkaProperties kafkaProperties;

    public KafkaNotificationListenerConfiguration(NotificationRepository notificationRepository,
                                                  ClientManagerNotificationClient clientManagerNotificationClient,
                                                  KafkaProperties kafkaProperties) {
        this.notificationRepository = notificationRepository;
        this.clientManagerNotificationClient = clientManagerNotificationClient;
        this.kafkaProperties = kafkaProperties;
        this.notificationScheduler = Schedulers.newSingle("notification-scheduler", true);
    }

    @Bean
    Disposable notificationKafkaListener(@Value("${notification.topic}") final String topicName) {
        return KafkaReceiver.create(ReceiverOptions.
                <String, String>create(this.kafkaProperties.getConsumerProps())
                .subscription(Collections.singleton(topicName))
                .commitInterval(Duration.ZERO))
                .receive()
                .publishOn(this.notificationScheduler)
                .concatMap(handleNotification(clientManagerNotificationClient, notificationRepository))
                .retry()
                .doOnCancel(this.notificationScheduler::dispose)
                .subscribe();
    }
}
