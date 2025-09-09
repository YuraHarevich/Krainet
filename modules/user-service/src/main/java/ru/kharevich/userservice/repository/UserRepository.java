package ru.kharevich.userservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.kharevich.userservice.model.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByEmail(String email);

    Optional<User> findByUsername(String username);

    List<User> findByEmailOrUsername(String email, String username);
}
