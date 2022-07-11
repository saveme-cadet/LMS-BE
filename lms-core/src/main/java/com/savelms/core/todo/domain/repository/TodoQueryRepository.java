package com.savelms.core.todo.domain.repository;

import javax.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class TodoQueryRepository {

    private final EntityManager em;

//    List<AllUserTodoDto> findByTodoDayFetchJoin(@Param("todoDay") LocalDate todoDay){
//        return em.createQuery("SELECT new com.savelms.core.todo.domain.repository.dto.AllUserTodoDtoAll() FROM Todo t "
//                + "join fetch t.user "
//                + "join fetch t.calendar "
//                + "WHERE  t.calendar.date = :todoDay", AllUserTodoDto.class)
//            .getResultList();
//    }

}
