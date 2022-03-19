package com.larry.fc.finalproject.api.controller;

import com.larry.fc.finalproject.api.dto.AuthUser;
import com.larry.fc.finalproject.api.dto.tododto.AllUserTodoDto;
import com.larry.fc.finalproject.api.dto.tododto.DeleteTodoDto;
import com.larry.fc.finalproject.api.dto.tododto.RequestTodoDto;
import com.larry.fc.finalproject.api.dto.tododto.TodoDto;
import com.larry.fc.finalproject.api.service.TodoQueryService;
import com.larry.fc.finalproject.api.service.TodoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
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
    public ResponseEntity<Void> createTodo(@Validated @Parameter @RequestBody TodoDto todoDto){
        todoService.create(todoDto);
        return ResponseEntity.ok().build();
    }



    @Operation(description = "오늘 할 일 가져오기")
    @GetMapping("/day/{userId}")
    public List<TodoDto> getUserInfoByDay(@PathVariable(name = "userId") Integer userId, @Parameter(name = "date", description = "date=2022-02-11", in = ParameterIn.QUERY) @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date){
        return todoQueryService.getTodoByDay(userId.longValue(),
                date == null ? LocalDate.now() : date);
    }

    @PutMapping("/todo")
    public ResponseEntity<Void> updateTodo(@Validated @Parameter @RequestBody TodoDto todoDto){
        todoService.update(todoDto, todoDto.getWriterId());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Void> deleteTodo(@PathVariable(name = "userId") Integer userId, @PathVariable(name = "todoId") Integer todoId, @Parameter(name = "date", description = "date=2022-02-11", in = ParameterIn.QUERY) @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date){

        try{
            todoService.delete(userId.longValue(), todoId.longValue(), date);
            return ResponseEntity.ok().build();
        } catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }
}
