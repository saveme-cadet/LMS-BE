package com.larry.fc.finalproject.api.service.userservice;

import com.larry.fc.finalproject.api.dto.*;
import com.larry.fc.finalproject.api.dto.userdto.UserAttendenceDto;
import com.larry.fc.finalproject.api.dto.userdto.UserTeamAndRoleDto;
import com.larry.fc.finalproject.api.dto.userinfodto.*;
import com.larry.fc.finalproject.api.util.DtoConverter;
import com.larry.fc.finalproject.api.util.UserDtoConverter;
import com.larry.fc.finalproject.core.domain.entity.DayTable;
import com.larry.fc.finalproject.core.domain.entity.PlusVacation;
import com.larry.fc.finalproject.core.domain.entity.user.User;
import com.larry.fc.finalproject.core.domain.entity.UserInfo;
import com.larry.fc.finalproject.core.domain.entity.repository.DayTableRepository;
import com.larry.fc.finalproject.core.domain.entity.repository.PlusVacationRepository;
import com.larry.fc.finalproject.core.domain.entity.repository.UserInfoRepository;
import com.larry.fc.finalproject.core.domain.entity.repository.UserRepository;
import com.larry.fc.finalproject.core.domain.service.UserService;
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
    private final DayTableRepository dayTableRepository;

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
        final Optional<UserInfo> original = userInfoRepository.findByWriter_Id(userAttendenceDto.getUserId());
        original.ifPresent(userInfo1 -> {
            userInfo1.setAttendeStatus(userInfo.getAttendeStatus());
            userInfoRepository.save(userInfo1);
        });
        User user = UserDtoConverter.fromUser(userAttendenceDto);
        final Optional<User> userConvert = userRepository.findAllById(userAttendenceDto.getUserId());
        userConvert.ifPresent(userInfo1 -> {
            userInfo1.setAttendStatus(userInfo.getAttendeStatus());
            userRepository.save(userInfo1);
        });
        for (int i = 0; i < LocalDate.now().lengthOfMonth(); i++) {
            LocalDate day = LocalDate.now().withDayOfMonth(1).plusDays(i);
            final Optional<DayTable> original1 = dayTableRepository.findAllByCadet_IdAndTableDay(userAttendenceDto.getUserId(), day);
            original1.filter(userTable -> userTable.getTableDay().getMonth().equals(LocalDate.now().getMonth()))
                    .ifPresent(allUser -> {
                        allUser.setAttendeStatus(userAttendenceDto.getAttendStatus());
                        dayTableRepository.save(allUser);
                    });
        }
        if (userAttendenceDto.getAttendStatus() == 1L){
            dayTableRepository.findAllByTableDayAndCadet_Id(LocalDate.now(), userAttendenceDto.getUserId())
                    .ifPresent(u -> {
                        throw new RuntimeException("user day already exit!");
                    });
            UserInfo userInfo1 = userInfoRepository.findByWriter_IdAndAttendeStatus(userAttendenceDto.getUserId(), 1L);
            UserInfoWeekDto userInfoWeekDto = DtoConverter.fromUserInfoWeek(userInfo1);
            DayTable dayTable = DayTable.dayTableJoinWith(userService.findByUserId(userAttendenceDto.getUserId()),
                    userInfoWeekDto.getRole(), userInfoWeekDto.getTeam(), userInfoWeekDto.getAttendScore(), userInfoWeekDto.getParticipateScore());
            dayTableRepository.save(dayTable);
        } else if (userAttendenceDto.getAttendStatus() != 1L){
            try{
                dayTableRepository.deleteDayTableByTableDayAndCadet_Id(LocalDate.now(), userAttendenceDto.getUserId());
            } catch (Exception e){
                log.error("error deleting entity Todo",userAttendenceDto.getUserId(), e);
                throw new RuntimeException("error deleting entity daytable " + userAttendenceDto.getUserId());
            }
        }
    }

    public void updateTeamAndRole(UserTeamAndRoleDto userTeamAndRoleDto){
        UserInfo userInfo = DtoConverter.fromTeamAndRole(userTeamAndRoleDto);
        final Optional<UserInfo> original = userInfoRepository.findByWriter_Id(userTeamAndRoleDto.getUserId());
        original.ifPresent(userInfo1 -> {
            userInfo1.setRole(userInfo.getRole());
            userInfo1.setTeam(userInfo.getTeam());
            userInfoRepository.save(userInfo1);
        });

        for (int i = 0; i < LocalDate.now().lengthOfMonth(); i++) {
            LocalDate day = LocalDate.now().withDayOfMonth(1).plusDays(i);
            final Optional<DayTable> original1 = dayTableRepository.findAllByCadet_IdAndTableDay(userTeamAndRoleDto.getUserId(), day);
            original1.filter(userTable -> userTable.getTableDay().getMonth().equals(LocalDate.now().getMonth()))
                    .ifPresent(allUser -> {
                        allUser.setRole(userTeamAndRoleDto.getRole());
                        allUser.setTeam(userTeamAndRoleDto.getTeam());
                        dayTableRepository.save(allUser);
                    });
        }
    }

    public void updateUserTeam(UserTeamChangeDto userTeamChangeDto){
        UserInfo userInfo = UserDtoConverter.fromUserTeam(userTeamChangeDto);
        final Optional<UserInfo> original = userInfoRepository.findByWriter_Id(userTeamChangeDto.getUserId());
        original.ifPresent(userInfo1 -> {
            userInfo1.setTeam(userInfo.getTeam());
            userInfoRepository.save(userInfo1);
        });

        for (int i = 0; i < LocalDate.now().lengthOfMonth(); i++) {
            LocalDate day = LocalDate.now().withDayOfMonth(1).plusDays(i);
            final Optional<DayTable> original1 = dayTableRepository.findAllByCadet_IdAndTableDay(userTeamChangeDto.getUserId(), day);
            original1.filter(userTable -> userTable.getTableDay().getMonth().equals(LocalDate.now().getMonth()))
                    .ifPresent(allUser -> {
                        allUser.setTeam(userTeamChangeDto.getTeam());
                        dayTableRepository.save(allUser);
                    });
        }
    }

    public void updateUserRole(UserRoleChangeDto userRoleChangeDto) {
        UserInfo userInfo = UserDtoConverter.fromUserRole(userRoleChangeDto);
        final Optional<UserInfo> original = userInfoRepository.findByWriter_Id(userRoleChangeDto.getUserId());
        original.ifPresent(userInfo1 -> {
            userInfo1.setRole(userInfo.getRole());
            userInfoRepository.save(userInfo1);
        });
        for (int i = 0; i < LocalDate.now().lengthOfMonth(); i++) {
            LocalDate day = LocalDate.now().withDayOfMonth(1).plusDays(i);
            final Optional<DayTable> original1 = dayTableRepository.findAllByCadet_IdAndTableDay(userRoleChangeDto.getUserId(), day);
            original1.filter(userTable -> userTable.getTableDay().getMonth().equals(LocalDate.now().getMonth()))
                    .ifPresent(allUser -> {
                        allUser.setRole(userRoleChangeDto.getRole());
                        dayTableRepository.save(allUser);
                    });
        }
    }

    public void updateUserVacationPlus(UserVacationChangeDto userVacationChangeDto){
        final Optional<UserInfo> original = userInfoRepository.findByWriter_Id(userVacationChangeDto.getUserId());
        original.ifPresent(userInfo1 -> {
            userInfo1.setVacation(userInfo1.getVacation() + 0.5);
            userInfoRepository.save(userInfo1);
        });
    }

    public void updateUserVacationMinus(UserVacationChangeDto userVacationChangeDto){
        final Optional<UserInfo> original = userInfoRepository.findByWriter_Id(userVacationChangeDto.getUserId());
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
                userInfo1.setAojitimescore(0);
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

    public void updateMonthOfUserAttendScore(){
        Stream<Long> idList = userInfoRepository.findAllBy()
                .stream()
                .filter(x -> x.getAttendeStatus().equals(1L))
                .map(x -> x.getWriter_Id());

        Long[] list = idList.toArray(Long[]::new);
        for (int i = 0; i < list.length; i++) {
            final Optional<UserInfo> original = userInfoRepository.findByWriter_Id(list[i]);
            original.ifPresent(userInfo1 -> {
                userInfo1.setAttendScore(userInfo1.getAttendScore() - userInfo1.getAojitimescore());
                userInfoRepository.save(userInfo1);
            });
        }
    }
}
