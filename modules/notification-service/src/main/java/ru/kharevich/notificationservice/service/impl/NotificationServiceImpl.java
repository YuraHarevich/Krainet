package ru.kharevich.notificationservice.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import ru.kharevich.notificationservice.client.UserClient;
import ru.kharevich.notificationservice.dto.response.AdminResponse;
import ru.kharevich.notificationservice.model.Notification;
import ru.kharevich.notificationservice.service.NotificationService;

import java.util.List;

import static ru.kharevich.notificationservice.util.NotificationServiceConstants.EMAIL_MESSAGE_TEMPLATE;

@Service
@RequiredArgsConstructor
@ConditionalOnProperty(name = "notification.email.enabled", havingValue = "true")
public class NotificationServiceImpl implements NotificationService {

    private final JavaMailSender emailSender;

    private final UserClient client;

    public void processNotification(Notification notification) {
        List<AdminResponse> adminResponseList = client.getAdminList();
        adminResponseList.forEach(response -> {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("noreply@baeldung.com");
            message.setTo(response.email());
            message.setSubject("subject");
            message.setText(EMAIL_MESSAGE_TEMPLATE.formatted(notification.id(), notification.username(), notification.email(), notification.action()));
            emailSender.send(message);
        });
    }

}
