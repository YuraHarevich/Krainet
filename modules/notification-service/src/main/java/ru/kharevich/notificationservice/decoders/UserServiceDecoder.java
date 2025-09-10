package ru.kharevich.notificationservice.decoders;

import feign.Response;
import feign.Util;
import feign.codec.ErrorDecoder;
import lombok.extern.slf4j.Slf4j;
import ru.kharevich.notificationservice.exceptions.UserNotFound;

import java.io.IOException;

import static ru.kharevich.notificationservice.util.NotificationServiceConstants.USER_NOT_FOUND;
import static ru.kharevich.notificationservice.util.NotificationServiceConstants.USER_SERVICE_UNAVAILABLE;

@Slf4j
public class UserServiceDecoder implements ErrorDecoder {
    @Override
    public Exception decode(String methodKey, Response response) {
        String responseBody = "";

        try {
            if (response.body() != null) {
                responseBody = Util.toString(response.body().asReader());
            }
        } catch (IOException e) {
            log.error("Failed to read response body", e);
        }

        log.error("Feign error in method [{}]: status={}, reason={}, responseBody={}",
                methodKey, response.status(), response.reason(), responseBody);

        if (response.status() == 404) {
            return new UserNotFound(USER_NOT_FOUND);
        }
        return new UserNotFound(USER_SERVICE_UNAVAILABLE);
    }
}