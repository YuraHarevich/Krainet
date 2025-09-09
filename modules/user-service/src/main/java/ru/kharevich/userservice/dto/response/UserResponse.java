package ru.kharevich.userservice.dto.response;

import java.util.UUID;

public record UserResponse(
        UUID id,
        UUID keycloakId,
        String username,
        String firstname,
        String lastname,
        String email
) {}
