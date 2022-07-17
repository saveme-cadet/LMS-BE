package com.savelms.core.user.emailauth.domain.repository;

import com.savelms.core.user.emailauth.domain.entity.EmailAuth;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmailAuthRepository extends JpaRepository<EmailAuth, Long> {

}
