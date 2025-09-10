package ru.kharevich.userservice.dto.other;

import ru.kharevich.userservice.model.Action;

import java.util.UUID;

public record Notification(

        UUID id,

        String username,

        String email,

        Action action

) {
}
