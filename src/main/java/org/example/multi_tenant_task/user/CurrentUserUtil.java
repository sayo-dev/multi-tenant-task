package org.example.multi_tenant_task.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class CurrentUserUtil {

    private final UserRepository userRepository;

    public User getLoggedInUser() {

        String username = getLoggedInUsername();

        return userRepository.findByEmailIgnoreCase(username)
                .orElseThrow(() -> new BadCredentialsException("Unauthorized"));
    }

    public String getLoggedInUsername() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName();

    }

}
