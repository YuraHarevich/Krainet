package ru.kharevich.notificationservice.model;


import java.util.UUID;

public record Notification(

        UUID id,

        String username,

        String email,

        Action action

) {
}