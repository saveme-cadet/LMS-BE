package com.larry.fc.finalproject.api.service.userservice;

import com.larry.fc.finalproject.api.dto.*;
import com.larry.fc.finalproject.api.dto.tabledto.AllTableDto;
import com.larry.fc.finalproject.api.dto.tabledto.TableCheckInDto;
import com.larry.fc.finalproject.api.dto.tabledto.TableCheckOutDto;
import com.larry.fc.finalproject.api.dto.userinfodto.UserInfoWeekDto;
import com.larry.fc.finalproject.api.dto.userinfodto.UserRoleChangeDto;
import com.larry.fc.finalproject.api.dto.userinfodto.UserTeamChangeDto;
import com.larry.fc.finalproject.api.util.DtoConverter;
import com.larry.fc.finalproject.api.util.UserDtoConverter;
import com.larry.fc.finalproject.core.domain.entity.DayTable;
import com.larry.fc.finalproject.core.domain.entity.UserInfo;
import com.larry.fc.finalproject.core.domain.entity.repository.DayTableRepository;
import com.larry.fc.finalproject.core.domain.entity.repository.UserInfoRepository;
import com.larry.fc.finalproject.core.domain.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@Slf4j
@Service
@RequiredArgsConstructor
public class AllUserTableService {
    private final DayTableRepository dayTableRepository;
    private final UserService userService;
    private final UserInfoRepository userInfoRepository;


//    public void create(AuthUser authUser){          //수정 필요 ??
//        final DayTable dayTable = DayTable.dayTableJoin(
//                userService.findByUserId(authUser.getId()));
//        dayTableRepository.save(dayTable);
//    }

    public void createUserDate(AuthUser authUser) {          //수정 필요 ??
        final DayTable dayTable = DayTable.dayTableJoin(
                userService.findByUserId(authUser.getId()));
        dayTableRepository.save(dayTable);
    }

    public void createAllDate(LocalDate date){          //수정 필요 ??
        Stream<Long> idList = userInfoRepository.findAllBy()
                .stream()
                .filter(x -> x.getAttendeStatus().equals(1L))
                .map(x -> x.getWriter_Id());

        Long[] list = idList.toArray(Long[]::new);

        UserInfo[] userInfos = new UserInfo[list.length];

        final DayTable []dayTables = new DayTable[list.length];
        for (int i = 0; i < list.length; i++){
            dayTableRepository.findAllByTableDayAndCadet_Id(date, list[i])
                    .ifPresent(u -> {
                        throw new RuntimeException("user day already exit!");
                    });
            userInfos[i] = userInfoRepository.findByWriter_IdAndAttendeStatus(list[i], 1L);
            UserInfoWeekDto userInfoWeekDto = DtoConverter.fromUserInfoWeek(userInfos[i]);
            dayTables[i] = DayTable.dayTableJoinWith(userService.findByUserId(list[i]),
                    userInfoWeekDto.getRole(), userInfoWeekDto.getTeam(), userInfoWeekDto.getAttendScore(), userInfoWeekDto.getParticipateScore());
            dayTableRepository.save(dayTables[i]);
        }
    }

    // 수정
    public void createAllDate1(){          //수정 필요 ??
        Stream<Long> idList = userInfoRepository.findAllBy()
                .stream()
                .filter(x -> x.getAttendeStatus().equals(1L))
                .map(x -> x.getWriter_Id());

        Long[] list = idList.toArray(Long[]::new);

        UserInfo[] userInfos = new UserInfo[list.length];

        final DayTable []dayTables = new DayTable[list.length];
        for (int i = 0; i < list.length; i++){
            dayTableRepository.findAllByTableDayAndCadet_Id(LocalDate.now(), list[i])
            .ifPresent(u -> {
                        throw new RuntimeException("user day already exit!");
                    });
            userInfos[i] = userInfoRepository.findByWriter_IdAndAttendeStatus(list[i], 1L);
            UserInfoWeekDto userInfoWeekDto = DtoConverter.fromUserInfoWeek(userInfos[i]);
            dayTables[i] = DayTable.dayTableJoinWith(userService.findByUserId(list[i]),
                    userInfoWeekDto.getRole(), userInfoWeekDto.getTeam(), userInfoWeekDto.getAttendScore(), userInfoWeekDto.getParticipateScore());
            dayTableRepository.save(dayTables[i]);
        }
    }

    //@Transactional
    // 수정
    public void createAll(){          //수정 필요 ??
        Stream<Long> idList = userInfoRepository.findAllBy()
                .stream()
                .filter(x -> x.getAttendeStatus().equals(1L))
                .map(x -> x.getWriter_Id());
        Long[] list = idList.toArray(Long[]::new);
        final DayTable []dayTables = new DayTable[list.length];
        for (int i = 0; i < list.length; i++){
            dayTableRepository.findAllByTableDayAndCadet_Id(LocalDate.now(), list[i])
                    .ifPresent(u -> {
                        throw new RuntimeException("user day already exit!");
                    });
            dayTables[i] = DayTable.dayTableJoin(userService.findByUserId(list[i]));
            dayTableRepository.save(dayTables[i]);
        }
    }

    public void updateCheckIn(TableCheckInDto tableCheckInDto, LocalDate date){
        DayTable dayTable = DtoConverter.fromCheckInTableDto(tableCheckInDto);
        final Optional<DayTable> original = dayTableRepository.findOptionalByCadet_IdAndTableDay(tableCheckInDto.getUserId(), date);
        original.filter(userTable -> userTable.getTableDay().equals(date))
                .ifPresent(allUser -> {
                            allUser.setCheckIn(dayTable.getCheckIn());
                            dayTableRepository.save(allUser);
                        });
        Stream<Short> checkOut = dayTableRepository.findAllByCadet_IdAndAttendeStatus(tableCheckInDto.getUserId(), 1L)
                .stream()
                .filter(x -> x.getCheckIn() != null)
                .filter(x -> x.getTableDay().getMonth().equals(tableCheckInDto.getTableDay().getMonth()))
                .map(x -> x.getCheckOut());
        Short[] checkOutList = checkOut.toArray(Short[]::new);
        Stream<Short> checkIn = dayTableRepository.findAllByCadet_IdAndAttendeStatus(tableCheckInDto.getUserId(), 1L)
                .stream()
                .filter(x -> x.getCheckOut() != null)
                .filter(x -> x.getTableDay().getMonth().equals(tableCheckInDto.getTableDay().getMonth()))
                .map(x -> x.getCheckIn());
        Short[] checkInList = checkIn.toArray(Short[]::new);
        List<Short> list1 = new ArrayList(Arrays.asList(checkInList));
        List<Short> list2 = new ArrayList(Arrays.asList(checkOutList));
        list1.addAll(list2);
        Short[] AllList = list1.toArray(new Short[0]);

        double score = 0;
        double participateScore = 0;
        for (short a : AllList){
            if (a == 2){
                score += 0.25;
            }else if (a == 3){
                score += 0.5;
            } else if (a == 1){
                participateScore += 0.5;
            }
        }
        final double result = score;
        final double participateResult = participateScore;
        if (tableCheckInDto.getCheckIn() == 6){
            final Optional<UserInfo> change = userInfoRepository.findByWriter_Id(tableCheckInDto.getUserId());
            change.ifPresent(userInfo -> {
                userInfo.setVacation(userInfo.getVacation() - 0.5);
                userInfoRepository.save(userInfo);
            });
        }
        final Optional<UserInfo> change = userInfoRepository.findByWriter_Id(tableCheckInDto.getUserId());
            change.ifPresent(userInfo -> {
                userInfo.setParticipateScore(participateResult);
                userInfo.setAttendScore(result);
                userInfoRepository.save(userInfo);
            });

        // 유저의 출석 점수 매일 갱신
//            for (int i = 0; i < tableCheckInDto.getTableDay().lengthOfMonth(); i++) {
//                LocalDate day = tableCheckInDto.getTableDay().withDayOfMonth(1).plusDays(i);
//                System.out.println(day);
                final Optional<DayTable> original1 = dayTableRepository.findAllByCadet_IdAndTableDay(tableCheckInDto.getUserId(), tableCheckInDto.getTableDay());
                original1.filter(userTable -> userTable.getTableDay().getMonth().equals(tableCheckInDto.getTableDay().getMonth()))
                        .ifPresent(allUser -> {
                            allUser.setAttendScore(result);
                            allUser.setParticipateScore(participateResult);
                            dayTableRepository.save(allUser);
                        });
            //}
    }

    public void updateCheckOut(TableCheckOutDto tableCheckOutDto,  LocalDate date){
        DayTable dayTable = DtoConverter.fromCheckOutTableDto(tableCheckOutDto);
        final Optional<DayTable> original = dayTableRepository.findAllByCadet_IdAndTableDay(tableCheckOutDto.getUserId(), dayTable.getTableDay());
        original.filter(userTable -> userTable.getTableDay().equals(date))
                .ifPresent(allUser -> {
                    allUser.setCheckOut(dayTable.getCheckOut());
                    dayTableRepository.save(allUser);
                });
        Stream<Short> checkOut = dayTableRepository
                .findAllByCadet_IdAndAttendeStatus(tableCheckOutDto.getUserId(), 1L)
                .stream()
                .filter(x -> x.getCheckIn() != null)
                .filter(x -> x.getTableDay().getMonth().equals(tableCheckOutDto.getTableDay().getMonth()))
                .map(x -> x.getCheckOut());
        Short[] checkOutList = checkOut.toArray(Short[]::new);
        Stream<Short> checkIn = dayTableRepository.findAllByCadet_IdAndAttendeStatus(tableCheckOutDto.getUserId(), 1L)
                .stream()
                .filter(x -> x.getCheckOut() != null)
                .filter(x -> x.getTableDay().getMonth().equals(tableCheckOutDto.getTableDay().getMonth()))
                .map(x -> x.getCheckIn());
        Short[] checkInList = checkIn.toArray(Short[]::new);
        List<Short> list1 = new ArrayList(Arrays.asList(checkInList));
        List<Short> list2 = new ArrayList(Arrays.asList(checkOutList));
        list1.addAll(list2);
        Short[] AllList = list1.toArray(new Short[0]);

        double score = 0;
        double participateScore = 0;
        for (short a : AllList){
            if (a == 2){
                score += 0.25;
            }else if (a == 3){
                score += 0.5;
            } else if (a == 1){
                participateScore += 0.5;
            }
        }
        final double result = score;
        final double participateResult = participateScore;
        if (tableCheckOutDto.getCheckOut() == 6){
            final Optional<UserInfo> change = userInfoRepository.findByWriter_Id(tableCheckOutDto.getUserId());
            change.ifPresent(userInfo -> {
                userInfo.setVacation(userInfo.getVacation() - 0.5);
                userInfoRepository.save(userInfo);
            });
        }
        final Optional<UserInfo> change = userInfoRepository.findByWriter_Id(tableCheckOutDto.getUserId());
        change.ifPresent(userInfo -> {
            userInfo.setParticipateScore(participateResult);
            userInfo.setAttendScore(result);
            userInfoRepository.save(userInfo);
        });

        // 유저의 출석 점수 매일 갱신
//        for (int i = 0; i < tableCheckOutDto.getTableDay().lengthOfMonth(); i++) {
//            LocalDate day = tableCheckOutDto.getTableDay().withDayOfMonth(1).plusDays(i);
            final Optional<DayTable> original1 = dayTableRepository.findAllByCadet_IdAndTableDay(tableCheckOutDto.getUserId(), tableCheckOutDto.getTableDay());
            original1.filter(userTable -> userTable.getTableDay().getMonth().equals(tableCheckOutDto.getTableDay().getMonth()))
                    .ifPresent(allUser -> {
                        allUser.setAttendScore(result);
                        allUser.setParticipateScore(participateResult);
                        dayTableRepository.save(allUser);
                    });
   //     }
    }

    public void updateUserTeam(UserTeamChangeDto userTeamChangeDto, LocalDate date){
        DayTable dayTable = DtoConverter.fromDayTableTeam(userTeamChangeDto);
        final Optional<DayTable> original = dayTableRepository.findAllByCadet_IdAndTableDay(userTeamChangeDto.getUserId(), date);

        original.filter(userTable -> userTable.getTableDay().equals(date))
                .ifPresent(allUser -> {
                    allUser.setTeam(dayTable.getTeam());
                    dayTableRepository.save(allUser);
                });
    }

    public void updateUserRole(UserRoleChangeDto userRoleChangeDto, LocalDate date){
        DayTable dayTable = DtoConverter.fromDayTableRole(userRoleChangeDto);
        final Optional<DayTable> original = dayTableRepository.findAllByCadet_IdAndTableDay(userRoleChangeDto.getUserId(), date);

        original.filter(userTable -> userTable.getTableDay().equals(date))
                .ifPresent(allUser -> {
                    allUser.setRole(dayTable.getRole());
                    dayTableRepository.save(allUser);
                });
    }

    public void updateUserAllInfo(AllTableDto allTableDto, Long id){

        DayTable dayTable = DtoConverter.fromDayTableAll(allTableDto);
        UserInfo userInfo = UserDtoConverter.fromUserAllInfo(allTableDto);
        final Optional<DayTable> original = dayTableRepository.findAllByCadet_IdAndTableDay(id, allTableDto.getTableDay());

        original.filter(userTable -> userTable.getTableDay().equals(allTableDto.getTableDay()))
                .ifPresent(allUser -> {
                    allUser.setRole(dayTable.getRole());
                    allUser.setParticipateScore(dayTable.getParticipateScore());
                    allUser.setTeam(dayTable.getTeam());
                    allUser.setRole(dayTable.getRole());
                    allUser.setCheckIn(dayTable.getCheckIn());
                    allUser.setCheckOut(dayTable.getCheckOut());
                    allUser.setAttendScore(dayTable.getAttendScore());
                    dayTableRepository.save(allUser);
                });
        final Optional<UserInfo> info = userInfoRepository.findByWriter_Id(id);
        info.ifPresent(userInfo1 -> {
            userInfo1.setVacation(userInfo.getVacation());
            userInfo1.setLevel(userInfo.getLevel());
            userInfoRepository.save(userInfo1);
        });
    }

    public void delete(Long id){
        try{
            dayTableRepository.deleteDayTableByCadet_Id(id);
        } catch (Exception e){
            log.error("error deleting entity Todo", id, e);
            throw new RuntimeException("error deleting entity Todo " + id);
        }
    }



}