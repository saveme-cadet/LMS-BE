package com.larry.fc.finalproject.api.service.userservice;

import com.larry.fc.finalproject.api.dto.AllUserDto;
import com.larry.fc.finalproject.api.dto.tabledto.AllTableDto;
import com.larry.fc.finalproject.api.util.DtoConverter;
import com.larry.fc.finalproject.core.domain.entity.DayTable;
import com.larry.fc.finalproject.core.domain.entity.User;
import com.larry.fc.finalproject.core.domain.entity.UserInfo;
import com.larry.fc.finalproject.core.domain.entity.repository.DayTableRepository;
import com.larry.fc.finalproject.core.domain.entity.repository.UserInfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class AllUserTableQueryService {
    private final UserInfoRepository userInfoRepository;
    private final DayTableRepository dayTableRepository;
    public List<AllTableDto> getDayTableByDay(LocalDate date) throws NullPointerException{
        Stream<Long> idList = userInfoRepository.findAllBy()
                .stream()
                .filter(x -> x.getAttendeStatus().equals(1L))
                .filter(x -> x.getCreatedAt().isBefore(date))
                .map(x -> x.getWriter_Id());
        Stream<Long> idList1 = userInfoRepository.findAllBy()
                .stream()
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
        for (int i = 0; i < sumList.length; i++){
            userInfos[i] = userInfoRepository.findByWriter_IdAndAttendeStatus(sumList[i], 1L);
            dayTables[i] = dayTableRepository.findByTableDayAndCadet_IdAndAttendeStatus(date, sumList[i], 1L);
            allTableDto[i] = DtoConverter.fromUserInfoDay(
                    DtoConverter.fromUserDayTable(dayTables[i]),
                    DtoConverter.fromAllUserInfoDto(userInfos[i]));
        }
        List<AllTableDto> allTableDtoList = Arrays.asList(allTableDto);
        return allTableDtoList;
    }


}
