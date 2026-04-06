package org.example.multi_tenant_task.entities.user;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;

@RequiredArgsConstructor
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = userRepository.findByEmailIgnoreCase(username).orElseThrow(()
                -> new UsernameNotFoundException("User not found"));

        return new CustomUserDetails(user);
    }
//
//    private Collection<? extends GrantedAuthority> getAuthority(User user) {
//        return user.getRole().stream().
//                map(role -> new SimpleGrantedAuthority("ROLE_" + role.getRole().name())).toList();
//    }
}
