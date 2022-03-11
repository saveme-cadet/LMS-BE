package com.larry.fc.finalproject.api.service;

import com.larry.fc.finalproject.api.dto.AuthUser;
import com.larry.fc.finalproject.api.dto.tododto.TodoDto;
import com.larry.fc.finalproject.api.util.DtoConverter;
import com.larry.fc.finalproject.core.domain.entity.Todo;
import com.larry.fc.finalproject.core.domain.entity.repository.TodoRepository;
import com.larry.fc.finalproject.core.domain.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class TodoService {
    private final UserService userService;
    private final TodoRepository todoRepository;

    public void create(TodoDto todoDto, AuthUser authUser){
        final Todo todo = Todo.todoJoin(
                todoDto.getTitle(),
                userService.findByUserId(authUser.getId()));
        todoRepository.save(todo);
    }


    public void update(TodoDto todoDto, Long userId){
        Todo todo = DtoConverter.fromTodoDto(todoDto);
        final Optional<Todo> original = todoRepository.findById(userId)
                .filter(todo1 -> todo1.getId().equals(todoDto.getTodoId()))
                .filter(todo1 -> todo1.getTodoDay().equals(todoDto.getTodoDay()));

        original.ifPresent(todo1 -> {
            todo1.setTitle(todo.getTitle());
            todo1.setTitleCheck(todo.isTitleCheck());
            todoRepository.save(todo1);
        });
    }

    public void delete(Long todoId){
        try{
            todoRepository.deleteById(todoId);
        } catch (Exception e){
            log.error("error deleting entity Todo", todoId, e);
            throw new RuntimeException("error deleting entity Todo " + todoId);
        }
    }
}
