package com.savelms.api.attendance.service;

import com.savelms.api.statistical.service.DayStatisticalDataService;
import com.savelms.api.vacation.dto.UseVacationRequest;
import com.savelms.api.vacation.service.VacationService;
import com.savelms.core.attendance.domain.AttendanceStatus;
import com.savelms.core.attendance.domain.entity.Attendance;
import com.savelms.core.attendance.dto.AttendanceDto;
import com.savelms.core.attendance.domain.repository.AttendanceRepository;
import com.savelms.core.exception.NoPermissionException;
import com.savelms.core.statistical.DayStatisticalData;
import com.savelms.core.statistical.DayStatisticalDataRepository;
import com.savelms.core.user.domain.entity.User;
import com.savelms.core.user.domain.repository.UserRepository;
import com.savelms.core.user.role.RoleEnum;
import com.savelms.core.user.role.domain.entity.UserRole;

import java.util.*;
import java.util.stream.Collectors;

import com.savelms.core.vacation.domain.entity.Vacation;
import com.savelms.core.vacation.domain.repository.VacationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.util.stream.Stream;

import static com.savelms.core.attendance.domain.AttendanceStatus.*;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class AttendanceServiceImpl implements AttendanceService{

    private final AttendanceRepository attendanceRepository;
    private final DayStatisticalDataRepository statisticalDataRepository;
    private final UserRepository userRepository;
    private final VacationService vacationService;
    private final VacationRepository vacationRepository;

//
//    public void updateCheckIn(TableCheckInDto tableCheckInDto, LocalDate date){
//        DayTable dayTable = DtoConverter.fromCheckInTableDto(tableCheckInDto);
//        final Optional<DayTable> original = dayTableRepository.findOptionalByCadet_IdAndTableDay(tableCheckInDto.getUserId(), date);
//        original.filter(userTable -> userTable.getTableDay().equals(date))
//                .ifPresent(allUser -> {
//                    allUser.setCheckIn(dayTable.getCheckIn());
//                    dayTableRepository.save(allUser);
//                });
//        Stream<Short> checkOut = dayTableRepository.findAllByCadet_IdAndAttendeStatus(tableCheckInDto.getUserId(), 1L)
//                .stream()
//                .filter(x -> x.getCheckIn() != null)
//                .filter(x -> x.getTableDay().getMonth().equals(tableCheckInDto.getTableDay().getMonth()))
//                .map(x -> x.getCheckOut());
//        Short[] checkOutList = checkOut.toArray(Short[]::new);
//        Stream<Short> checkIn = dayTableRepository.findAllByCadet_IdAndAttendeStatus(tableCheckInDto.getUserId(), 1L)
//                .stream()
//                .filter(x -> x.getCheckOut() != null)
//                .filter(x -> x.getTableDay().getMonth().equals(tableCheckInDto.getTableDay().getMonth()))
//                .map(x -> x.getCheckIn());
//        Short[] checkInList = checkIn.toArray(Short[]::new);
//        List<Short> list1 = new ArrayList(Arrays.asList(checkInList));
//        List<Short> list2 = new ArrayList(Arrays.asList(checkOutList));
//        list1.addAll(list2);
//        Short[] AllList = list1.toArray(new Short[0]);
//
//        double score = 0;
//        double participateScore = 0;
//        for (short a : AllList){
//            if (a == 2){
//                score += 0.25;
//            }else if (a == 3){
//                score += 0.5;
//            } else if (a == 1){
//                participateScore += 0.5;
//            }
//        }
//        final double result = score;
//        final double participateResult = participateScore;
//        if (tableCheckInDto.getCheckIn() == 6){
//            final Optional<UserInfo> change = userInfoRepository.findByWriter_Id(tableCheckInDto.getUserId());
//            change.ifPresent(userInfo -> {
//                userInfo.setVacation(userInfo.getVacation() - 0.5);
//                userInfoRepository.save(userInfo);
//            });
//        }
//        final Optional<UserInfo> change = userInfoRepository.findByWriter_Id(tableCheckInDto.getUserId());
//        change.ifPresent(userInfo -> {
//            userInfo.setParticipateScore(participateResult);
//            userInfo.setAttendScore(result);
//            userInfoRepository.save(userInfo);
//        });
//
//        // 유저의 출석 점수 매일 갱신
////            for (int i = 0; i < tableCheckInDto.getTableDay().lengthOfMonth(); i++) {
////                LocalDate day = tableCheckInDto.getTableDay().withDayOfMonth(1).plusDays(i);
////                System.out.println(day);
//        final Optional<DayTable> original1 = dayTableRepository.findAllByCadet_IdAndTableDay(tableCheckInDto.getUserId(), tableCheckInDto.getTableDay());
//        original1.filter(userTable -> userTable.getTableDay().getMonth().equals(tableCheckInDto.getTableDay().getMonth()))
//                .ifPresent(allUser -> {
//                    allUser.setAttendScore(result);
//                    allUser.setParticipateScore(participateResult);
//                    dayTableRepository.save(allUser);
//                });
//        //}
//    }

    @Override
    @Transactional
    public void checkIn(Long attendanceId, String apiId, AttendanceStatus status) throws NoPermissionException {

        // attendanceId 로 변경하는 유저 찾고 그 유저의 정보 변경하기

        Optional<Attendance> findAttendanceOptional = attendanceRepository.findById(attendanceId);
        Optional<User> user = userRepository.findByApiId(apiId);            // 변경 권한 확인하기

        final Optional<Attendance> original = attendanceRepository.findById(attendanceId);
        original
                .ifPresent(allUser -> {
                    allUser.setCheckInStatus(status);
                    attendanceRepository.save(allUser);
                });
        Stream<AttendanceStatus> checkOut = attendanceRepository.findAttendanceByUserId(user.get().getId())
                .filter(x -> x.getCheckOutStatus() != NONE)
                .filter(x -> x.getCalendar().getDate().getMonth().equals(findAttendanceOptional.get().getCalendar().getDate().getMonth()))
                .map(x -> x.getCheckOutStatus());
        Short[] checkOutList = checkOut.toArray(Short[]::new);
        Stream<AttendanceStatus> checkIn =attendanceRepository.findAttendanceByUserId(user.get().getId())
                .filter(x -> x.getCheckOutStatus() != NONE)
                .filter(x -> x.getCalendar().getDate().getMonth().equals(findAttendanceOptional.get().getCalendar().getDate().getMonth()))
                .map(x -> x.getCheckInStatus());
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
        if (status == VACATION){
            vacationService.useVacation(new UseVacationRequest(0.5D, "휴가"), apiId);
        }
        final Optional<DayStatisticalData> change = statisticalDataRepository.findByApiIdAndDate(apiId, findAttendanceOptional.get().getCalendar().getDate());
        change.ifPresent(userInfo -> {
            userInfo.setAbsentScore(result);
            userInfo.setAttendanceScore(participateResult);
            statisticalDataRepository.save(userInfo);
        });
    }

    @Override
    @Transactional
    public void checkOut(Long attendanceId, String userApiId, AttendanceStatus status) throws NoPermissionException {
        Optional<Attendance> findAttendanceOptional = attendanceRepository.findById(attendanceId);
        Optional<User> user = userRepository.findByApiId(userApiId);
        if (!(findAttendanceOptional.get().getCheckInStatus().equals(NONE) || findAttendanceOptional.get().getCheckInStatus().equals(VACATION))) {
            statisticalDataRepository.findByUsernameAndDate(findAttendanceOptional.get().getUser().getUsername(), findAttendanceOptional.get().getCalendar().getDate())
                    .ifPresentOrElse(dayStatisticalData -> {
                        dayStatisticalData.updateAttendanceScore(-1 * findAttendanceOptional.get().getCheckInStatus().getAttendanceScore());
                        dayStatisticalData.updateAbsentScore( -1 * findAttendanceOptional.get().getCheckInStatus().getAttendanceScore());

                        if (status.equals(ABSENT)) {
                            dayStatisticalData.updateWeekAbsentScore(-1 * 0.5D);
                        }
                    }, () -> {
                        log.warn("통계 테이블에 당일 레코드가 존재하지 않아 점수가 업데이트되지 않았습니다.");
                    });
        } else if (findAttendanceOptional.get().getCheckInStatus().equals(VACATION)) {
            vacationService.useVacation(new UseVacationRequest(0.5D, "출결 휴가"), userApiId);

        }
        findAttendanceOptional.ifPresentOrElse(findAttendance -> {
                    //변경 권한 확인
              //      if (validateUserAndDatePermission(findAttendance, user, findAttendanceOptional.get().getCalendar().getDate())) {
                        //체크인을 하지 않고 체크아웃을 시도하는 경우 예외 발생
                        if (findAttendance.getCheckInStatus() == AttendanceStatus.NONE) {
                            log.info("Check-Out Fail: Must try Check-In first");

                            throw new IllegalStateException("Must try Check-In first");
                        }
                        findAttendance.checkOut(status);
                        updateAttendanceAndAbsentScore(findAttendanceOptional.get().getUser(), status, findAttendanceOptional.get().getCalendar().getDate());

                        log.info("Check-Out Success: try_user={}, user={}, checkOut={}",
                                findAttendanceOptional.get().getUser().getUsername(), findAttendance.getUser().getUsername(), status);
//                    } else { //변경 권한이 없는 유저가 조작 시 예외 발생
//                        log.info("Check-Out Fail: try_user={}, user={}, checkOut={}",
//                                user.getUsername(), findAttendance.getUser().getUsername(), status);
//                        throw new NoPermissionException("Attempt to Check-Out without permission");
//                    }
                },
                //출석표가 없는 경우 -> 잘못된 attendanceId 입력 시
                () -> {
                    log.info("Check-Out Fail: Attendance Not Found");
                    throw new NoSuchElementException("Attendance Not Found");
                });
    }

    private void updateAttendanceAndAbsentScore(User user, AttendanceStatus status, LocalDate date) {
        statisticalDataRepository.findByUsernameAndDate(user.getUsername(), date)
                .ifPresentOrElse(dayStatisticalData -> {
                    dayStatisticalData.updateAttendanceScore(status.getAttendanceScore());
                    dayStatisticalData.updateAbsentScore(status.getAttendancePenalty());

                    if (status.equals(ABSENT)) {
                        dayStatisticalData.updateWeekAbsentScore(0.5D);
                    }
                }, () -> {
                    log.warn("통계 테이블에 당일 레코드가 존재하지 않아 점수가 업데이트되지 않았습니다.");
                });
    }

    private boolean validateUserAndDatePermission(Attendance attendance, User user, LocalDate date) {
        //지난 날짜는 출결 불가능
        boolean isToday = attendance.getCalendar().getDate().isEqual(date);

        //본인의 출결만 변경 가능
        boolean isUserSelf = attendance.getUser().getUsername().equals(user.getUsername());

        //시도하려는 유저의 가장 최근에 기록(createdAt)된 역할을 가져온다.
        UserRole userRole = user.getUserRoles().stream()
                .max(Comparator.comparing(UserRole::getCreatedAt))
                .get();

        //현재는 문자열로 비교하지만, Enum 타입으로 바뀌면 다른 방법 사용
        //일단 String이라서 equals로 비교했습니다. Enum으로 바꾼다면 == 비교로 변경해도 괜찮습니다.
        //아래 주석 코드 사용하기
        String tryUserRole = userRole.getRole().getValue().name();
        boolean isManager = (tryUserRole.equals(RoleEnum.ROLE_MANAGER.name()));

//        RoleEnum tryUserRole = userRole.getRole().get  /*RoleEnum 필드 get메서드*/  ();
//        boolean isManager = (tryUserRole == RoleEnum.ROLE_MANAGER);

        //오늘 날짜이면서, 변경 권한이 있는 유저
        return isToday && (isUserSelf || isManager);
    }

    public AttendanceDto getAttendanceByDate(String username, LocalDate date) {
        Attendance attendance = attendanceRepository.findByUsernameAndDate(username, date)
                .orElseThrow(() -> new EntityNotFoundException("출석정보를 찾지 못하였습니다."));

        return new AttendanceDto(attendance);
    }

    public Map<Long, AttendanceDto> getAllAttendanceByDate(LocalDate date) {
        return attendanceRepository.findAllByDateWithUser(date)
            .stream()
            .collect(Collectors.toMap(
                attendance -> attendance.getUser().getId(),
                AttendanceDto::new
            ));
    }



//    public List<> getAttendanceAllUserByDate() {
//
//    }

}
