package ru.kharevich.userservice.controller.api;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.RequestBody;
import ru.kharevich.userservice.dto.request.UserRequest;
import ru.kharevich.userservice.dto.response.UserResponse;

import java.util.UUID;

public interface UserApi {

    UserResponse createUser(@Valid @RequestBody UserRequest request);

    UserResponse updateUser(@Valid @RequestBody UserRequest request);

    void deleteUser();

    UserResponse getUser();

}
