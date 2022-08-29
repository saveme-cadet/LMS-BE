package com.savelms.core.user.emailauth.domain.repository;

import com.savelms.core.user.emailauth.domain.entity.EmailAuth;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import javax.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class EmailAuthCustomRepository {

    private final EntityManager em;

    public Optional<EmailAuth> findValidAuthByEmail(String email, String authToken, LocalDateTime currentTime) {
        List<EmailAuth> emailAuth = em.createQuery("SELECT ea FROM EmailAuth ea "
                    + "where ea.email = :email and "
                    + "ea.authToken = :authToken and "
                    + "ea.expireDate > :currentTime and "
                    + "ea.expired = false",
                EmailAuth.class)
            .setParameter("email", email)
            .setParameter("authToken", authToken)
            .setParameter("currentTime", currentTime)
            .getResultList();
        if (emailAuth.isEmpty()) {
            return Optional.empty();
        }

        return Optional.ofNullable(emailAuth.get(0));
    }
}
