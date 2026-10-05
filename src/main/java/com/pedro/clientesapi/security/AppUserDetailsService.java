package com.pedro.clientesapi.security;

import com.pedro.clientesapi.entity.AppUser;
import com.pedro.clientesapi.repository.AppUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AppUserDetailsService implements UserDetailsService {

    private final AppUserRepository appUserRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AppUser appUser = appUserRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario nao encontrado: " + username));

        return User.builder()
                .username(appUser.getUsername())
                .password(appUser.getPasswordHash())
                .authorities("ROLE_ADMIN")
                .build();
    }
}
