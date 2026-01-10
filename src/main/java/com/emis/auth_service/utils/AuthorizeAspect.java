package com.emis.auth_service.utils;


import com.emis.auth_service.enums.UserRole;
import com.emis.auth_service.exceptions.AuthAspectException;
import com.emis.auth_service.exceptions.UnauthorizedException;
import com.emis.auth_service.model.UserModel;
import com.emis.auth_service.provider.keycloak.KeycloakAuthProvider;
import com.emis.auth_service.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Arrays;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class AuthorizeAspect {


    private final KeycloakAuthProvider keycloakAuthProvider;
    private final UserRepository userRepository;



    @Around("@annotation(authorize)")
    public Object authorize(ProceedingJoinPoint joinPoint, Authorize authorize) throws Throwable {
        try {
            HttpServletRequest request = ((ServletRequestAttributes)
                    RequestContextHolder.currentRequestAttributes()).getRequest();
            String token = extractBearerToken(request);
            log.info("received token: {}", token);
            // here token has to be validated by calling this  method validateTokenGetUid()
            String userId = keycloakAuthProvider.validateTokenGetUid(token);
            Optional<UserModel> userModelOptional = userRepository.findByUserId(userId);
            if (userModelOptional.isEmpty()) {
                throw new UnauthorizedException("User not found");
            }
            UserModel user = userModelOptional.get();
            // 4️⃣ Role validation (if roles present in annotation)
            String[] requiredRoles = authorize.roles();
            if (requiredRoles.length > 0) {

                Set<String> userRoles = user.getRoles()
                        .stream()
                        .map(UserRole::name)
                        .collect(Collectors.toSet());

                boolean hasRole = Arrays.stream(requiredRoles)
                        .anyMatch(userRoles::contains);

                if (!hasRole) {
                    throw new UnauthorizedException("Insufficient permissions");
                }
            }
            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(user, null);
            SecurityContextHolder.getContext().setAuthentication(authentication);

            // 6️⃣ Proceed to actual method
            return joinPoint.proceed();
        } catch (Exception e) {
            throw new AuthAspectException("UNAUTHORIZED: " + e.getMessage());
        }
    }

    private String extractBearerToken(HttpServletRequest request) throws UnauthorizedException {
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null ) {
            throw new UnauthorizedException("Missing/invalid Bearer token");
        }
        return authHeader.replace("Bearer ", "").trim();
    }
}
