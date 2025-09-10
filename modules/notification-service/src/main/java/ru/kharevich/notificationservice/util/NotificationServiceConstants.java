package ru.kharevich.notificationservice.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class NotificationServiceConstants {

    public static final String EMAIL_MESSAGE_TEMPLATE = "User with id %s, username %s, email %s have just made %s action with his profile";

}
