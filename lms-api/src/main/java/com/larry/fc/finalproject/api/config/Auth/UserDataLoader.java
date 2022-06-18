package com.larry.fc.finalproject.api.config.Auth;

import com.larry.fc.finalproject.core.domain.entity.repository.AuthorityRepository;
import com.larry.fc.finalproject.core.domain.entity.repository.RoleRepository;
import com.larry.fc.finalproject.core.domain.entity.repository.UserRepository;
import com.larry.fc.finalproject.core.domain.entity.user.Authority;
import com.larry.fc.finalproject.core.domain.entity.user.Role;
import com.larry.fc.finalproject.core.domain.entity.user.User;
import java.util.Arrays;
import javax.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import java.util.Set;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

//테스트 케이스 입력용
@Slf4j
@RequiredArgsConstructor
@Component
public class UserDataLoader implements CommandLineRunner {

    private final EntityManager em;

    private final AuthorityRepository authorityRepository;
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    private void loadSecurityData() {
        //beer auths
        Authority createUser = authorityRepository.save(
            Authority.builder().permission("user.create").build());
        Authority updateUser = authorityRepository.save(
            Authority.builder().permission("user.update").build());
        Authority readUser = authorityRepository.save(
            Authority.builder().permission("user.read").build());
        Authority deleteUser = authorityRepository.save(
            Authority.builder().permission("user.delete").build());

        Role adminRole = roleRepository.save(Role.builder().name("ADMIN").build());
        Role managerRole = roleRepository.save(Role.builder().name("MANAGER").build());
        Role userRole = roleRepository.save(Role.builder().name("USER").build());
        Role unAuthorizedRole = roleRepository.save(Role.builder().name("UNAUTHORIZED").build());

        adminRole.setAuthorities(Set.of(createUser, updateUser, readUser, deleteUser));

        managerRole.setAuthorities(Set.of(readUser));

        userRole.setAuthorities(Set.of(readUser));

        roleRepository.saveAll(Arrays.asList(adminRole, managerRole, userRole));

        userRepository.save(User.builder()
            .username("admin")
            .password(passwordEncoder.encode("admin"))
            .role(adminRole)
            .attendStatus(1L)
            .build());

        userRepository.save(User.builder()
            .username("manager")
            .password(passwordEncoder.encode("manager"))
            .role(managerRole)
            .attendStatus(1L)

            .build());

        userRepository.save(User.builder()
            .username("user")
            .password(passwordEncoder.encode("user"))
            .role(userRole)
            .attendStatus(1L)
            .build());

        userRepository.save(User.builder()
            .username("unAuthorized")
            .password(passwordEncoder.encode("unAuthorized"))
            .role(unAuthorizedRole)
            .attendStatus(1L)
            .build());

        log.debug("Users Loaded: " + userRepository.count());
    }

    @Override
    public void run(String... args) throws Exception {
        if (authorityRepository.count() == 0) {
            loadSecurityData();
        }
    }
}
