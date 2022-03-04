package com.larry.fc.finalproject.api.service;

import com.larry.fc.finalproject.api.dto.AuthUser;
import com.larry.fc.finalproject.api.dto.userstatisticalchartdto.AttendDto;
import com.larry.fc.finalproject.api.dto.userstatisticalchartdto.ObjectDto;
import com.larry.fc.finalproject.api.util.UserDtoConverter;
import com.larry.fc.finalproject.core.domain.entity.StatisticalChart;
import com.larry.fc.finalproject.core.domain.entity.repository.DayTableRepository;
import com.larry.fc.finalproject.core.domain.entity.repository.TodoRepository;
import com.larry.fc.finalproject.core.domain.entity.repository.UserStatisticalChartRepository;
import com.larry.fc.finalproject.core.domain.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserStatisticalChartService {
    private final UserStatisticalChartRepository userStatisticalChartRepository;
    private final UserService userService;
    private final TodoRepository todoRepository;
    private final DayTableRepository dayTableRepository;

    public List<AttendDto> getUserAttendStatic(Long userId){
        UserMonthatttendStatic(userId);
        final Stream<AttendDto> userAttend = userStatisticalChartRepository.findByWriter_Id(userId) // wirter 로 수정
                .stream()
                .map(userInfo1 -> UserDtoConverter.fromAttend(userInfo1));
        final List<AttendDto> response = userAttend.collect(Collectors.toList());
        return response;
    }

    public List<ObjectDto> getUserObjectStatic(Long userId){
        UserDayObjectStatic(userId);
        UserMonthObjectStatic(userId);
        final Stream<ObjectDto> userObject = userStatisticalChartRepository.findByWriter_Id(userId) // wirter 로 수정
                .stream()
                .map(userInfo1 -> UserDtoConverter.fromObject(userInfo1));
        final List<ObjectDto> response = userObject.collect(Collectors.toList());
        return response;
    }

    public void create(AuthUser authUser) {
        final StatisticalChart statisticalChart = StatisticalChart.userStaticChartJoin(
                userService.findByUserId(authUser.getId()));
        userStatisticalChartRepository.save(statisticalChart);
    }

    public void UserDayObjectStatic(Long userId){

        int count = (int)todoRepository.findAllByWriter_Id(userId).stream()
                .filter(c -> c.getTodoDay().equals(LocalDate.now()))
                .count();
        int facount = (int) todoRepository.findAllByWriter_Id(userId).stream()
                .filter(w -> w.getTodoDay().equals(LocalDate.now()))
                .filter(i -> i.isTitleCheck())
                .count();
        Double result;

        result = (double) facount / (double) count * 100;

        final Optional<StatisticalChart> original = userStatisticalChartRepository.findAllByWriter_Id(userId);
        original.ifPresent(allUser -> {
            allUser.setDayObjectiveAchievementRate(result);
            userStatisticalChartRepository.save(allUser);
        });
    }

    public void UserMonthObjectStatic(Long userId){

        int count = (int)todoRepository.findAllByWriter_Id(userId).stream()
                .filter(c -> c.getTodoDay().getMonth().equals(LocalDate.now().getMonth()))
                .count();
        int facount = (int) todoRepository.findAllByWriter_Id(userId).stream()
                .filter(w -> w.getTodoDay().getMonth().equals(LocalDate.now().getMonth()))
                .filter(i -> i.isTitleCheck())
                .count();
        Double result;

        result = (double) facount / (double) count * 100;

        final Optional<StatisticalChart> original = userStatisticalChartRepository.findAllByWriter_Id(userId);
        original.ifPresent(allUser -> {
            allUser.setMonthObjectiveAchievementRate(result);
            userStatisticalChartRepository.save(allUser);
        });
    }

    public void UserMonthatttendStatic(Long userId){

        int count = (int)dayTableRepository.findAllByCadet_Id(userId).stream()
                .filter(c -> c.getTableDay().getMonth().equals(LocalDate.now().getMonth()))
                .count();
        int failCheckIn = (int) dayTableRepository.findAllByCadet_Id(userId).stream()
                .filter(w -> w.getTableDay().getMonth().equals(LocalDate.now().getMonth()))
                .filter(i -> i.getCheckIn() == 3)
                .count();
        int failCheckOut = (int) dayTableRepository.findAllByCadet_Id(userId).stream()
                .filter(w -> w.getTableDay().getMonth().equals(LocalDate.now().getMonth()))
                .filter(i -> i.getCheckOut() == 3)
                .count();

        Double result;

        result = 100 - (((double) failCheckIn + (double) failCheckOut) / ((double) count * 2 ) * 100);
        final Optional<StatisticalChart> original = userStatisticalChartRepository.findAllByWriter_Id(userId);
        original.ifPresent(allUser -> {
            allUser.setMonthAttendanceRate(result);
            userStatisticalChartRepository.save(allUser);
        });
    }


}
