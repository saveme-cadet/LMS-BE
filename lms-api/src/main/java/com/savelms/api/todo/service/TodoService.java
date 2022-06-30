package com.savelms.api.todo.service;

import com.savelms.api.calendar.service.CalendarService;
import com.savelms.api.todo.controller.createTodoRequest;
import com.savelms.core.calendar.domain.entity.Calendar;
import com.savelms.core.calendar.domain.repository.CalendarRepository;
import com.savelms.core.todo.domain.entity.Todo;
import com.savelms.core.todo.domain.repository.TodoRepository;
import javax.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class TodoService {

    private final CalendarService calendarService;
    private final TodoRepository todoRepository;
    public Todo create(createTodoRequest request) {

        Calendar calendar = calendarService.findByDate(request.getTodoDay());

        Todo.builder()
            .title(request.getTitle())
            .calendar(calendar)
            .complete(false);
        return new Todo();
    }

    public Todo findById(Long id) {
        return todoRepository.findById(id).orElseThrow(() ->
            new EntityNotFoundException("Todo not found by id: " + id)
            );

    }

}
