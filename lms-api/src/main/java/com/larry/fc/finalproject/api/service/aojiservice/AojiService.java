package com.larry.fc.finalproject.api.service.aojiservice;

import com.larry.fc.finalproject.api.dto.aojidto.AojiDto;
import com.larry.fc.finalproject.api.dto.aojidto.AojiResponseDto;
import com.larry.fc.finalproject.api.util.AojiDtoConverter;
import com.larry.fc.finalproject.api.util.UserDtoConverter;
import com.larry.fc.finalproject.core.domain.entity.DayTable;
import com.larry.fc.finalproject.core.domain.entity.StudyTime;
import com.larry.fc.finalproject.core.domain.entity.UserInfo;
import com.larry.fc.finalproject.core.domain.entity.repository.DayTableRepository;
import com.larry.fc.finalproject.core.domain.entity.repository.StudyTimeRepository;
import com.larry.fc.finalproject.core.domain.entity.repository.UserInfoRepository;
import com.larry.fc.finalproject.core.domain.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.NonUniqueResultException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@Slf4j
@Service
@RequiredArgsConstructor
public class AojiService {
    private final UserService userService;
    private final UserInfoRepository userInfoRepository;
    private final StudyTimeRepository studyTimeRepository;
    private final DayTableRepository dayTableRepository;

    public void createAojiTime(Long userId){
        Long id = studyTimeRepository.countAllByUser_Id(userId);
        final StudyTime studyTime = StudyTime.studyTimeJoin(
                    userService.findByUserId(userId), id + 1);
        studyTimeRepository.save(studyTime);
    }

    public void updateAojiTime(Long userId) {
        StudyTime studyTime1;
        try {
            StudyTime studyTime = studyTimeRepository.findByUser_IdAndCh(userId, 0L);
            studyTime.setEndAt(LocalDateTime.now());
            studyTime.setCh(1L);
            studyTimeRepository.save(studyTime);
            calcAttendScore(userId);
        } catch (Exception e){
            System.out.println(e);
        }
    }

    public void updateAllAojiTime(Long userId, AojiDto aojiDto){
        StudyTime studyTimeConvert = AojiDtoConverter.fromAojiDto(aojiDto);
        final Optional<StudyTime> studyTime = studyTimeRepository.findAllByUser_IdAndAojiTimeIndex(userId, aojiDto.getAojiTimeIndex());
        studyTime.ifPresent(studyTime1 -> {
            studyTime1.setStartAt(studyTimeConvert.getStartAt());
            studyTime1.setEndAt(studyTimeConvert.getEndAt());
            studyTimeRepository.save(studyTime1);
        });
    }

    public void deleteAojiTime(Long userId, Long aojiId){
        try{
            studyTimeRepository.deleteByAojiTimeIndexAndUser_Id(aojiId, userId);
        } catch (Exception e){
            log.error("error deleting entity Todo", userId, e);
            throw new RuntimeException("error deleting entity Todo " + userId);
        }
    }

    public void calcAttendScore(Long userId){
        Long id = studyTimeRepository.countAllByUser_Id(userId);
        for (Long i = 1L; i <= id; i++){
            StudyTime studyTime = studyTimeRepository.findStudyTimeByAojiTimeIndexAndUser_Id(i, userId);
            if (ChronoUnit.SECONDS.between(studyTime.getStartAt() , studyTime.getEndAt() ) != 0){
                Long time = Duration.between(studyTime.getStartAt(), studyTime.getEndAt()).getSeconds();
                Double timeDouble = time.doubleValue();
                Double aojiScore = timeDouble  / 28800;
                UserInfo changeUserInfo = UserDtoConverter.fromUserInfoInAttendScore(Math.round(aojiScore*100)/100.0);
                final Optional<UserInfo> userInfo = userInfoRepository.findByWriter_Id(userId);
                userInfo.ifPresent(userInfo1 -> {
                    userInfo1.setAttendScore(userInfo1.getAttendScore() - changeUserInfo.getAttendScore());
                    userInfoRepository.save(userInfo1);
                });
            }
        }

        Double result = userInfoRepository.findByWriter_IdAndAttendeStatus(userId, 1L).getAttendScore();
        LocalDate nowDay = studyTimeRepository.findAllByUser_Id(userId).getDay();
        for (int i = 0; i < nowDay.lengthOfMonth(); i++) {
            LocalDate day = nowDay.withDayOfMonth(1).plusDays(i);
            final Optional<DayTable> original1 = dayTableRepository.findAllByCadet_IdAndTableDay(userId, day);
            original1.filter(userTable -> userTable.getTableDay().getMonth().equals(nowDay.getMonth()))
                    .ifPresent(allUser -> {
                        allUser.setAttendScore(result);
                        dayTableRepository.save(allUser);
                    });
        }
    }

    public void deleteAojiTimeAtDay(){
        try{
            final Stream<Long> userAoji = studyTimeRepository.findAllBy()
                .stream()
                .filter(x-> userInfoRepository.existsUserInfoByAttendeStatusAndWriter_Id(1L, x.getUserId()))
                .map(x -> x.getUserId()).distinct();
            Long []list = userAoji.toArray(Long[]::new);
            for (int i = 0; i < list.length; i++){
                System.out.println(list[i]);
            }
            Long []idList = new Long[list.length];
            for (int i = 0; i < list.length; i++) {
                idList[i] = studyTimeRepository.countAllByUser_Id(list[i]);
                if (ChronoUnit.SECONDS.between(studyTimeRepository.findStudyTimeByAojiTimeIndexAndUser_Id(idList[i], list[i]).getStartAt() , studyTimeRepository.findStudyTimeByAojiTimeIndexAndUser_Id(idList[i], list[i]).getEndAt() ) == 0) {
                    final Optional<StudyTime> studyTime = studyTimeRepository.findAllByUser_IdAndAojiTimeIndex(list[i], idList[i]);
                    studyTime.ifPresent(studyTime1 -> {
                        studyTime1.setEndAt(LocalDateTime.now());
                        studyTimeRepository.save(studyTime1);
                    });
                    calcAttendScore(list[i]);
                    studyTimeRepository.deleteByUser_Id(list[i]);
                    createAojiTime(list[i]);
                } else {
                    studyTimeRepository.deleteByUser_Id(list[i]);
                }
            }
        }
        catch (Exception e){
            log.error("error deleting entity Todo",  e);
            throw new RuntimeException("error deleting entity Todo " );
        }
    }
}
