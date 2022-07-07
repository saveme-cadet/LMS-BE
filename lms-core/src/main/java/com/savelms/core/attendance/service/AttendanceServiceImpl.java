package com.savelms.core.attendance.service;

import com.savelms.core.attendance.domain.AttendanceStatus;
import com.savelms.core.attendance.domain.entity.Attendance;
import com.savelms.core.attendance.repository.AttendanceRepository;
import com.savelms.core.exception.NoPermissionException;
import com.savelms.core.user.domain.entity.User;
import com.savelms.core.user.role.RoleEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class AttendanceServiceImpl implements AttendanceService{

    private final AttendanceRepository attendanceRepository;

    @Override
    @Transactional
    public void checkIn(Long attendanceId, User user, AttendanceStatus status) throws NoPermissionException {
        //디비에서 찾기
        Optional<Attendance> findAttendanceOptional = attendanceRepository.findById(attendanceId);

        findAttendanceOptional.ifPresentOrElse(findAttendance -> {
                    //변경 권한 확인
                    if (validateUserAndDatePermission(findAttendance, user)) {
                        findAttendance.checkIn(status);
                        log.info("Check-In Success: try_user={}, user={}, checkIn={}",
                                user.getUsername(), findAttendance.getUser().getUsername(), status);
                    } else { //변경 권한이 없는 유저가 조작 시 예외 발생
                        log.info("Check-In Fail: try_user={}, user={}, checkIn={}",
                                user.getUsername(), findAttendance.getUser().getUsername(), status);
                        throw new NoPermissionException("Attempt to Check-In without permission.");
                    }
                },
                //출석표가 없는 경우 -> 잘못된 attendanceId 입력 시
                () -> {
                    log.info("Check-In Fail: Attendance Not Found");
                });
    }

    @Override
    @Transactional
    public void checkOut(Long attendanceId, User user, AttendanceStatus status) throws NoPermissionException {
        //디비에서 찾기
        Optional<Attendance> findAttendanceOptional = attendanceRepository.findById(attendanceId);

        findAttendanceOptional.ifPresentOrElse(findAttendance -> {
                    //변경 권한 확인
                    if (validateUserAndDatePermission(findAttendance, user)) {
                        findAttendance.checkOut(status);
                        log.info("Check-Out Success: try_user={}, user={}, checkOut={}",
                                user.getUsername(), findAttendance.getUser().getUsername(), status);
                    } else { //변경 권한이 없는 유저가 조작 시 예외 발생
                        log.info("Check-Out Fail: try_user={}, user={}, checkOut={}",
                                user.getUsername(), findAttendance.getUser().getUsername(), status);
                        throw new NoPermissionException("Attempt to Check-Out without permission.");
                    }
                },
                //출석표가 없는 경우 -> 잘못된 attendanceId 입력 시
                () -> {
                    log.info("Check-Out Fail: Attendance Not Found");
                });
    }

    private boolean validateUserAndDatePermission(Attendance attendance, User user) {
        //지난 날짜는 출결 불가능
        boolean isToday = attendance.getCalendar().getDate().isEqual(LocalDate.now());

        //본인의 출결만 변경 가능
        boolean isUserSelf = attendance.getUser().getUsername().equals(user.getUsername());

        //머슴인 경우 다른 유저 출결 변경 가능
        boolean isManager = user.getUserRoles().equals(RoleEnum.ROLE_MANAGER);

        //오늘 날짜이면서, 변경 권한이 있는 유저
        return isToday && (isUserSelf || isManager);
    }
}
