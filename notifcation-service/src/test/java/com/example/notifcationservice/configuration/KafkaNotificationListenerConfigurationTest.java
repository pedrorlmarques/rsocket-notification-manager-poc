package com.example.notifcationservice.configuration;

import com.example.notifcationservice.KafkaContainerTestingSupport;
import com.example.notifcationservice.NotifcationServiceApplication;
import com.example.notifcationservice.domain.Notification;
import com.example.notifcationservice.domain.NotificationDTO;
import com.example.notifcationservice.domain.NotificationType;
import com.example.notifcationservice.repository.NotificationRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.rsocket.SocketAcceptor;
import io.rsocket.core.RSocketServer;
import io.rsocket.transport.netty.server.CloseableChannel;
import io.rsocket.transport.netty.server.TcpServerTransport;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import reactor.core.publisher.Mono;
import reactor.core.publisher.MonoProcessor;
import reactor.test.StepVerifier;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

@SpringBootTest(classes = NotifcationServiceApplication.class)
@ActiveProfiles("test")
class KafkaNotificationListenerConfigurationTest implements KafkaContainerTestingSupport {

    @Value("${notification.topic}")
    private String topicName;

    // volatile guarantees visibility across threads.
    // MonoProcessor implements stateful semantics for a mono
    private volatile MonoProcessor<Object> result;
    private CloseableChannel closeableChannel;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private NotificationRepository notificationRepository;
    @Autowired
    private ClientManagerNotificationProperties clientManagerNotificationProperties;

    @BeforeEach
    public void setUp() {

        this.result = MonoProcessor.create();

        closeableChannel = RSocketServer.create(SocketAcceptor
                .forFireAndForget(p -> {
                    System.out.println("Received Processed Task " + p.getDataUtf8());
                    p.release();
                    result.onNext("Just for Testing Purpose");
                    result.onComplete();
                    return Mono.empty();
                }))
                .bind(TcpServerTransport.create(clientManagerNotificationProperties.getPort()))
                .block();
    }

    @AfterEach
    public void tearDown() {
        this.notificationRepository.deleteAll().block();
        closeableChannel.dispose();
    }

    @Test
    void testGivenNotificationShouldPersistOnDatabaseAndSentNotificationToClientManagerService() throws Exception {

        var notificationDTO = new NotificationDTO();
        notificationDTO.setUserId("8a4dade4-6660-11eb-ae93-0242ac130002");
        notificationDTO.setHeader("WMA06KZZ9LM857901");
        notificationDTO.setBody("asd13");
        notificationDTO.setNotificationType(NotificationType.SUCCESS);

        try (var producer = createProducer()) {
            final var message = this.objectMapper.writeValueAsString(notificationDTO);
            final var producerRecord = new ProducerRecord<>(this.topicName, notificationDTO.getUserId(), message);
            producer.send(producerRecord).get();
        }

        var notification = new Notification();
        notification.setUserId(notificationDTO.getUserId());
        notification.setHeader(notificationDTO.getHeader());
        notification.setBody(notificationDTO.getBody());
        notification.setNotificationType(notificationDTO.getNotificationType().name());

        await().untilAsserted(() -> {
            var notificationPersisted = this.notificationRepository.findAll().blockFirst();
            assertThat(notificationPersisted).isEqualToIgnoringGivenFields(notification, "id");
        });

        StepVerifier.create(result)
                .expectSubscription()
                .expectNext("Just for Testing Purpose")
                .verifyComplete();
    }
}
