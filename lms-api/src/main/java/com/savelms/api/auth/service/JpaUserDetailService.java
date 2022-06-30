package com.savelms.api.auth.service;


import com.savelms.core.user.domain.entity.User;
import com.savelms.core.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class JpaUserDetailService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username).orElseThrow(() -> {
                return new UsernameNotFoundException("User name: " + username + " not found");
            }
        );
        return new org.springframework.security.core.userdetails.User(user.getUsername(),
            user.getPassword(), user.getEnabled(), user.getAccountNonExpired(),
            user.getCredentialsNonExpired(), user.getAccountNonLocked(), user.getAuthorities());
    }

}
