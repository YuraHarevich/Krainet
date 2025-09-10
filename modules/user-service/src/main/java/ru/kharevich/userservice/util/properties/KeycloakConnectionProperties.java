package ru.kharevich.userservice.util.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "keycloak")
@Getter
@Setter
public class KeycloakConnectionProperties {

    private String clientId;

    private String domain;

    private String clientSecret;

    private String authUrl;

    private String realm;

    private String defaultRole;

}
