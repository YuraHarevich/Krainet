package ru.kharevich.userservice.util.validation;

import ru.kharevich.userservice.model.User;

import java.util.UUID;

public interface UserValidationService {

    void alreadyExistsByEmailCheck(String email);

    void alreadyExistsByUsernameCheck(String username);

    User alreadyExistsByUsernameOrEmailExceptPresentUsernameOrEmail(String username, String email, UUID id);

    User alreadyExistsByUsernameOrEmailExceptPresentUsernameOrEmail(String username, String email, String originUsername);

    User findIfExistsById(UUID id);

    User findIfExistsByUsername(String username);

}
