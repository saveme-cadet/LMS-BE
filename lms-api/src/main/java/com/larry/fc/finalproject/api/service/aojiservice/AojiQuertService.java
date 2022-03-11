package com.larry.fc.finalproject.api.service.aojiservice;

import com.larry.fc.finalproject.api.dto.aojidto.AojiResponseDto;
import com.larry.fc.finalproject.api.dto.userinfodto.UserInfoMonthDto;
import com.larry.fc.finalproject.api.util.AojiDtoConverter;
import com.larry.fc.finalproject.api.util.DtoConverter;
import com.larry.fc.finalproject.core.domain.entity.repository.StudyTimeRepository;
import com.larry.fc.finalproject.core.domain.entity.repository.UserInfoRepository;
import com.larry.fc.finalproject.core.domain.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class AojiQuertService {
    private final StudyTimeRepository studyTimeRepository;

    public List<AojiResponseDto> getAojiTime(Long userId) throws NullPointerException{
        final Stream<AojiResponseDto> userAoji = studyTimeRepository.findStudyTimeByUser_Id(userId)
                .stream()
                .map(userAoji1 -> AojiDtoConverter.fromStudyTime(userAoji1));
        final List<AojiResponseDto> responseDtos = userAoji.collect(Collectors.toList());
        return responseDtos;
    }
}
