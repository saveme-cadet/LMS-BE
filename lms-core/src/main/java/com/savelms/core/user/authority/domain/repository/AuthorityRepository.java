package com.savelms.core.user.authority.domain.repository;

import com.savelms.core.user.authority.domain.entity.Authority;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorityRepository extends JpaRepository<Authority, Integer> {

    Optional<Authority> findByPermission(String s);
}

