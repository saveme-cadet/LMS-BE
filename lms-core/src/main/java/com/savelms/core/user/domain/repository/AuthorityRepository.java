package com.savelms.core.user.domain.repository;

import com.savelms.core.user.domain.entity.Authority;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorityRepository extends JpaRepository<Authority, Integer> {

}

