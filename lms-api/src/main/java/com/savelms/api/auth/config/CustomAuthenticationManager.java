package com.savelms.api.auth.config;

import com.savelms.core.user.domain.entity.User;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
public class CustomAuthenticationManager {

    public Boolean userIdMatches(Authentication authentication, String userId) {
        User user = (User) authentication.getPrincipal();
        return user.getApiId().equals(userId);
    }
}
