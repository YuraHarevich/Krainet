package ru.kharevich.userservice.service;

import ru.kharevich.userservice.dto.request.UserRequest;
import ru.kharevich.userservice.dto.response.UserResponse;
import ru.kharevich.userservice.model.User;

public interface UserService {

    UserResponse createUser(UserRequest request);

    UserResponse updateUser(UserRequest request);

    UserResponse getUser();

    void deleteUser();

}
