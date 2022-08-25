package com.savelms.core.vacation.domain.repository;

import com.savelms.core.vacation.domain.entity.Vacation;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public class VacationRepositoryImpl implements VacationQueryRepository {

    @PersistenceContext
    private EntityManager em;

    @Override
    public Optional<Vacation> findAllByDate(LocalDate date) {
        String sql = "select v from Vacation v where v.createdAt <= :date order by v.id desc";

        Vacation vacation = em.createQuery(sql, Vacation.class)
                .setParameter("date", date)
                .setMaxResults(1)
                .getSingleResult();

        return Optional.ofNullable(vacation);
    }

    @Override
    public Optional<Vacation> findFirstByUserApiId(String userApiId) {
        String query = "select v from Vacation v where v.user.apiId = :userApiId order by v.id desc";

        List<Vacation> vacations = em.createQuery(query, Vacation.class)
                .setParameter("userApiId", userApiId)
                .setMaxResults(1)
                .getResultList();

        return vacations.stream().findFirst();
    }

    @Override
    public Optional<Vacation> findFirstByUsername(String username) {
        String query = "select v from Vacation v where v.user.username = :username order by v.id desc";

        List<Vacation> vacations = em.createQuery(query, Vacation.class)
                .setParameter("username", username)
                .setMaxResults(1)
                .getResultList();

        return vacations.stream().findFirst();
    }
}
