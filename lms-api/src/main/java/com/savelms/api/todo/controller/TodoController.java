package com.savelms.api.todo.controller;

import com.savelms.api.todo.controller.dto.AllUserTodoDto;
import com.savelms.api.todo.controller.dto.CreateTodoRequest;
import com.savelms.api.todo.controller.dto.CreateTodoResponse;
import com.savelms.api.todo.controller.dto.DeleteTodoResponse;
import com.savelms.api.todo.controller.dto.GetMyTodosByDayResponse;
import com.savelms.api.todo.controller.dto.GetTodoProgressResponse;
import com.savelms.api.todo.controller.dto.ListResponse;
import com.savelms.api.todo.controller.dto.UpdateTodoRequest;
import com.savelms.api.todo.controller.dto.UpdateTodoResponse;
import com.savelms.api.todo.service.TodoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(originPatterns = "http://3.38.226.166:8080")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class TodoController {

    private final TodoService todoService;

    @Operation(description = "오늘 할 일 저장")
    @PostMapping("/todos")
    public CreateTodoResponse createTodo(@Validated @Parameter @RequestBody CreateTodoRequest request,
        @AuthenticationPrincipal User user) {
        Long todoId = todoService.create(request, user.getUsername());
        return CreateTodoResponse.builder()
            .todoId(todoId)
            .build();
    }

    @Operation(description = "오늘 내 할 일 가져오기")
    @GetMapping("/todos")
    public ListResponse<GetMyTodosByDayResponse> getMyTodosByToday(@AuthenticationPrincipal User user,
        @Parameter(name = "date", description = "date=2022-02-11", in = ParameterIn.QUERY)
        @RequestParam(required = false)
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {

        List<GetMyTodosByDayResponse> todoDtos = todoService.getMyAllTodoInToday(
            user.getUsername(),
            date == null ? LocalDate.now() : date);

        ListResponse<GetMyTodosByDayResponse> response = new ListResponse<>();
        response.setCount(todoDtos.size());
        response.setContent(todoDtos);
        return response;
    }

    @Operation(description = "오늘 할 일 모든 유저 가져오기")
    @GetMapping("/users/todos")
    public ListResponse<AllUserTodoDto> getUserTodoAllByDay(
        @Parameter(name = "date", description = "date=2022-02-11", in = ParameterIn.QUERY)
        @RequestParam(required = false)
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {

        return todoService.getTodoAllByDay(date == null ? LocalDate.now() : date);
    }

    @Operation(description = "오늘 할 일 모든 유저 진척률")
    @GetMapping("/users/todos/progress")
    public ListResponse<GetTodoProgressResponse> getUserTodoProgress(
        @Parameter(name = "date", description = "date=2022-02-11", in = ParameterIn.QUERY)
        @RequestParam(required = false)
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {

        return todoService.getTodoProgress(date == null ? LocalDate.now() : date);
    }



    @PatchMapping("/todos/{id}")
    public ResponseEntity<UpdateTodoResponse> updateTodo(@Validated @Parameter @RequestBody UpdateTodoRequest request,
        @PathVariable("id") Long todoId){
        Long id = todoService.update(request, todoId);

        UpdateTodoResponse responseBody = UpdateTodoResponse.builder()
            .todoId(id)
            .build();
        return new ResponseEntity<>(responseBody, HttpStatus.OK);
    }

    @DeleteMapping("/todos/{id}")
    public ResponseEntity<DeleteTodoResponse> deleteTodo(@PathVariable(name = "id") Long todoId,
        @AuthenticationPrincipal User user) {

        try {
            Long id = todoService.delete(todoId, user.getUsername());
            DeleteTodoResponse responseBody = DeleteTodoResponse.builder()
                .todoId(id)
                .build();
            return new ResponseEntity<>(responseBody, HttpStatus.OK);
        } catch (RuntimeException re) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

}
