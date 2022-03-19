package com.larry.fc.finalproject.api.service;

import com.larry.fc.finalproject.api.dto.AuthUser;
import com.larry.fc.finalproject.api.dto.tododto.DeleteTodoDto;
import com.larry.fc.finalproject.api.dto.tododto.TodoDto;
import com.larry.fc.finalproject.api.util.DtoConverter;
import com.larry.fc.finalproject.core.domain.entity.Todo;
import com.larry.fc.finalproject.core.domain.entity.repository.TodoRepository;
import com.larry.fc.finalproject.core.domain.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class TodoService {
    private final UserService userService;
    private final TodoRepository todoRepository;

    public void create(TodoDto todoDto){
        todoRepository.findByTodoIdAndTodoDayAndWriter_Id(todoDto.getTodoId(), todoDto.getTodoDay(), todoDto.getWriterId())
                .ifPresent(u -> {
                    throw new RuntimeException("todoId already exit!");
                });
        final Todo todo = Todo.todoJoin(
                todoDto.getTitle(),
                todoDto.getTodoId(),
                userService.findByUserId(todoDto.getWriterId()));
        todoRepository.save(todo);
    }


    public void update(TodoDto todoDto, Long userId){
        Todo todo = DtoConverter.fromTodoDto(todoDto);
        final Optional<Todo> original = todoRepository.findByTodoIdAndTodoDayAndWriter_Id(todoDto.getTodoId(), todoDto.getTodoDay(), userId);

        original.ifPresent(todo1 -> {
            todo1.setTitle(todo.getTitle());
            todo1.setTitleCheck(todo.isTitleCheck());
            todoRepository.save(todo1);
        });
    }

    public void delete(Long userId, Long todoId, LocalDate date){
        try{
            todoRepository.deleteTodoByWriter_IdAndTodoIdAndTodoDay(userId, todoId, date);
        } catch (Exception e){
            log.error("error deleting entity Todo", todoId, e);
            throw new RuntimeException("error deleting entity Todo " + todoId);
        }
    }
}
