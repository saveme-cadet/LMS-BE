package com.savelms.core.team.domain.repository;

import com.savelms.core.team.domain.entity.UserTeam;
import java.util.List;
import javax.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class UserTeamCustomRepository {

    private final EntityManager em;

    public List<UserTeam> currentlyUsedUserTeam(Long userId) {
        return em.createQuery(
                "SELECT ut FROM UserTeam ut WHERE ut.user.id = :userId AND ut.currentlyUsed = true",
                UserTeam.class)
            .setParameter("userId", userId).getResultList();
    }
}
