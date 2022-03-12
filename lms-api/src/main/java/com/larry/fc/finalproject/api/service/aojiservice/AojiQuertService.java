package com.larry.fc.finalproject.api.service.aojiservice;

import com.larry.fc.finalproject.api.dto.aojidto.AojiResponseDto;
import com.larry.fc.finalproject.api.dto.aojidto.AojiUserDto;
import com.larry.fc.finalproject.api.dto.userinfodto.UserInfoMonthDto;
import com.larry.fc.finalproject.api.util.AojiDtoConverter;
import com.larry.fc.finalproject.api.util.DtoConverter;
import com.larry.fc.finalproject.core.domain.entity.UserInfo;
import com.larry.fc.finalproject.core.domain.entity.repository.StudyTimeRepository;
import com.larry.fc.finalproject.core.domain.entity.repository.UserInfoRepository;
import com.larry.fc.finalproject.core.domain.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class AojiQuertService {
    private final StudyTimeRepository studyTimeRepository;
    private final UserInfoRepository userInfoRepository;

    public List<AojiResponseDto> getAojiTime(Long userId) throws NullPointerException{
        final Stream<AojiResponseDto> userAoji = studyTimeRepository.findStudyTimeByUser_Id(userId)
                .stream()
                .map(userAoji1 -> AojiDtoConverter.fromStudyTime(userAoji1));
        final List<AojiResponseDto> responseDtos = userAoji.collect(Collectors.toList());
        return responseDtos;
    }

    public List<AojiUserDto> getAojiUser() throws NullPointerException{
        final Stream<Long> userAoji = studyTimeRepository.findAllByEndAt(null)
                .stream()
                .filter(x-> userInfoRepository.existsUserInfoByAttendeStatusAndWriter_Id(1L, x.getUserId()))
                .map(x -> x.getUserId());
        Long []list = userAoji.toArray(Long[]::new);
        UserInfo[] userInfos = new UserInfo[list.length];
        AojiUserDto[] aojiUserDtos = new AojiUserDto[list.length];
        for (int i = 0; i < list.length; i++){
            userInfos[i] = userInfoRepository.findByWriter_IdAndAttendeStatus(list[i], 1L);
            aojiUserDtos[i] = DtoConverter.aojiUserDtofromUserInfo(userInfos[i]);
        }
        List<AojiUserDto> aojiUserDtoList = Arrays.asList(aojiUserDtos);
        return aojiUserDtoList;
    }
}
