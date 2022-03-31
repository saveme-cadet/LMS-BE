package com.larry.fc.finalproject.api.service.todoservice;

import com.larry.fc.finalproject.api.dto.AuthUser;
import com.larry.fc.finalproject.api.dto.tabledto.AllTableDto;
import com.larry.fc.finalproject.api.dto.tododto.AllUserTodoDto;
import com.larry.fc.finalproject.api.dto.tododto.TodoDto;
import com.larry.fc.finalproject.api.util.DtoConverter;
import com.larry.fc.finalproject.core.domain.entity.DayTable;
import com.larry.fc.finalproject.core.domain.entity.UserInfo;
import com.larry.fc.finalproject.core.domain.entity.repository.DayTableRepository;
import com.larry.fc.finalproject.core.domain.entity.repository.TodoRepository;
import com.larry.fc.finalproject.core.domain.entity.repository.UserInfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class TodoQueryService {
    private final TodoRepository todoRepository;
    private final UserInfoRepository userInfoRepository;

    public List<TodoDto> getTodoByDay(Long userId, LocalDate date) {

            final Stream<TodoDto> todo = todoRepository.findAllByWriter_Id(userId)
                    .stream()
                    .filter(todo1 -> todo1.getTodoDay().equals(date))
                    .map(todo1 -> DtoConverter.fromTodo(todo1));
            final List<TodoDto> response = todo.collect(Collectors.toList());
            return response;

    }
}