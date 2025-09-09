package ru.kharevich.userservice.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.kharevich.userservice.dto.request.UserRequest;
import ru.kharevich.userservice.dto.response.UserResponse;
import ru.kharevich.userservice.model.User;
import ru.kharevich.userservice.repository.UserRepository;
import ru.kharevich.userservice.service.UserAdminService;
import ru.kharevich.userservice.service.UserService;
import ru.kharevich.userservice.util.mappers.UserMapper;
import ru.kharevich.userservice.util.validation.UserValidationService;

import java.util.Optional;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService, UserAdminService {

    private final UserValidationService userValidationService;

    private final UserRepository userRepository;

    private final UserMapper userMapper;

    @Transactional
    public UserResponse updateUserById(UserRequest request, UUID id) {
        User user = userValidationService.alreadyExistsByUsernameOrEmailExceptPresentUsernameOrEmail(request.username(), request.email(), id);
        userMapper.updateUserByRequest(request, user);
        return userMapper.toUserResponse(user);
    }

    public UserResponse getUserById(UUID id) {
        User user = userValidationService.findIfExistsById(id);
        return userMapper.toUserResponse(user);
    }

    public void deleteUserById( UUID id) {
        User user = userValidationService.findIfExistsById(id);
        userRepository.delete(user);
    }

    public UserResponse createUser(UserRequest request) {
        userValidationService.alreadyExistsByUsernameCheck(request.username());
        userValidationService.alreadyExistsByEmailCheck(request.email());
        User user = userMapper.toUser(request);
        return userMapper.toUserResponse(userRepository.saveAndFlush(user));
    }

    @Transactional
    public UserResponse updateUser(UserRequest request) {
        //todo: доделать с keycloak
        return null;
    }

    public UserResponse getUser() {
        //todo: доделать с keycloak
        return null;
    }

    public void deleteUser() {
        //todo: доделать с keycloak
    }

}
