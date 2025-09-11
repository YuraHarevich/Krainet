package ru.kharevich.notificationservice.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import ru.kharevich.notificationservice.model.Notification;
import ru.kharevich.notificationservice.service.NotificationService;

import static ru.kharevich.notificationservice.util.NotificationServiceConstants.EMAIL_MESSAGE_TEMPLATE;

@Service
@Slf4j
@ConditionalOnProperty(name = "notification.email.enabled", havingValue = "false")
public class NotificationStubService implements NotificationService {

    public void processNotification(Notification notification) {
        log.info("NotificationStubService.processNotification: Notification received : {}", notification);
    }

}
