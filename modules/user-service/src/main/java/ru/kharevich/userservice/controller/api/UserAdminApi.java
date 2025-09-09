package ru.kharevich.userservice.controller.api;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.RequestBody;
import ru.kharevich.userservice.dto.request.UserRequest;
import ru.kharevich.userservice.dto.response.UserResponse;

import java.util.UUID;

public interface UserAdminApi {

    UserResponse updateUserById(UUID id,@Valid @RequestBody UserRequest request);

    void deleteUserById(UUID id);

    UserResponse getUserById(UUID id);

}
