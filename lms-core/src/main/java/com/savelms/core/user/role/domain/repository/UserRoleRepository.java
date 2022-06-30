package com.savelms.core.user.role.domain.repository;

import com.savelms.core.user.role.domain.entity.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRoleRepository extends JpaRepository<UserRole, Long> {

}
