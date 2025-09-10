package ru.kharevich.userservice.dto.response;

import java.util.UUID;

public record AdminResponse (
    String username,
    String email
) {
}
