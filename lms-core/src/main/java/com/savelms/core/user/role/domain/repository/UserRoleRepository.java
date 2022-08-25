package com.savelms.core.user.role.domain.repository;

import com.savelms.core.user.role.domain.entity.UserRole;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRoleRepository extends JpaRepository<UserRole, Long> {

    @Query("SELECT ur FROM UserRole ur WHERE ur.user.id = :userId AND ur.currentlyUsed = true")
    public List<UserRole> currentlyUsedUserRole(@Param("userId") Long userId);



}
