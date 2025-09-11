package ru.kharevich.userservice.service;

import org.keycloak.representations.AccessTokenResponse;
import ru.kharevich.userservice.dto.request.RegistrationRequest;
import ru.kharevich.userservice.dto.request.SignInRequest;
import ru.kharevich.userservice.dto.request.UserRequest;
import ru.kharevich.userservice.dto.response.UserResponse;

public interface UserService {

    UserResponse createUser(RegistrationRequest request);

    UserResponse updateUser(UserRequest request);

    UserResponse getUser();

    void deleteUser();

    AccessTokenResponse sighIn(SignInRequest request);

}
