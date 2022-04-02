package com.larry.fc.finalproject.api.service.todoservice;

import com.larry.fc.finalproject.api.dto.AuthUser;
import com.larry.fc.finalproject.api.dto.tabledto.AllTableDto;
import com.larry.fc.finalproject.api.dto.tododto.AllUserTodoDto;
import com.larry.fc.finalproject.api.dto.tododto.TodoDto;
import com.larry.fc.finalproject.api.util.DtoConverter;
import com.larry.fc.finalproject.api.util.UserDtoConverter;
import com.larry.fc.finalproject.core.domain.entity.DayTable;
import com.larry.fc.finalproject.core.domain.entity.StatisticalChart;
import com.larry.fc.finalproject.core.domain.entity.Todo;
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
    private final DayTableRepository dayTableRepository;

    public List<TodoDto> getTodoByDay(Long userId, LocalDate date) {

            final Stream<TodoDto> todo = todoRepository.findAllByWriter_Id(userId)
                    .stream()
                    .filter(todo1 -> todo1.getTodoDay().equals(date))
                    .map(todo1 -> DtoConverter.fromTodo(todo1));
            final List<TodoDto> response = todo.collect(Collectors.toList());
            return response;

    }

    public List<AllUserTodoDto> getTodoAllByDay(LocalDate date) throws NullPointerException{
            Stream<Long> idList = userInfoRepository.findAllBy()
                    .stream()
                    .filter(x -> dayTableRepository.existsByTableDayAndCadet_Id(date, x.getWriter_Id()))
                    .filter(x -> x.getAttendeStatus().equals(1L))
                    .filter(x -> x.getCreatedAt().isBefore(date))
                    .map(x -> x.getWriter_Id());
            Stream<Long> idList1 = userInfoRepository.findAllBy()
                    .stream()
                    .filter(x -> dayTableRepository.existsByTableDayAndCadet_Id(date, x.getWriter_Id()))
                    .filter(x -> x.getAttendeStatus().equals(1L))
                    .filter(x -> x.getCreatedAt().isEqual(date))
                    .map(x -> x.getWriter_Id());
            Long[] list = idList.toArray(Long[]::new);
            Long[] list1 = idList1.toArray(Long[]::new);
            Long[] sumList = Stream.of(list, list1).flatMap(Stream::of).toArray(Long[]::new);
            for (int i = 0; i < sumList.length; i++){
                System.out.println(sumList[i]);
            }
            AllUserTodoDto[] allUserTodoDtos = new AllUserTodoDto[sumList.length];
            UserInfo[] userInfos = new UserInfo[sumList.length];

            for (int i = 0; i < sumList.length; i++){

                userInfos[i] = userInfoRepository.findByWriter_IdAndAttendeStatus(sumList[i], 1L);
                List<TodoDto> todoDtoList = getTodoByDay(sumList[i], date);
                allUserTodoDtos[i] = DtoConverter.allUserTodoDtoFrom(
                        userInfos[i],
                        todoDtoList
                );
            }
            List<AllUserTodoDto> allTableDtoList = Arrays.asList(allUserTodoDtos);
            return allTableDtoList;
    }
}
