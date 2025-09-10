package ru.kharevich.userservice.config;

import lombok.RequiredArgsConstructor;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.kharevich.userservice.util.properties.KeycloakConnectionProperties;


@RequiredArgsConstructor
@Configuration
public class KeycloakConfig {

    private final KeycloakConnectionProperties keycloakConnectionProperties;

    @Bean
    public Keycloak keycloak() {
        return KeycloakBuilder.builder()
                .serverUrl(keycloakConnectionProperties.getAuthUrl())
                .realm(keycloakConnectionProperties.getRealm())
                .grantType(OAuth2Constants.CLIENT_CREDENTIALS)
                .clientId(keycloakConnectionProperties.getClientId())
                .clientSecret(keycloakConnectionProperties.getClientSecret())
                .build();
    }

}