package ru.kharevich.userservice.service;

import org.keycloak.representations.AccessTokenResponse;
import org.keycloak.representations.idm.UserRepresentation;
import ru.kharevich.userservice.dto.request.RegistrationRequest;
import ru.kharevich.userservice.dto.request.SignInRequest;
import ru.kharevich.userservice.dto.request.UserRequest;

import java.util.List;
import java.util.UUID;

public interface AuthenticationProviderService {

    public UUID createUser(RegistrationRequest userRequest);

    AccessTokenResponse sighIn(SignInRequest request);

    void updateUser(String userId, UserRequest request);

    void deleteUser(String userId);

    List<UserRepresentation>  getUsersWithAdminRole();

}
