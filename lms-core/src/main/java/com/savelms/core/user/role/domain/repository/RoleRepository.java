package com.savelms.core.user.role.domain.repository;

import com.savelms.core.user.role.RoleEnum;
import com.savelms.core.user.role.domain.entity.Role;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {


    Optional<Role> findByValue(RoleEnum roleEnum);
}
