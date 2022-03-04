package com.larry.fc.finalproject.api.service.userservice;

import com.larry.fc.finalproject.api.dto.AuthUser;
import com.larry.fc.finalproject.api.dto.userdto.UserDto;
import com.larry.fc.finalproject.api.dto.userinfodto.UserInfoDto;
import com.larry.fc.finalproject.api.util.DtoConverter;
import com.larry.fc.finalproject.api.util.UserDtoConverter;
import com.larry.fc.finalproject.core.domain.entity.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class UserQueryService {
    private final UserRepository userRepository;
    public List<UserDto> getUser(){
        final Stream<UserDto> userInfo = userRepository.findByAttendStatus(1L) // wirter 로 수정
                .stream()
                .map(userInfo1 -> UserDtoConverter.fromUser(userInfo1));
        final List<UserDto> response = userInfo.collect(Collectors.toList());
        return response;
    }
}
