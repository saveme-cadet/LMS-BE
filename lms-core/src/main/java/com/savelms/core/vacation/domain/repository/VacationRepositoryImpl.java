package com.savelms.core.vacation.domain.repository;

import com.savelms.core.vacation.domain.entity.Vacation;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Optional;

@Repository
public class VacationRepositoryImpl implements VacationQueryRepository {

    @PersistenceContext
    private EntityManager em;

    @Override
    public Optional<Vacation> findFirstByUserId(Long userId) {
        String query = "select v from Vacation v where v.user.id = :userId order by v.id desc";

        Vacation vacation = em.createQuery(query, Vacation.class)
                .setParameter("userId", userId)
                .setMaxResults(1)
                .getSingleResult();

        return Optional.ofNullable(vacation);
    }

    @Override
    public Optional<Vacation> findFirstByUsername(String username) {
        String query = "select v from Vacation v where v.user.username = :username order by v.id desc";

        Vacation vacation = em.createQuery(query, Vacation.class)
                .setParameter("username", username)
                .setMaxResults(1)
                .getSingleResult();

        return Optional.ofNullable(vacation);
    }

}
