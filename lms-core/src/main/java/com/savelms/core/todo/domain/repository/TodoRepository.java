package com.savelms.core.todo.domain.repository;

import com.savelms.core.todo.domain.entity.Todo;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TodoRepository extends JpaRepository<Todo, Long> {

    @Query("SELECT t FROM Todo t "
        + "join fetch t.user "
        + "join fetch t.calendar "
        + "WHERE t.user.apiId = :apiId AND t.calendar.date = :todoDay")
    List<Todo> findByApiIdAndTodoDayFetchJoin(@Param("apiId")String apiId, @Param("todoDay") LocalDate todoDay);


    @Query("SELECT t FROM Todo t "
        + "join fetch t.user "
        + "join fetch t.calendar "
        + "WHERE  t.calendar.date = :todoDay")
    List<Todo> findByTodoDayFetchJoin(@Param("todoDay") LocalDate todoDay);


}
