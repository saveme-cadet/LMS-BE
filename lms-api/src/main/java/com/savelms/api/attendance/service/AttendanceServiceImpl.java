package com.savelms.api.attendance.service;

import com.savelms.api.statistical.service.DayStatisticalDataService;
import com.savelms.core.attendance.domain.AttendanceStatus;
import com.savelms.core.attendance.domain.entity.Attendance;
import com.savelms.core.attendance.dto.AttendanceDto;
import com.savelms.core.attendance.domain.repository.AttendanceRepository;
import com.savelms.core.exception.NoPermissionException;
import com.savelms.core.user.domain.entity.User;
import com.savelms.core.user.role.RoleEnum;
import com.savelms.core.user.role.domain.entity.UserRole;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class AttendanceServiceImpl implements AttendanceService{

    private final AttendanceRepository attendanceRepository;
    private final DayStatisticalDataService statisticalDataService;

    @Override
    @Transactional
    public void checkIn(Long attendanceId, User user, AttendanceStatus status) throws NoPermissionException {
        Optional<Attendance> findAttendanceOptional = attendanceRepository.findById(attendanceId);

        findAttendanceOptional.ifPresentOrElse(findAttendance -> {
                    //변경 권한 확인
                    if (validateUserAndDatePermission(findAttendance, user)) {
                        findAttendance.checkIn(status);

                        statisticalDataService.updateAttendanceAndAbsentScore(user.getUsername(), status, LocalDate.now());

                        log.info("Check-In Success: try_user={}, user={}, checkIn={}",
                                user.getUsername(), findAttendance.getUser().getUsername(), status);
                    } else { //변경 권한이 없는 유저가 조작 시 예외 발생
                        log.info("Check-In Fail: try_user={}, user={}, checkIn={}",
                                user.getUsername(), findAttendance.getUser().getUsername(), status);
                        throw new NoPermissionException("Attempt to Check-In without permission");
                    }
                },
                //출석표가 없는 경우 -> 잘못된 attendanceId 입력 시
                () -> {
                    log.info("Check-In Fail: Attendance Not Found");
                    throw new NoSuchElementException("Attendance Not Found");
                });
    }

    @Override
    @Transactional
    public void checkOut(Long attendanceId, User user, AttendanceStatus status) throws NoPermissionException {
        Optional<Attendance> findAttendanceOptional = attendanceRepository.findById(attendanceId);

        findAttendanceOptional.ifPresentOrElse(findAttendance -> {
                    //변경 권한 확인
                    if (validateUserAndDatePermission(findAttendance, user)) {
                        //체크인을 하지 않고 체크아웃을 시도하는 경우 예외 발생
                        if (findAttendance.getCheckInStatus() == AttendanceStatus.NONE) {
                            log.info("Check-Out Fail: Must try Check-In first");

                            throw new IllegalStateException("Must try Check-In first");
                        }
                        findAttendance.checkOut(status);

                        statisticalDataService.updateAttendanceAndAbsentScore(user.getUsername(), status, LocalDate.now());
                        log.info("Check-Out Success: try_user={}, user={}, checkOut={}",
                                user.getUsername(), findAttendance.getUser().getUsername(), status);
                    } else { //변경 권한이 없는 유저가 조작 시 예외 발생
                        log.info("Check-Out Fail: try_user={}, user={}, checkOut={}",
                                user.getUsername(), findAttendance.getUser().getUsername(), status);
                        throw new NoPermissionException("Attempt to Check-Out without permission");
                    }
                },
                //출석표가 없는 경우 -> 잘못된 attendanceId 입력 시
                () -> {
                    log.info("Check-Out Fail: Attendance Not Found");
                    throw new NoSuchElementException("Attendance Not Found");
                });
    }

    private boolean validateUserAndDatePermission(Attendance attendance, User user) {
        //지난 날짜는 출결 불가능
        boolean isToday = attendance.getCalendar().getDate().isEqual(LocalDate.now());

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
