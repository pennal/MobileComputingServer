package org.letsride.server.security;


import org.letsride.server.models.Account;
import org.letsride.server.models.User;
import org.letsride.server.repositories.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class AppUserDetailService implements UserDetailsService {
    private final UserRepository userRepository;

    public AppUserDetailService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    @Override
    public final UserDetails loadUserByUsername(String username) {
        final User user = this.userRepository.findByAccount_Username(username);

        if (user == null) {
            throw new UsernameNotFoundException("User not found");

        }

        Account a = user.getAccount();

        String password = (a.getPassword() == null ? "" : a.getPassword());

        return org.springframework.security.core.userdetails.User
                .withUsername(a.getUsername())
                .password(password)
                .authorities(a.getRole())
                .accountExpired(a.getExpired())
                .accountLocked(a.getLocked())
                .credentialsExpired(a.getCredentialsExpired())
                .disabled(a.getDisabled())
                .build();

    }


}