package ru.kharevich.notificationservice.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class NotificationServiceConstants {

    public static final String EMAIL_MESSAGE_TEMPLATE = "User with id %s, username %s, email %s have just made %s action with his profile";

    public static final String USER_NOT_FOUND = "user not found";

    public static final String USER_SERVICE_UNAVAILABLE = "Internal service error";


}
