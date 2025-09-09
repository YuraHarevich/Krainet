package ru.kharevich.userservice.util.constants;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class UserServiceConstantMessages {

    public static final String USER_NOT_FOUND = "User with id %s not found";

    public static final String USER_WITH_SUCH_USERNAME_ALREADY_EXISTS = "User with username %s already exists";

    public static final String USER_WITH_SUCH_EMAIL_ALREADY_EXISTS = "User with email %s already exists";

    public static final String USER_WITH_SUCH_EMAIL_OR_USERNAME_ALREADY_EXISTS = "User with email or username already exists";


}
