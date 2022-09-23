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
import com.savelms.api.user.controller.error.ErrorResult;
import com.savelms.core.user.domain.entity.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import java.time.LocalDate;
import java.util.List;
import javax.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class TodoController {


    @ExceptionHandler
    public ResponseEntity<ErrorResult> entityNotFoundException(EntityNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(ErrorResult.builder()
                .message(e.getMessage())
                .build());
    }

//    @ExceptionHandler
//    public ResponseEntity<ErrorResult> exception(Exception e) {
//        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//            .body(ErrorResult.builder()
//                .message(e.getMessage())
//                .build());
//    }
    private final TodoService todoService;

    @PreAuthorize("hasAuthority('todo.create') OR "
        + "(hasAuthority('user.todo.create') AND @customAuthenticationManager.userIdMatches(authentication, #userId))")
    @Operation(description = "오늘 할 일 저장")
    @PostMapping("/users/{userId}/todos")
    public CreateTodoResponse createTodo(@Validated @Parameter @RequestBody CreateTodoRequest request,
        @Parameter(hidden = true) @AuthenticationPrincipal User user,
        @PathVariable("userId") String userId) {
        Long todoId = todoService.create(request, userId);
        return CreateTodoResponse.builder()
            .todoId(todoId)
            .build();
    }

    @PreAuthorize("hasAuthority('todo.read') OR "
        + "hasAuthority('user.todo.read')")
    @Operation(description = "오늘 내 할 일 가져오기")
    @GetMapping("/users/{userId}/todos")
    public ListResponse<GetMyTodosByDayResponse> getMyTodosByToday(@PathVariable("userId") String userId,
        @Parameter(hidden = true) @AuthenticationPrincipal User user,
        @Parameter(name = "date", description = "date=2022-02-11", in = ParameterIn.QUERY)
        @RequestParam(required = false)
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {

        List<GetMyTodosByDayResponse> todoDtos = todoService.getMyAllTodoInToday(
            userId,
            date == null ? LocalDate.now() : date);

        ListResponse<GetMyTodosByDayResponse> response = new ListResponse<>();
        response.setCount(todoDtos.size());
        response.setContent(todoDtos);
        return response;
    }

    @PreAuthorize("hasAuthority('todo.read')")
    @Operation(description = "오늘 할 일 모든 유저 가져오기")
    @GetMapping("/users/todos")
    public ListResponse<AllUserTodoDto> getUserTodoAllByDay(
        @Parameter(name = "date", description = "date=2022-02-11", in = ParameterIn.QUERY)
        @RequestParam(required = false)
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {

        return todoService.getTodoAllByDay(date == null ? LocalDate.now() : date);
    }

//    @PreAuthorize("hasAuthority('todo.read')")
//    @Operation(description = "오늘 할 일 모든 유저 진척률")
//    @GetMapping("/users/todos/progress")
//    public ListResponse<GetTodoProgressResponse> getUserTodoProgress(
//        @Parameter(name = "date", description = "date=2022-02-11", in = ParameterIn.QUERY)
//        @RequestParam(required = false)
//        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
//
//        return todoService.getTodoProgress(date == null ? LocalDate.now() : date);
//    }


    @PreAuthorize("hasAuthority('todo.update') OR "
        + "(hasAuthority('user.todo.update') AND @customAuthenticationManager.userIdMatches(authentication, #userId))")
    @PatchMapping("/users/{userId}/todos/{todoId}")
    public ResponseEntity<UpdateTodoResponse> updateTodo(@Validated @Parameter @RequestBody UpdateTodoRequest request,
        @PathVariable("userId") String userId,
        @PathVariable("todoId") Long todoId){
        Long id = todoService.update(request, todoId, userId);

        UpdateTodoResponse responseBody = UpdateTodoResponse.builder()
            .todoId(id)
            .build();
        return new ResponseEntity<>(responseBody, HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('todo.delete') OR "
        + "(hasAuthority('user.todo.delete') AND @customAuthenticationManager.userIdMatches(authentication, #userId))")
    @DeleteMapping("/users/{userId}/todos/{todoId}")
    public ResponseEntity<DeleteTodoResponse> deleteTodo(@PathVariable("userId") String userId,
        @PathVariable(name = "todoId") Long todoId,
        @Parameter(hidden = true) @AuthenticationPrincipal User user) {

            Long id = todoService.delete(todoId, userId);
            DeleteTodoResponse responseBody = DeleteTodoResponse.builder()
                .todoId(id)
                .build();
        return new ResponseEntity<>(responseBody, HttpStatus.OK);
    }

}
