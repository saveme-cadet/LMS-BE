package com.larry.fc.finalproject.core.domain.entity.repository;

import com.larry.fc.finalproject.core.domain.entity.Todo;
import com.larry.fc.finalproject.core.domain.entity.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface TodoRepository extends JpaRepository<Todo, Long> {
    List<Todo> findAllByWriter_Id(Long id);
    List<Todo> deleteTodoByWriter_Id(Long id);
    Optional<Todo> findByTodoIdAndTodoDayAndWriter_Id(Long id, LocalDate localDate, Long userId);
    List<Todo> deleteTodoByWriter_IdAndTodoIdAndTodoDay(Long id, Long todoId, LocalDate date);

    //boolean existsByTableDayAndCadet_Id(LocalDate date, Long writer_id);
}
