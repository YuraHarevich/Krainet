package ru.kharevich.notificationservice.kafka.consumer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import ru.kharevich.notificationservice.model.Notification;
import ru.kharevich.notificationservice.service.NotificationService;

@Service
@Slf4j
@RequiredArgsConstructor
public class NotificationConsumer {

    private final NotificationService notificationService;

    @KafkaListener(topics = "${spring.kafka.topic.user}", groupId = "${spring.kafka.consumer.group-id}")
    public void consumeSupplyRequests(Notification notification) {
        log.info("NotificationConsumer.consumeNotification: Consuming notification about user with username: {}", notification.username());
        notificationService.processNotification(notification);
    }

}