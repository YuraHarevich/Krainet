package ru.kharevich.notificationservice.service;

import ru.kharevich.notificationservice.model.Notification;

public interface NotificationService {
    void processNotification(Notification notification);
}
