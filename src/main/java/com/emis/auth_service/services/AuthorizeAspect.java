package com.emis.auth_service.services;


import com.emis.auth_service.exceptions.UnauthorizedException;
import com.emis.auth_service.utils.Authorize;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class AuthorizeAspect {



    @Around("@annotation(authorize)")
    public Object authorize(ProceedingJoinPoint joinPoint, Authorize authorize) throws Throwable {
        
        HttpServletRequest request = ((ServletRequestAttributes) 
            RequestContextHolder.currentRequestAttributes()).getRequest();
        
        String token = extractBearerToken(request);
        log.info("received token: {}", token);
        if(true)
           throw new  UnauthorizedException("UNAUTHORIZED EXCEPTION DEMO");
//        var tokenInfo = keycloakTokenService.introspectToken(token);
//
//        if (!tokenInfo.active()) {
//            throw new UnauthorizedException("Token inactive/expired");
//        }
//
//        String sid = tokenInfo.sub();
//        String username = tokenInfo.username();
//
//        // 🔥 GENERALIZED LOGIC
//        if (authorize.requireRoles() && authorize.roles().length > 0) {
//            // Full role validation from UserModel
//            validateUserRoles(sid, authorize.roles());
//        } // else: Token validated ✅ No role check needed
//
//        setSecurityContext(username, tokenInfo.roles());
//        request.setAttribute("sid", sid);
//        request.setAttribute("username", username);

        return joinPoint.proceed();
    }

    private String extractBearerToken(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
//        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
//            throw new UnauthorizedException("Missing/invalid Bearer token");
//        }
        return authHeader.substring(7);
    }

//    private void validateUserRoles(String sid, String[] requiredRoles) {
//        boolean hasRoles = userRoleService.hasRequiredRoles(sid, requiredRoles);
//        if (!hasRoles) {
//            throw new UnauthorizedException("Missing roles: " +
//                String.join(", ", requiredRoles));
//        }
//    }
//
//    private void setSecurityContext(String username, List<String> roles) {
//        var authorities = roles.stream()
//            .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
//            .toList();
//
//        SecurityContextHolder.getContext().setAuthentication(
//            new org.springframework.security.authentication.UsernamePasswordAuthenticationToken(
//                username, null, authorities
//            )
//        );
//    }
}
