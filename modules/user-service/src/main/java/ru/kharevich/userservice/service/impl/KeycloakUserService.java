package ru.kharevich.userservice.service.impl;

import jakarta.ws.rs.ClientErrorException;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.CreatedResponseUtil;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.RolesResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.AccessTokenResponse;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ru.kharevich.userservice.dto.request.RegistrationRequest;
import ru.kharevich.userservice.dto.request.SignInRequest;
import ru.kharevich.userservice.dto.request.UserRequest;
import ru.kharevich.userservice.service.AuthenticationProviderService;
import ru.kharevich.userservice.util.exception.UserModifyingException;
import ru.kharevich.userservice.util.exception.WrongCredentialsException;
import ru.kharevich.userservice.util.properties.KeycloakConnectionProperties;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import static ru.kharevich.userservice.util.constants.UserServiceConstantMessages.USER_CREATION_EXCEPTION_MESSAGE;
import static ru.kharevich.userservice.util.constants.UserServiceConstantMessages.USER_DELETE_EXCEPTION_MESSAGE;
import static ru.kharevich.userservice.util.constants.UserServiceConstantMessages.USER_UPDATE_EXCEPTION_MESSAGE;
import static ru.kharevich.userservice.util.constants.UserServiceConstantMessages.WRONG_CREDENTIALS_MESSAGE;

@Service
@RequiredArgsConstructor
@Slf4j
public class KeycloakUserService implements AuthenticationProviderService {

    private final Keycloak keycloak;

    private final KeycloakConnectionProperties keycloakProperties;

    public UUID createUser(RegistrationRequest request) {
        CredentialRepresentation credential = new CredentialRepresentation();
        credential.setType(CredentialRepresentation.PASSWORD);
        credential.setValue(request.password());

        Map<String, List<String>> attributes = new HashMap<>();

        UserRepresentation keycloakUser = new UserRepresentation();
        keycloakUser.setUsername(request.username());
        keycloakUser.setFirstName(request.firstname());
        keycloakUser.setLastName(request.lastname());
        keycloakUser.setEmail(request.email());
        keycloakUser.setCredentials(List.of(credential));
        keycloakUser.setEnabled(true);
        keycloakUser.setAttributes(attributes);

        RealmResource realmResource = keycloak.realm(keycloakProperties.getRealm());
        UsersResource usersResource = realmResource.users();

        try (Response response = usersResource.create(keycloakUser)) {
            if (response.getStatus() == HttpStatus.CREATED.value()) {
                String keycloakUserId = CreatedResponseUtil.getCreatedId(response);
                assignRoleToUser(realmResource, usersResource, keycloakProperties.getDefaultRole(), keycloakUserId);
                return UUID.fromString(keycloakUserId);
            } else {
                String errorResponse = response.readEntity(String.class);
                log.error("KeycloakUserServiceImpl.createKeycloakUser: Failed to create Keycloak user. Status: {}, Error: {}",
                        response.getStatus(), errorResponse);
                throw new UserModifyingException(USER_CREATION_EXCEPTION_MESSAGE);
            }
        } catch (Exception e) {
            log.error("KeycloakUserServiceImpl.createKeycloakUser: Exception while creating Keycloak user", e);
            throw new UserModifyingException(USER_CREATION_EXCEPTION_MESSAGE);
        }
    }

    public AccessTokenResponse sighIn(SignInRequest request) {
        try {
            Keycloak userKeycloak = KeycloakBuilder.builder()
                    .serverUrl(keycloakProperties.getAuthUrl())
                    .realm(keycloakProperties.getRealm())
                    .grantType(OAuth2Constants.PASSWORD)
                    .clientId(keycloakProperties.getClientId())
                    .clientSecret(keycloakProperties.getClientSecret())
                    .username(request.username())
                    .password(request.password())
                    .build();
            return userKeycloak.tokenManager().getAccessToken();
        } catch (ClientErrorException exception) {
            log.debug("KeycloakUserServiceImpl.sighIn: " + exception.getMessage());
            throw new WrongCredentialsException(WRONG_CREDENTIALS_MESSAGE);
        }
    }

    public void updateUser(String userId, UserRequest request) {
        RealmResource realmResource = keycloak.realm(keycloakProperties.getRealm());
        UsersResource usersResource = realmResource.users();
        UserResource userResource = usersResource.get(userId);

        UserRepresentation userRepresentation = userResource.toRepresentation();
        userRepresentation.setUsername(request.username());
        userRepresentation.setFirstName(request.firstname());
        userRepresentation.setLastName(request.lastname());
        userRepresentation.setEmail(request.email());
        userRepresentation.setEnabled(true);

        try {
            userResource.update(userRepresentation);
        }catch (ClientErrorException e) {
            Response response = e.getResponse();
            String errorBody = response.readEntity(String.class);
            log.error("Keycloak 400 error: Status: {}, Body: {}",
                    response.getStatus(), errorBody);
            throw new UserModifyingException(USER_UPDATE_EXCEPTION_MESSAGE);

        } catch (Exception e) {
            log.debug("KeycloakUserServiceImpl.updateUser: " + e);
            throw new UserModifyingException(USER_UPDATE_EXCEPTION_MESSAGE);
        }
    }

    public void deleteUser(String userId) {
        RealmResource realmResource = keycloak.realm(keycloakProperties.getRealm());
        UsersResource usersResource = realmResource.users();

        UserResource userResource = usersResource.get(userId);
        try {
            userResource.remove();
        } catch (Exception e) {
            log.debug("KeycloakUserServiceImpl.deleteUser: " + e);
            throw new UserModifyingException(USER_DELETE_EXCEPTION_MESSAGE);
        }
    }

    private void assignRoleToUser(RealmResource realmResource, UsersResource usersResource, String role, String userId) {
        RolesResource rolesResource = realmResource.roles();
        RoleRepresentation roleRepresentation = rolesResource.get(role).toRepresentation();
        UserResource userResource = usersResource.get(userId);
        userResource.roles().realmLevel().add(List.of(roleRepresentation));
        log.info("KeycloakUserServiceImpl.role {} successfully assigned to user with id {}", role, userId);
    }

    public List<UserRepresentation> getUsersWithAdminRole() {
        RealmResource realmResource = keycloak.realm("krainet-realm");
        RolesResource rolesResource = realmResource.roles();

        // Находим роль ADMIN
        RoleRepresentation adminRole = rolesResource.get("ADMIN").toRepresentation();

        // Получаем всех пользователей с этой ролью
        List<UserRepresentation> adminUsers = realmResource.users()
                .list() // можно добавить параметры: first, max, search и т.д.
                .stream()
                .filter(user -> hasRole(user.getId(), adminRole))
                .collect(Collectors.toList());

        return adminUsers;
    }

    private boolean hasRole(String userId, RoleRepresentation role) {
        UserResource userResource = keycloak.realm("krainet-realm").users().get(userId);
        List<RoleRepresentation> userRoles = userResource.roles().realmLevel().listAll();
        return userRoles.stream().anyMatch(r -> r.getName().equals(role.getName()));
    }

}
