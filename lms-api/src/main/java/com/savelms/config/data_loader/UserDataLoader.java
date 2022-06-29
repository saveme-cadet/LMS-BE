package com.savelms.config.data_loader;

import com.savelms.core.user.role.RoleEnum;
import com.savelms.core.user.role.domain.entity.Role;
import com.savelms.core.user.role.domain.entity.UserRole;
import com.savelms.core.user.role.domain.repository.RoleRepository;
import com.savelms.core.user.role.domain.repository.UserRoleRepository;
import com.savelms.core.user.authority.domain.entity.Authority;
import com.savelms.core.user.domain.entity.User;
import com.savelms.core.user.authority.domain.repository.AuthorityRepository;
import com.savelms.core.user.domain.repository.UserRepository;
import java.util.Arrays;
import java.util.UUID;
import javax.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

//테스트 케이스 입력용
@RequiredArgsConstructor
@Component
@Slf4j
public class UserDataLoader implements CommandLineRunner {

    private final EntityManager em;
    private final BCryptPasswordEncoder passwordEncoder;
    private final AuthorityRepository authorityRepository;
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;

    @Transactional
    public void loadSecurityData() {
        // Authority 생성
        Authority createUser = saveNewAuthority("user.create");
        Authority updateUser = saveNewAuthority("user.update");
        Authority readUser = saveNewAuthority("user.read");
        Authority deleteUser = saveNewAuthority("user.delete");

        //Role 생성
        Role adminRole = saveNewRole(RoleEnum.ROLE_ADMIN.name());
        Role managerRole = saveNewRole(RoleEnum.ROLE_MANAGER.name());
        Role userRole = saveNewRole(RoleEnum.ROLE_USER.name());
        Role unauthorizedRole = saveNewRole(RoleEnum.ROLE_UNAUTHORIZED.name());

        adminRole.addAuthorities(createUser, updateUser, readUser, deleteUser);
        managerRole.addAuthorities(createUser, updateUser, readUser, deleteUser);
        userRole.addAuthorities(createUser, updateUser, readUser, deleteUser);
        unauthorizedRole.addAuthorities(createUser, updateUser, readUser, deleteUser);

        roleRepository.saveAll(Arrays.asList(adminRole, managerRole, userRole, unauthorizedRole));

        UserRole adminUserRole = UserRole.builder()
            .role(adminRole)
            .build();
        User admin = User.createDefaultUser("admin", passwordEncoder.encode("admin"),
            "admin@gmail.com");
        adminUserRole.setUserAndUserRoleToUser(admin);


        UserRole managerUserRole = UserRole.builder()
            .role(managerRole)
            .build();
        User manager = User.createDefaultUser("manager", passwordEncoder.encode("manager"),
            "manager@gmail.com");
        managerUserRole.setUserAndUserRoleToUser(manager);

        UserRole userUserRole = UserRole.builder()
            .role(userRole)
            .build();
        User user = User.createDefaultUser("user", passwordEncoder.encode("user"),
            "user@gmail.com");

        userUserRole.setUserAndUserRoleToUser(user);

        UserRole unauthorizedUserRole = UserRole.builder()
            .role(unauthorizedRole)
            .build();
        User unauthorized = User.createDefaultUser("unauthorized",
            passwordEncoder.encode("unauthorized"),
            "unauthorized@gmail.com");
        unauthorizedUserRole.setUserAndUserRoleToUser(unauthorized);



        userRepository.saveAll(Arrays.asList(admin, manager, user, unauthorized));
        userRoleRepository.saveAll(
            Arrays.asList(adminUserRole, managerUserRole, userUserRole, unauthorizedUserRole));

    }


    private Role saveNewRole(String admin) {
        Role role = Role.builder().name(admin).build();
        return roleRepository.save(role);
    }

    private Authority saveNewAuthority(String s) {
        return authorityRepository.save(
            Authority.builder().permission(s).build());
    }


    @Override
    public void run(String... args) throws Exception {
        if (authorityRepository.count() == 0) {
            loadSecurityData();
        }
    }
}
