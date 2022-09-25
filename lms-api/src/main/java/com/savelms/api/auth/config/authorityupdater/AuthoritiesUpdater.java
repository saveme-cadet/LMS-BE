package com.savelms.api.auth.config.authorityupdater;

import com.savelms.core.user.domain.entity.User;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class AuthoritiesUpdater {

    private final UserRoleService userRoleService;

    @Autowired
    public AuthoritiesUpdater(UserRoleService userRoleService) {
        this.userRoleService = userRoleService;
    }

    public void update(User user) {
        Set<SimpleGrantedAuthority> authorities = user.getAuthorities();
        List<UserRole> userRoles = userRoleService.findByUser(user);

        Authentication newAuth = new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword(), actualAuthorities);

        SecurityContextHolder.getContext().setAuthentication(newAuth);
    }
}