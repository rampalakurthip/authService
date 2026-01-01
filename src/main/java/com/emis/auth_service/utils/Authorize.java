package com.emis.auth_service.utils;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Authorize {
    String[] roles() default {};           // Roles to check (if requireRoles=true)
    boolean requireRoles() default false;  // NEW: Skip role check, just validate token
}
