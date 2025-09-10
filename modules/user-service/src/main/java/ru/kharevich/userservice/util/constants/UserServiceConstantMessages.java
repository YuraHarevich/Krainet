package ru.kharevich.userservice.util.constants;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class UserServiceConstantMessages {

    public static final String USER_NOT_FOUND_BY_ID = "User with id %s not found";

    public static final String USER_NOT_FOUND_BY_USERNAME = "User with username %s not found";

    public static final String USER_WITH_SUCH_USERNAME_ALREADY_EXISTS = "User with username %s already exists";

    public static final String USER_WITH_SUCH_EMAIL_ALREADY_EXISTS = "User with email %s already exists";

    public static final String USER_WITH_SUCH_EMAIL_OR_USERNAME_ALREADY_EXISTS = "User with email or username already exists";

    public static final String JWT_CONVERT_EXCEPTION_MESSAGE = "exception while trying to convert Jwt";

    public static final String USER_CREATION_EXCEPTION_MESSAGE = "unable to create or modify user";

    public static final String USER_DELETE_EXCEPTION_MESSAGE = "unable to delete user";

    public static final String USER_UPDATE_EXCEPTION_MESSAGE = "unable to update user";

    public static final String WRONG_CREDENTIALS_MESSAGE = "wrong credentials";

    public static final String JWT_CLAIM_EXTRACT_EXCEPTION_MESSAGE = "unable to extract jwt claim";

    public static final String JWT_CONVERSION_EXCEPTION_MESSAGE = "unable to convert jwt";

}
