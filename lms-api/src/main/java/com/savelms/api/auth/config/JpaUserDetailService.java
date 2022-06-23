package com.savelms.api.auth.config;


import com.savelms.core.role.domain.entity.Role;
import com.savelms.core.user.domain.entity.Authority;
import com.savelms.core.user.domain.entity.User;
import com.savelms.core.user.domain.repository.UserRepository;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
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
        User user = userRepository.findByUsername(username).orElseThrow(() ->{
            return new UsernameNotFoundException("User name: " + username + " not found");
            }
        );

        return new org.springframework.security.core.userdetails.User(user.getUsername(),
            user.getPassword(),
            user.getEnabled(), user.getAccountNonExpired(), user.getCredentialsNonExpired(),
            user.getAccountNonLocked(), convertToSpringAuthorityCollection(user.getAuthorities()));
    }

    private Collection<? extends GrantedAuthority> convertToSpringAuthorityCollection(
        Set<Authority> authorities) {
        return authorities.stream()
            .map((a)->
                (new SimpleGrantedAuthority(a.getPermission())))
            .collect(Collectors.toSet());
    }

//    private Collection<? extends GrantedAuthority> convertToSpringAuthorityCollection(Role role) {
//        HashSet<SimpleGrantedAuthority> simpleGrantedAuthorities = new HashSet<>();
//        simpleGrantedAuthorities.add(new SimpleGrantedAuthority(role.name()));
//        return simpleGrantedAuthorities;
//    }
//    private Collection<? extends GrantedAuthority> convertToSpringAuthorities(
//        Set<Authority> authorities) {
//        if (authorities != null && authorities.size() != 0) {
//            return authorities.stream()
//                  .map(Authority::getRole)
//                .map(SimpleGrantedAuthority::new)
//                .collect(Collectors.toSet());
//        }else{
//            return new HashSet<>();
//        }
//    }
}
