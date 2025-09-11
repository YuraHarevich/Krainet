package ru.kharevich.userservice.util.other;


import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

import static ru.kharevich.userservice.util.constants.JwtNameConstants.REALM_ACCESS_CLAIM;
import static ru.kharevich.userservice.util.constants.JwtNameConstants.ROLES_CLAIM;
import static ru.kharevich.userservice.util.constants.JwtNameConstants.USERNAME_CLAIM_NAME;
import static ru.kharevich.userservice.util.constants.UserServiceConstantMessages.JWT_CLAIM_EXTRACT_EXCEPTION_MESSAGE;
import static ru.kharevich.userservice.util.constants.UserServiceConstantMessages.JWT_CONVERSION_EXCEPTION_MESSAGE;

@Component
@Slf4j
public class JwtUtils {

    public String getUsername() {
        Authentication authentication = getAuthentication();

        if (authentication instanceof JwtAuthenticationToken jwtAuth) {
            Jwt jwt = jwtAuth.getToken();
            Object preferredUsername = jwt.getClaims().get(USERNAME_CLAIM_NAME);

            if (preferredUsername != null) {
                return preferredUsername.toString();
            } else {
                throw new IllegalStateException(JWT_CLAIM_EXTRACT_EXCEPTION_MESSAGE.formatted(USERNAME_CLAIM_NAME));
            }
        }

        log.warn("unable to extract username from token");
        throw new IllegalStateException(JWT_CONVERSION_EXCEPTION_MESSAGE);
    }

    public String getUserRole() {
        Authentication authentication = getAuthentication();
        if (authentication instanceof JwtAuthenticationToken jwtAuth) {
            Jwt jwt = jwtAuth.getToken();

            Map<String, Object> realmAccess = jwt.getClaim(REALM_ACCESS_CLAIM);
            if (realmAccess == null) {
                throw new IllegalStateException(JWT_CLAIM_EXTRACT_EXCEPTION_MESSAGE.formatted(REALM_ACCESS_CLAIM));
            }
            List<String> roles = (List<String>) realmAccess.get(ROLES_CLAIM);
            if (roles == null || roles.isEmpty()) {
                throw new IllegalStateException(JWT_CLAIM_EXTRACT_EXCEPTION_MESSAGE.formatted(ROLES_CLAIM));
            }
            if (roles.contains("ADMIN")) {
                return "ADMIN";
            } else if (roles.contains("USER")) {
                return "USER";
            } else {
                log.warn("cant find suitable role (USER / ADMIN)");
                throw new IllegalStateException(JWT_CONVERSION_EXCEPTION_MESSAGE);
            }
        }
        log.warn("unable to extract realm access from token");
        throw new IllegalStateException(JWT_CONVERSION_EXCEPTION_MESSAGE);
    }

    private Authentication getAuthentication(){
        return SecurityContextHolder.getContext().getAuthentication();
    }

}
