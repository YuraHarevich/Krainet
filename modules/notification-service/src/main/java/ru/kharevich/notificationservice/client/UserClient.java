package ru.kharevich.notificationservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.kharevich.notificationservice.config.FeignConfig;
import ru.kharevich.notificationservice.decoders.UserServiceDecoder;
import ru.kharevich.notificationservice.dto.response.AdminResponse;

import java.util.List;

@FeignClient(
        name = "user-service",
        configuration = {UserServiceDecoder.class, FeignConfig.class}
)
public interface UserClient {

    @GetMapping("/api/v1/users/admin/all")
    List<AdminResponse> getAdminList();

}