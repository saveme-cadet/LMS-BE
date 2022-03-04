package com.larry.fc.finalproject.api.service.userservice;

import com.larry.fc.finalproject.api.dto.*;
import com.larry.fc.finalproject.api.dto.userdto.UserAttendenceDto;
import com.larry.fc.finalproject.api.dto.userinfodto.*;
import com.larry.fc.finalproject.api.util.DtoConverter;
import com.larry.fc.finalproject.api.util.UserDtoConverter;
import com.larry.fc.finalproject.core.domain.entity.DayTable;
import com.larry.fc.finalproject.core.domain.entity.PlusVacation;
import com.larry.fc.finalproject.core.domain.entity.UserInfo;
import com.larry.fc.finalproject.core.domain.entity.repository.PlusVacationRepository;
import com.larry.fc.finalproject.core.domain.entity.repository.UserInfoRepository;
import com.larry.fc.finalproject.core.domain.entity.repository.UserRepository;
import com.larry.fc.finalproject.core.domain.service.UserService;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Optional;
import java.util.stream.Stream;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserInfoService {

    private final UserService userService;
    private final UserInfoRepository userInfoRepository;
    private final PlusVacationRepository plusVacationRepository;
    private final UserRepository userRepository;

    public void create(AuthUser authUser){
        final UserInfo userInfo = UserInfo.userInfoJoin(
                userService.findByUserId(authUser.getId()));

        userInfoRepository.save(userInfo);
    }

    @Transactional
    public UserInfo findByUserId(Long userId){
        return userInfoRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("no user by id"));
    }

    public void update(UserInfoDto userInfoDto, AuthUser authUser){
        UserInfo userInfo = DtoConverter.fromUserInfoDto(userInfoDto);
        final Optional<UserInfo> original = userInfoRepository.findByWriter_Id(authUser.getId());

        original.ifPresent(userInfo1-> {
            userInfo1.setConfidenceSubject(userInfo.getConfidenceSubject());
            userInfo1.setLevel(userInfo.getLevel());
            userInfo1.setNowSubject(userInfo.getNowSubject());
            userInfoRepository.save(userInfo1);
        });
    }

    public void updateWeek(UserInfoWeekDto userInfoWeekDto, AuthUser authUser){
        UserInfo userInfo = DtoConverter.fromUserInfoWeekDto(userInfoWeekDto);
        final Optional<UserInfo> original = userInfoRepository.findByWriter_Id(authUser.getId());

        original.ifPresent(userInfo1-> {
            userInfo1.setRole(userInfo.getRole());
            userInfo1.setTeam(userInfo.getTeam());
            userInfo1.setAttendScore(userInfo.getAttendScore());
            userInfo1.setVacation(userInfo.getVacation());
            userInfoRepository.save(userInfo1);
        });
        if (userInfo.getRole().equals("머슴")){
            final PlusVacation plusVacation = PlusVacation.gainServent(
                    userService.findByUserId(authUser.getId()));
            plusVacationRepository.save(plusVacation);
        }
        else if (userInfo.getRole().equals("카뎃")){
            final Optional<PlusVacation> vacation = plusVacationRepository.findAllByUserId(authUser.getId());
            vacation.ifPresent( vacation1->{
                //vacation1.setEndAt(LocalDate.now());    //localdate 시험 role 자동 + 1
                vacation1.setFinished(false);
                plusVacationRepository.save(vacation1);
            });
            Optional<PlusVacation> plusVacation = plusVacationRepository.findAllByUserId(authUser.getId());

            if (plusVacation.get().getStartAt().plusDays(7).equals(plusVacation.get().getEndAt())||
            plusVacation.get().getStartAt().plusDays(7).isBefore(plusVacation.get().getEndAt())){
                original.ifPresent(userInfo1-> {
                    userInfo1.setVacation(userInfo.getVacation() + 1);
                    userInfoRepository.save(userInfo1);
                });
            }
        }
    }

    public void updateMonth(UserInfoMonthDto userInfoMonthDto, AuthUser authUser){
        UserInfo userInfo = DtoConverter.fromUserInfoMonthDto(userInfoMonthDto);
        final Optional<UserInfo> original = userInfoRepository.findByWriter_Id(authUser.getId());

        original.ifPresent(userInfo1-> {
            userInfo1.setConfidenceSubject(userInfo.getConfidenceSubject());
            userInfo1.setLevel(userInfo.getLevel());
            userInfo1.setNowSubject(userInfo.getNowSubject());
            userInfoRepository.save(userInfo1);
        });
    }

    public void updateAttendStatus(UserAttendenceDto userAttendenceDto){
        UserInfo userInfo = DtoConverter.fromAttendStatus(userAttendenceDto);
        final Optional<UserInfo> original = userInfoRepository.findByWriter_Id(userAttendenceDto.getUser_id());
        original.ifPresent(userInfo1 -> {
            userInfo1.setAttendeStatus(userInfo.getAttendeStatus());
            userInfoRepository.save(userInfo1);
        });
    }

    public void updateUserTeam(UserTeamChangeDto userTeamChangeDto){
        UserInfo userInfo = UserDtoConverter.fromUserTeam(userTeamChangeDto);
        final Optional<UserInfo> original = userInfoRepository.findByWriter_Id(userTeamChangeDto.getUser_id());
        original.ifPresent(userInfo1 -> {
            userInfo1.setTeam(userInfo.getTeam());
            userInfoRepository.save(userInfo1);
        });
    }

    public void updateUserRole(UserRoleChangeDto userRoleChangeDto){
        UserInfo userInfo = UserDtoConverter.fromUserRole(userRoleChangeDto);
        final Optional<UserInfo> original = userInfoRepository.findByWriter_Id(userRoleChangeDto.getUser_id());
        original.ifPresent(userInfo1 -> {
            userInfo1.setRole(userInfo.getRole());
            userInfoRepository.save(userInfo1);
        });
    }

    public void updateUserVacationPlus(UserVacationChangeDto userVacationChangeDto){
        //UserInfo userInfo = DtoConverter.fromUserVacation(userVacationChangeDto);
        final Optional<UserInfo> original = userInfoRepository.findByWriter_Id(userVacationChangeDto.getUserid());
        original.ifPresent(userInfo1 -> {
            userInfo1.setVacation(userInfo1.getVacation() + 0.5);
            userInfoRepository.save(userInfo1);
        });
    }

    public void updateUserVacationMinus(UserVacationChangeDto userVacationChangeDto){
        UserInfo userInfo = UserDtoConverter.fromUserVacation(userVacationChangeDto);
        final Optional<UserInfo> original = userInfoRepository.findByWriter_Id(userVacationChangeDto.getUserid());
        original.ifPresent(userInfo1 -> {
            userInfo1.setVacation(userInfo1.getVacation() - 0.5);
            userInfoRepository.save(userInfo1);
        });
    }

    public void updateMonthUser(){
        Stream<Long> idList = userInfoRepository.findAllBy()
                .stream()
                .filter(x -> x.getAttendeStatus().equals(1L))
                .map(x -> x.getWriter_Id());

        Long[] list = idList.toArray(Long[]::new);

        for (int i = 0; i < list.length; i++) {
            final Optional<UserInfo> original = userInfoRepository.findByWriter_Id(list[i]);
            original.ifPresent(userInfo1 -> {
                userInfo1.setAttendScore(0);
                userInfo1.setLevel(userInfo1.getLevel() + 1);
                userInfo1.setParticipateScore(0);
                userInfoRepository.save(userInfo1);
            });
        }
    }

    public void delete( AuthUser authUser){
        try{
            userRepository.deleteById(authUser.getId());
        } catch (Exception e){
            log.error("error deleting entity Todo", authUser.getId(), e);
            throw new RuntimeException("error deleting entity Todo " + authUser.getId());
        }
    }
}
