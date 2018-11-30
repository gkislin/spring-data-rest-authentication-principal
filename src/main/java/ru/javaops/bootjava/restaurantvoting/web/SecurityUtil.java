package ru.javaops.bootjava.restaurantvoting.web;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import ru.javaops.bootjava.restaurantvoting.AuthUser;

import static java.util.Objects.requireNonNull;

public class SecurityUtil {

    public static AuthUser safeGet() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null) {
            return null;
        }
        Object principal = auth.getPrincipal();
        return (principal instanceof AuthUser) ? (AuthUser) principal : null;
    }

    public static AuthUser get() {
        AuthUser user = safeGet();
        requireNonNull(user, "No authorized user found");
        return user;
    }
}