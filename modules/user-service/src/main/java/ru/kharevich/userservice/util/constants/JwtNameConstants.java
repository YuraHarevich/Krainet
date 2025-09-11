package ru.kharevich.userservice.util.constants;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class JwtNameConstants {

    public static final String USERNAME_CLAIM_NAME = "preferred_username";

    public static final String REALM_ACCESS_CLAIM = "realm_access";

    public static final String ROLES_CLAIM = "roles";

    public static final List<String> ROLES_THAT_ARE_UNDER_SUPERVISION =  new ArrayList<>(Arrays.asList("USER"));

}
