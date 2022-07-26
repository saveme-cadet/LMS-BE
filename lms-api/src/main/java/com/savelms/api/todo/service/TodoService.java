package com.savelms.api.todo.service;

import com.savelms.api.calendar.service.CalendarService;
import com.savelms.api.statistical.service.DayStatisticalDataService;
import com.savelms.api.todo.controller.dto.AllUserTodoDto;
import com.savelms.api.todo.controller.dto.AllUserTodoSingleTodoDto;
import com.savelms.api.todo.controller.dto.CreateTodoRequest;
import com.savelms.api.todo.controller.dto.GetMyTodosByDayResponse;
import com.savelms.api.todo.controller.dto.GetTodoProgressResponse;
import com.savelms.api.todo.controller.dto.ListResponse;
import com.savelms.api.todo.controller.dto.UpdateTodoRequest;
import com.savelms.api.user.service.UserService;
import com.savelms.core.calendar.domain.entity.Calendar;
import com.savelms.core.todo.domain.entity.Todo;
import com.savelms.core.todo.domain.repository.TodoRepository;
import com.savelms.core.user.domain.entity.User;
import com.savelms.core.user.domain.repository.UserRepository;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class TodoService {

    private final UserRepository userRepository;
    private final UserService userService;

    private final CalendarService calendarService;
    private final TodoRepository todoRepository;

    private final DayStatisticalDataService statisticalDataService;

    @Transactional
    public Long create(CreateTodoRequest request, String apiId) {

        User user = userRepository.findByApiId(apiId)
            .orElseThrow(() ->
                new EntityNotFoundException("User not found"));
        Calendar calendar = calendarService.findByDate(request.getTodoDay());

        Todo todo = Todo.createTodo(false, request.getTitle(), user, calendar);
        todoRepository.save(todo);
        return todo.getId();
    }

    public Todo findById(Long id) {
        return todoRepository.findById(id).orElseThrow(() ->
            new EntityNotFoundException("Todo not found by id: " + id)
            );

    }

    public List<GetMyTodosByDayResponse> getMyAllTodoInToday(String userId, LocalDate localDate) {
        List<Todo> todos = todoRepository.findByApiIdAndTodoDayFetchJoin(userId,
            localDate);
        return todos.stream()
            .map(todo ->
                GetMyTodosByDayResponse.builder()
                    .writerId(todo.getUser().getApiId())
                    .todoId(todo.getId())
                    .title(todo.getTitle())
                    .titleCheck(todo.getComplete())
                    .todoDay(todo.getCalendar().getDate())
                    .build())
            .collect(Collectors.toList());
    }

    public ListResponse<AllUserTodoDto> getTodoAllByDay(LocalDate localDate) {
        List<Todo> todos = todoRepository.findByTodoDayFetchJoin(localDate);
        List<User> users = userRepository.findAll();
        ListResponse<AllUserTodoDto> response = new ListResponse<>();

        for (User user : users) {
            List<AllUserTodoSingleTodoDto> todoDtos = todos.stream()
                .filter((todo) ->
                    todo.getUser().getId().equals(user.getId()))
                .map((todo) ->
                    AllUserTodoSingleTodoDto.builder()
                        .writerId(todo.getUser().getApiId())
                        .todoId(todo.getId())
                        .title(todo.getTitle())
                        .titleCheck(todo.getComplete())
                        .todoDay(todo.getCalendar().getDate())
                        .build())
                .collect(Collectors.toList());
            if (todoDtos.size() == 0){
                continue;
            }
            AllUserTodoDto userDto = AllUserTodoDto.builder()
                .writerId(user.getApiId())
                .username(user.getUsername())
                .todoDtoList(todoDtos)
                .build();
            response.getContent().add(userDto);
        }
        response.setCount(response.getContent().size());
        return response;
    }

    public ListResponse<GetTodoProgressResponse> getTodoProgress(LocalDate localDate) {

        List<Todo> todos = todoRepository.findByTodoDayFetchJoin(localDate);
        ListResponse<GetTodoProgressResponse> response = new ListResponse<>();
        Map<String, Integer[]> data = new HashMap<>(40);

        todos.forEach((todo) -> {
            String apiId = todo.getUser().getApiId();
            if (data.containsKey(apiId) == false) {
                data.put(apiId, new Integer[]{0, 0});
            }
            ++data.get(apiId)[0];
            if (todo.getComplete() == true) {
                ++data.get(apiId)[1];
            }
        });
        data.forEach((key, value) -> {
            GetTodoProgressResponse dto = GetTodoProgressResponse.builder()
                .writerId(key)
                .progress((double)value[1]/ value[0])
                .build();
            response.getContent().add(dto);

            statisticalDataService.updateTodoSuccessRate(dto.getWriterId(), dto.getProgress(), localDate);
        });
        response.setCount(response.getContent().size());

        return response;

    }

    @Transactional
    public Long update(UpdateTodoRequest request, Long todoId, String userId) {
        Todo todo = todoRepository.findById(todoId)
            .orElseThrow(() ->
                new EntityNotFoundException("Todo not found by id: " + todoId));
        if (todo.getUser().getApiId().equals(userId) == false) {
            throw new IllegalArgumentException("Todo "
                + todoId
                + " not found by userId: " + todoId);
        }
            return todo.changeTitleAndComplete(request.getTitle(), request.getTitleCheck());
    }

    @Transactional
    public Long delete(Long todoId, String userId) {


        Todo todo = todoRepository.findById(todoId)
            .orElseThrow(() ->
                new EntityNotFoundException("Todo not found by id: " + todoId));
        if (todo.getUser().getApiId().equals(userId) == false) {
            throw new IllegalArgumentException("Todo "
                + todoId
                + " not found by userId: " + todoId);
        }
        todoRepository.deleteById(todoId);
        return todo.getId();
    }


}
