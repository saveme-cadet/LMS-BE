package com.larry.fc.finalproject.api.service.userservice;

import com.larry.fc.finalproject.api.dto.AuthUser;
import com.larry.fc.finalproject.api.dto.userinfodto.AllUserInfoDto;
import com.larry.fc.finalproject.api.dto.userinfodto.UserInfoDto;
import com.larry.fc.finalproject.api.dto.userinfodto.UserInfoMonthDto;
import com.larry.fc.finalproject.api.dto.userinfodto.UserInfoWeekDto;
import com.larry.fc.finalproject.api.util.DtoConverter;
import com.larry.fc.finalproject.core.domain.entity.repository.UserInfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class UserInfoQueryService {
    private final UserInfoRepository userInfoRepository;

    public List<UserInfoDto> getUserInfoByDay(AuthUser authUser){
       final Stream<UserInfoDto> userInfo = userInfoRepository.findAllByWriter_Id(authUser.getId()) // wirter 로 수정
               .stream()
               .map(userInfo1 -> DtoConverter.fromUserInfo(userInfo1));
       final List<UserInfoDto> response = userInfo.collect(Collectors.toList());
       return response;
    }

    public List<UserInfoWeekDto> getUserInfoWeek(AuthUser authUser){
        final Stream<UserInfoWeekDto> userInfo = userInfoRepository.findAllByWriter_Id(authUser.getId()) // wirter 로 수정
                .stream()
                .map(userInfo1 -> DtoConverter.fromUserInfoWeek(userInfo1));
        final List<UserInfoWeekDto> response = userInfo.collect(Collectors.toList());
        return response;
    }

    public List<UserInfoMonthDto> getUserInfoMonth(AuthUser authUser){
        final Stream<UserInfoMonthDto> userInfo = userInfoRepository.findAllByWriter_Id(authUser.getId()) // wirter 로 수정
                .stream()
                .map(userInfo1 -> DtoConverter.fromUserInfoMonth(userInfo1));
        final List<UserInfoMonthDto> response = userInfo.collect(Collectors.toList());
        return response;
    }

    public List<UserInfoDto> getAllUserInfo() {
        final Stream<UserInfoDto> userInfo = userInfoRepository.findAll() // wirter 로 수정
                .stream()
                .map(userInfo1 -> DtoConverter.fromUserInfo(userInfo1));
        final List<UserInfoDto> response = userInfo.collect(Collectors.toList());
        return response;
    }

    public List<UserInfoDto> getUserInfo(Long userId) {
        final Stream<UserInfoDto> userInfo = userInfoRepository.findAllByWriter_Id(userId) // wirter 로 수정
                .stream()
                .map(userInfo1 -> DtoConverter.fromUserInfo(userInfo1));
        final List<UserInfoDto> response = userInfo.collect(Collectors.toList());
        return response;
    }
}
