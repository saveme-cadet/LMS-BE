package com.larry.fc.finalproject.api.service.aojiservice;

import com.larry.fc.finalproject.core.domain.entity.DayTable;
import com.larry.fc.finalproject.core.domain.entity.StudyTime;
import com.larry.fc.finalproject.core.domain.entity.UserInfo;
import com.larry.fc.finalproject.core.domain.entity.repository.StudyTimeRepository;
import com.larry.fc.finalproject.core.domain.entity.repository.UserInfoRepository;
import com.larry.fc.finalproject.core.domain.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AojiService {
    private final UserService userService;
    private final UserInfoRepository userInfoRepository;
    private final StudyTimeRepository studyTimeRepository;

    public void createAojiTime(Long userId){
        final StudyTime studyTime = StudyTime.studyTimeJoin(
                    userService.findByUserInfoId(userId));
        studyTimeRepository.save(studyTime);
    }

    public void updateAojiTime(Long userId){        // 시간 여러개 일 때

        final Optional<StudyTime> studyTime = studyTimeRepository.findAllByUser_IdAndEndAt(userId, null);
        studyTime.ifPresent(studyTime1 -> {
            studyTime1.setEndAt(LocalDateTime.now());
            studyTimeRepository.save(studyTime1);
        });
    }

    public void deleteAojiTime(Long userId){

    }
}
