package ru.kharevich.userservice.service;

import ru.kharevich.userservice.dto.request.UserRequest;
import ru.kharevich.userservice.dto.response.UserResponse;
import ru.kharevich.userservice.model.User;

import java.util.UUID;

public interface UserAdminService {

    UserResponse updateUserById(UserRequest request, UUID id);

    UserResponse getUserById( UUID id);

    void deleteUserById(UUID id);

}
