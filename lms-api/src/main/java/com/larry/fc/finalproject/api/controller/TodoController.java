package com.larry.fc.finalproject.api.controller;

import com.larry.fc.finalproject.api.dto.AuthUser;
import com.larry.fc.finalproject.api.dto.TodoDto;
import com.larry.fc.finalproject.api.service.TodoQueryService;
import com.larry.fc.finalproject.api.service.TodoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Tag(name = "오늘 할 일")
@RequiredArgsConstructor
@RequestMapping("/api/todo")
@RestController
public class TodoController {

    private final TodoService todoService;
    private final TodoQueryService todoQueryService;

    @Operation(description = "오늘 할 일 저장")
    @PostMapping("/usertodo")
    public ResponseEntity<Void> createTodo(@Parameter @RequestBody TodoDto todoDto,
                                           @Parameter(name = "userId", description = "user 의 id", in = ParameterIn.QUERY) AuthUser authUser){
        todoService.create(todoDto, authUser);
        return ResponseEntity.ok().build();
    }

    @Operation(description = "오늘 할 일 가져오기")
    @GetMapping("/day")
    public List<TodoDto> getUserInfoByDay(
            @Parameter(name = "userId", description = "user 의 id", in = ParameterIn.QUERY) AuthUser authUser,
            @Parameter(name = "date", description = "date=2022-02-14", in = ParameterIn.QUERY) @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date){
        return todoQueryService.getTodoByDay(authUser, date == null ? LocalDate.now() : date);
    }

    @PutMapping("/modify")
    public ResponseEntity<Void> updateTodo(@Parameter @RequestBody TodoDto todoDto, @Parameter(name = "userId", description = "user 의 id", in = ParameterIn.QUERY) AuthUser authUser){
        todoService.update(todoDto, authUser);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Void> deleteTodo(@Parameter(name ="todoId", description = "삭제할 todoId")Long todoId){
        try{
            todoService.delete(todoId);
            return ResponseEntity.ok().build();
        } catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }

}
