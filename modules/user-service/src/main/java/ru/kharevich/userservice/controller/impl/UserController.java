package ru.kharevich.userservice.controller.impl;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.keycloak.representations.AccessTokenResponse;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.kharevich.userservice.controller.api.UserAdminApi;
import ru.kharevich.userservice.controller.api.UserApi;
import ru.kharevich.userservice.dto.request.RegistrationRequest;
import ru.kharevich.userservice.dto.request.SignInRequest;
import ru.kharevich.userservice.dto.request.UserRequest;
import ru.kharevich.userservice.dto.response.AdminResponse;
import ru.kharevich.userservice.dto.response.UserResponse;
import ru.kharevich.userservice.model.User;
import ru.kharevich.userservice.service.UserAdminService;
import ru.kharevich.userservice.service.UserService;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/users")
public class UserController implements UserApi, UserAdminApi {

    private final UserService userService;

    private final UserAdminService userAdminService;

    @PostMapping("sign-up")
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponse createUser(@Valid @RequestBody RegistrationRequest request) {
        UserResponse userResponse = userService.createUser(request);
        return userResponse;
    }

    @PatchMapping("me")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public UserResponse updateUser(@Valid @RequestBody UserRequest request) {
        UserResponse userResponse = userService.updateUser(request);
        return userResponse;
    }

    @DeleteMapping("me")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void deleteUser() {
        userService.deleteUser();
    }

    @GetMapping("me")
    @ResponseStatus(HttpStatus.OK)
    public UserResponse getUser() {
        UserResponse userResponse = userService.getUser();
        return userResponse;
    }

    @PatchMapping("admin/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public UserResponse updateUserById(@PathVariable UUID id, @Valid @RequestBody UserRequest request) {
        UserResponse userResponse = userAdminService.updateUserById(request, id);
        return userResponse;
    }

    @DeleteMapping("admin/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void deleteUserById(@PathVariable UUID id) {
        userAdminService.deleteUserById(id);
    }

    @GetMapping("admin/{id}")
    @ResponseStatus(HttpStatus.OK)
    public UserResponse getUserById(@PathVariable UUID id) {
        UserResponse userResponse = userAdminService.getUserById(id);
        return userResponse;
    }

    @GetMapping("admin/all")
    @ResponseStatus(HttpStatus.OK)
    public List<AdminResponse> getAdmins() {
        return userAdminService.getAllAdmins();
    }

    @PostMapping("/sign-in")
    @ResponseStatus(HttpStatus.OK)
    public AccessTokenResponse sighIn(@RequestBody @Valid SignInRequest request) {
        AccessTokenResponse response = userService.sighIn(request);
        return response;
    }

}
