package ru.kharevich.userservice.util.other;


import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;

import static ru.kharevich.userservice.util.constants.JwtNameConstants.USERNAME_CLAIM_NAME;
import static ru.kharevich.userservice.util.constants.UserServiceConstantMessages.JWT_CLAIM_EXTRACT_EXCEPTION_MESSAGE;
import static ru.kharevich.userservice.util.constants.UserServiceConstantMessages.JWT_CONVERSION_EXCEPTION_MESSAGE;

@Component
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

        throw new IllegalStateException(JWT_CONVERSION_EXCEPTION_MESSAGE);
    }

    private Authentication getAuthentication(){
        return SecurityContextHolder.getContext().getAuthentication();
    }

}
