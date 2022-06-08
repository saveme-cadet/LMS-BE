package com.larry.fc.finalproject.api.service.userservice;

import com.larry.fc.finalproject.api.dto.tabledto.AllTableDto;
import com.larry.fc.finalproject.api.util.DtoConverter;
import com.larry.fc.finalproject.api.util.UserDtoConverter;
import com.larry.fc.finalproject.core.domain.entity.DayTable;
import com.larry.fc.finalproject.core.domain.entity.StatisticalChart;
import com.larry.fc.finalproject.core.domain.entity.UserInfo;
import com.larry.fc.finalproject.core.domain.entity.repository.DayTableRepository;
import com.larry.fc.finalproject.core.domain.entity.repository.TodoRepository;
import com.larry.fc.finalproject.core.domain.entity.repository.UserInfoRepository;
import com.larry.fc.finalproject.core.domain.entity.repository.UserStatisticalChartRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class AllUserTableQueryService {
    private final UserInfoRepository userInfoRepository;
    private final DayTableRepository dayTableRepository;
    private final UserStatisticalChartRepository userStatisticalChartRepository;
    private final TodoRepository todoRepository;

    public List<AllTableDto> getDayTableByDay(LocalDate date) throws NullPointerException{
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
        AllTableDto[] allTableDto = new AllTableDto[sumList.length];
        UserInfo[] userInfos = new UserInfo[sumList.length];
        DayTable[] dayTables = new DayTable[sumList.length];
        StatisticalChart[] statisticalCharts = new StatisticalChart[sumList.length];

        for (int i = 0; i < sumList.length; i++){
            UserDayObjectStatic(sumList[i], date);
            UserMonthObjectStatic(sumList[i], date);
            userInfos[i] = userInfoRepository.findByWriter_IdAndAttendeStatus(sumList[i], 1L);
            dayTables[i] = dayTableRepository.findByTableDayAndCadet_IdAndAttendeStatus(date, sumList[i], 1L);
            statisticalCharts[i] = userStatisticalChartRepository.findStatisticalChartByWriter_Id(sumList[i]);
            allTableDto[i] = DtoConverter.fromUserInfoDay(
                    DtoConverter.fromUserDayTable(dayTables[i]),
                    DtoConverter.fromAllUserInfoDto(userInfos[i]),
                    UserDtoConverter.fromAllObject(statisticalCharts[i]));
        }
        List<AllTableDto> allTableDtoList = Arrays.asList(allTableDto);
        return allTableDtoList;
    }

    public void UserDayObjectStatic(Long userId, LocalDate date){

        int count = (int)todoRepository.findAllByWriter_Id(userId).stream()
                .filter(c -> c.getTodoDay().equals(date))
                .count();
        int facount = (int) todoRepository.findAllByWriter_Id(userId).stream()
                .filter(w -> w.getTodoDay().equals(date))
                .filter(i -> i.isTitleCheck())
                .count();

        Double result;

        if (count == 0)
            result = 0.0;
        else
            result = (double) facount / (double) count * 100;

        final Optional<StatisticalChart> original = userStatisticalChartRepository.findAllByWriter_Id(userId);
        original.ifPresent(allUser -> {
            allUser.setDayObjectiveAchievementRate(result);
            userStatisticalChartRepository.save(allUser);
        });
    }

    public void UserMonthObjectStatic(Long userId, LocalDate date){

        int count = (int)todoRepository.findAllByWriter_Id(userId).stream()
                .filter(c -> c.getTodoDay().getMonth().equals(date.getMonth()))
                .count();
        int facount = (int) todoRepository.findAllByWriter_Id(userId).stream()
                .filter(w -> w.getTodoDay().getMonth().equals(date.getMonth()))
                .filter(i -> i.isTitleCheck())
                .count();
        Double result;

        if (count == 0)
            result = 0.0;
        else
            result = (double) facount / (double) count * 100;

        final Optional<StatisticalChart> original = userStatisticalChartRepository.findAllByWriter_Id(userId);
        original.ifPresent(allUser -> {
            allUser.setMonthObjectiveAchievementRate(result);
            userStatisticalChartRepository.save(allUser);
        });
    }

}
