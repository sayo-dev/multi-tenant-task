package org.example.multi_tenant_task.component;

import lombok.RequiredArgsConstructor;
import org.example.multi_tenant_task.entities.user.User;
import org.example.multi_tenant_task.entities.user.UserRepository;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@RequiredArgsConstructor
@Component
public class AuditorAwareImpl implements AuditorAware<User> {

    private final UserRepository userRepository;

    @Override
    public Optional<User> getCurrentAuditor() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() || auth.getName().equals("anonymousUser")) {

            return Optional.empty();
        }

        User user = userRepository.findByEmailIgnoreCase(auth.getName()).get();
        return Optional.of(user);
    }
}
