package ru.kharevich.userservice.service;

import org.keycloak.representations.idm.UserRepresentation;
import ru.kharevich.userservice.dto.request.RegistrationRequest;
import ru.kharevich.userservice.dto.request.UserRequest;
import ru.kharevich.userservice.dto.response.AdminResponse;
import ru.kharevich.userservice.dto.response.UserResponse;
import ru.kharevich.userservice.model.User;

import java.util.List;
import java.util.UUID;

public interface UserAdminService {

    UserResponse updateUserById(UserRequest request, UUID id);

    UserResponse getUserById( UUID id);

    void deleteUserById(UUID id);

    List<AdminResponse>  getAllAdmins();
}
