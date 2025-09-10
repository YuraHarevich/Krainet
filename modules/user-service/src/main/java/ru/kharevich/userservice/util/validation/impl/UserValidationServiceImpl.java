package ru.kharevich.userservice.util.validation.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.kharevich.userservice.model.User;
import ru.kharevich.userservice.repository.UserRepository;
import ru.kharevich.userservice.util.exception.UserAlreadyExistsException;
import ru.kharevich.userservice.util.exception.UserNotFoundException;
import ru.kharevich.userservice.util.validation.UserValidationService;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static ru.kharevich.userservice.util.constants.UserServiceConstantMessages.USER_NOT_FOUND_BY_ID;
import static ru.kharevich.userservice.util.constants.UserServiceConstantMessages.USER_NOT_FOUND_BY_USERNAME;
import static ru.kharevich.userservice.util.constants.UserServiceConstantMessages.USER_WITH_SUCH_EMAIL_ALREADY_EXISTS;
import static ru.kharevich.userservice.util.constants.UserServiceConstantMessages.USER_WITH_SUCH_EMAIL_OR_USERNAME_ALREADY_EXISTS;
import static ru.kharevich.userservice.util.constants.UserServiceConstantMessages.USER_WITH_SUCH_USERNAME_ALREADY_EXISTS;

@Service
@RequiredArgsConstructor
public class UserValidationServiceImpl implements UserValidationService {

    private final UserRepository userRepository;

    public void alreadyExistsByUsernameCheck(String username) {
        Optional<User> user = userRepository.findByUsernameIgnoreCase(username);
        user.ifPresent(u-> {
            throw new UserAlreadyExistsException(USER_WITH_SUCH_USERNAME_ALREADY_EXISTS.formatted(username));
        });
    }

    // метод служит для валидации изменения данных пользователя. Если введенные мэйл или username отсутствуют либо принадлежат этому пользователю, то все норм, иначе - ошибка
    public User alreadyExistsByUsernameOrEmailExceptPresentUsernameOrEmail(String username, String email, UUID id) {
        Optional<User> user = userRepository.findById(id);
        List<User> usersWithUsernameAndEmailMatch = userRepository.findByEmailOrUsername(email, username);
        if(user.isPresent()) {
            if(usersWithUsernameAndEmailMatch.size() == 1) {
                if(!usersWithUsernameAndEmailMatch.getFirst().getUsername().equals(username)) {
                    throw new UserAlreadyExistsException(USER_WITH_SUCH_EMAIL_OR_USERNAME_ALREADY_EXISTS);
                }
            }
            if(usersWithUsernameAndEmailMatch.size() == 2) {
                throw new UserAlreadyExistsException(USER_WITH_SUCH_EMAIL_OR_USERNAME_ALREADY_EXISTS);
            }
        }
        else  {
            throw new UserNotFoundException(USER_NOT_FOUND_BY_ID);
        }

        return user.get();

    }

    public User alreadyExistsByUsernameOrEmailExceptPresentUsernameOrEmail(String username, String email, String originUsername) {
        Optional<User> user = userRepository.findByUsernameIgnoreCase(originUsername);
        List<User> usersWithUsernameAndEmailMatch = userRepository.findByEmailOrUsername(email, username);
        if(user.isPresent()) {
            if(usersWithUsernameAndEmailMatch.size() == 1) {
                if(!usersWithUsernameAndEmailMatch.getFirst().getUsername().equals(username)) {
                    throw new UserAlreadyExistsException(USER_WITH_SUCH_EMAIL_OR_USERNAME_ALREADY_EXISTS);
                }
            }
            if(usersWithUsernameAndEmailMatch.size() == 2) {
                throw new UserAlreadyExistsException(USER_WITH_SUCH_EMAIL_OR_USERNAME_ALREADY_EXISTS);
            }
        }
        else  {
            throw new UserNotFoundException(USER_NOT_FOUND_BY_ID);
        }

        return user.get();

    }

    public void alreadyExistsByEmailCheck(String email) {
        Optional<User> user = userRepository.findByEmail(email);
        user.ifPresent(u-> {
            throw new UserAlreadyExistsException(USER_WITH_SUCH_EMAIL_ALREADY_EXISTS.formatted(email));
        });
    }

    public User findIfExistsById(UUID id) {
        Optional<User> user = userRepository.findById(id);
        if(user.isPresent()) {
            return user.get();
        }
        else {
            throw new UserNotFoundException(USER_NOT_FOUND_BY_ID.formatted(id.toString()));
        }
    }

    public User findIfExistsByUsername(String username) {
        Optional<User> user = userRepository.findByUsernameIgnoreCase(username);
        if(user.isPresent()) {
            return user.get();
        }
        else {
            throw new UserNotFoundException(USER_NOT_FOUND_BY_USERNAME.formatted(username));
        }
    }

}
