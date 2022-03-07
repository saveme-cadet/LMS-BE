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
    public List<AllTableDto> getDayTableByDay(LocalDate date){
        Stream<Long> idList = userInfoRepository.findAllBy()
                .stream()
                .filter(x -> x.getAttendeStatus().equals(1L))
                .filter(x -> x.getCreatedAt().isBefore(date))
                .map(x -> x.getWriter_Id());
        Long[] list = idList.toArray(Long[]::new);
        for (int i = 0; i < list.length; i++){
            System.out.println(list[i]);
        }
        AllTableDto[] allTableDto = new AllTableDto[list.length];
        UserInfo[] userInfos = new UserInfo[list.length];
        DayTable[] dayTables = new DayTable[list.length];
        for (int i = 0; i < list.length; i++){
            userInfos[i] = userInfoRepository.findByWriter_IdAndAttendeStatus(list[i], 1L);
            dayTables[i] = dayTableRepository.findByTableDayAndCadet_Id(date, list[i]);
            allTableDto[i] = DtoConverter.fromUserInfoDay(
                    DtoConverter.fromUserDayTable(dayTables[i]),
                    DtoConverter.fromAllUserInfoDto(userInfos[i]));
        }
        List<AllTableDto> allTableDtoList = Arrays.asList(allTableDto);
        return allTableDtoList;
    }


}
