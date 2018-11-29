package ru.javaops.bootjava.restaurantvoting.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.javaops.bootjava.restaurantvoting.AuthUser;
import ru.javaops.bootjava.restaurantvoting.model.User;
import ru.javaops.bootjava.restaurantvoting.repository.UserRepository;

import java.util.Optional;

@Slf4j
@AllArgsConstructor
@Service("userDetailsService")
public class UserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {

    private UserRepository userRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(final String email) {
        log.debug("Authenticating {}", email);
        Optional<User> optionalUser = userRepository.findByEmailIgnoreCase(email);
        return new AuthUser(optionalUser.orElseThrow(
                () -> new UsernameNotFoundException("User '" + email + "' was not found")));
    }
}