package com.savelms.core.user.domain.repository;

import com.savelms.core.user.domain.repository.dto.UserSortRuleDto;
import com.savelms.core.user.domain.entity.User;
import java.util.List;
import javax.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class UserCustomRepository {

    private final EntityManager em;
    public List<User> findAllAndSortAndPage(Boolean attendStatus, Long offset, Long size, UserSortRuleDto sortRule) {

//        return em.createQuery(
//                "SELECT u FROM User u "
//                    + "WHERE u.attendStatus = :attendStatus "
//                    + "ORDER BY u.:field :sortType",
//                User.class)
//            .setParameter("attendStatus", attendStatus)
//            .setParameter("field", sortRule.getFieldName())
//            .setParameter("sortType", sortRule.getSortType())
//            .setFirstResult(offset.intValue())
//            .setMaxResults(size.intValue())
//            .getResultList();

        return em.createQuery("SELECT u FROM User u"
            + " WHERE u.attendStatus = :attendStatus",
            User.class)
            .setParameter("attendStatus", attendStatus)
            .setFirstResult(offset.intValue())
            .setMaxResults(size.intValue())
            .getResultList();
    }
}
