package ru.kharevich.userservice.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.representations.AccessTokenResponse;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.stereotype.Service;
import ru.kharevich.userservice.dto.other.Notification;
import ru.kharevich.userservice.dto.request.RegistrationRequest;
import ru.kharevich.userservice.dto.request.SignInRequest;
import ru.kharevich.userservice.dto.request.UserRequest;
import ru.kharevich.userservice.dto.response.AdminResponse;
import ru.kharevich.userservice.dto.response.UserResponse;
import ru.kharevich.userservice.kafka.producer.NotificationProducer;
import ru.kharevich.userservice.model.Action;
import ru.kharevich.userservice.model.User;
import ru.kharevich.userservice.repository.UserRepository;
import ru.kharevich.userservice.service.AuthenticationProviderService;
import ru.kharevich.userservice.service.UserAdminService;
import ru.kharevich.userservice.service.UserService;
import ru.kharevich.userservice.util.mappers.UserMapper;
import ru.kharevich.userservice.util.other.JwtUtils;
import ru.kharevich.userservice.util.validation.UserValidationService;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService, UserAdminService {

    private final UserValidationService userValidationService;

    private final UserRepository userRepository;

    private final UserMapper userMapper;

    private final AuthenticationProviderService authenticationProviderService;

    private final JwtUtils jwtUtils;

    private final NotificationProducer notificationProducer;

    @Transactional
    public UserResponse updateUserById(UserRequest request, UUID id) {
        User user = userValidationService.alreadyExistsByUsernameOrEmailExceptPresentUsernameOrEmail(request.username(), request.email(), id);
        userMapper.updateUserByRequest(request, user);

        authenticationProviderService.updateUser(user.getKeycloakId().toString(), request);
        return userMapper.toUserResponse(user);
    }

    public UserResponse getUserById(UUID id) {
        User user = userValidationService.findIfExistsById(id);
        return userMapper.toUserResponse(user);
    }

    public void deleteUserById( UUID id) {
        User user = userValidationService.findIfExistsById(id);

        authenticationProviderService.deleteUser(user.getKeycloakId().toString());
        userRepository.delete(user);
    }

    public List<AdminResponse> getAllAdmins() {
        List<UserRepresentation> userRepresentations = authenticationProviderService.getUsersWithAdminRole();
        List<AdminResponse> adminResponseList = userRepresentations.stream().map(userMapper::toAdminResponse).toList();
        return adminResponseList;
    }

    public UserResponse createUser(RegistrationRequest request) {
        userValidationService.alreadyExistsByUsernameCheck(request.username());
        userValidationService.alreadyExistsByEmailCheck(request.email());
        User user = userMapper.toUser(request);

        UUID keycloakId = authenticationProviderService.createUser(request);
        user.setKeycloakId(keycloakId);
        return userMapper.toUserResponse(userRepository.saveAndFlush(user));
    }

    @Transactional
    public UserResponse updateUser(UserRequest request) {
        String username = jwtUtils.getUsername();
        User user = userValidationService.findIfExistsByUsername(username);
        userValidationService.alreadyExistsByUsernameOrEmailExceptPresentUsernameOrEmail(request.username(), request.email(), username);

        UUID tempId = user.getId();
        String tempUsername = user.getUsername();
        String tempEmail = user.getEmail();

        authenticationProviderService.updateUser(user.getKeycloakId().toString(), request);
        userMapper.updateUserByRequest(request, user);

        User updatedUser = userRepository.saveAndFlush(user);
        notificationProducer.sendNotification(new Notification(tempId, tempUsername, tempEmail, Action.UPDATE));
        return userMapper.toUserResponse(updatedUser);
    }

    public UserResponse getUser() {
        String username = jwtUtils.getUsername();
        User user = userValidationService.findIfExistsByUsername(username);

        notificationProducer.sendNotification(new Notification(user.getId(), user.getUsername(), user.getEmail(), Action.GET));
        return userMapper.toUserResponse(user);
    }

    public void deleteUser() {
        String username = jwtUtils.getUsername();
        User user = userValidationService.findIfExistsByUsername(username);
        authenticationProviderService.deleteUser(user.getKeycloakId().toString());

        notificationProducer.sendNotification(new Notification(user.getId(), user.getUsername(), user.getEmail(), Action.DELETE));
    }

    @Override
    public AccessTokenResponse sighIn(SignInRequest request) {
        AccessTokenResponse response = authenticationProviderService.sighIn(request);
        return response;
    }




}
