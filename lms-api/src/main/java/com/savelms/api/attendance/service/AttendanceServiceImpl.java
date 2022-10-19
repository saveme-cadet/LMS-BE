package com.savelms.api.attendance.service;

import com.savelms.api.vacation.dto.AddVacationRequest;
import com.savelms.api.vacation.dto.UseVacationRequest;
import com.savelms.api.vacation.service.VacationService;
import com.savelms.core.attendance.domain.AttendanceStatus;
import com.savelms.core.attendance.domain.entity.Attendance;
import com.savelms.core.attendance.dto.AttendanceDto;
import com.savelms.core.attendance.domain.repository.AttendanceRepository;
import com.savelms.core.calendar.domain.repository.CalendarRepository;
import com.savelms.core.exception.NoPermissionException;
import com.savelms.core.statistical.DayStatisticalData;
import com.savelms.core.statistical.DayStatisticalDataRepository;
import com.savelms.core.user.AttendStatus;
import com.savelms.core.user.domain.entity.User;
import com.savelms.core.user.domain.repository.UserRepository;
import com.savelms.core.user.role.RoleEnum;
import com.savelms.core.user.role.domain.entity.UserRole;

import java.time.DayOfWeek;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.*;
import java.util.stream.Collectors;
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
public class AttendanceServiceImpl implements AttendanceService {

    private final AttendanceRepository attendanceRepository;
    private final DayStatisticalDataRepository statisticalDataRepository;
    private final UserRepository userRepository;
    private final VacationService vacationService;
    private final CalendarRepository calendarRepository;

    @Override
    @Transactional
    public void checkIn(Long attendanceId, String apiId, AttendanceStatus status) throws NoPermissionException {

        // attendanceId 로 변경하는 유저 찾고 그 유저의 정보 변경하기

        Optional<Attendance> findAttendanceOptional = attendanceRepository.findById(attendanceId);
        Optional<User> user = userRepository.findByApiId(apiId);            // 변경 권한 확인하기


        final Optional<Attendance> original = attendanceRepository.findById(attendanceId);
        if (original.get().getCheckInStatus().equals(VACATION)) {
            vacationService.addVacation(new AddVacationRequest(0.5D), findAttendanceOptional.get().getUser().getApiId());
        }
        original
                .ifPresent(allUser -> {
                    allUser.setCheckInStatus(status);
                    attendanceRepository.save(allUser);
                });
        Stream<AttendanceStatus> checkOut = attendanceRepository.findAttendanceByUserId(findAttendanceOptional.get().getUser().getId())
                .filter(x -> x.getCheckOutStatus() != NONE)
                .filter(x -> x.getCalendar().getDate().getMonth().equals(findAttendanceOptional.get().getCalendar().getDate().getMonth()))
                .filter(x -> !x.getCalendar().getDate().isAfter(findAttendanceOptional.get().getCalendar().getDate()))
                .map(x -> x.getCheckOutStatus());
        AttendanceStatus[] checkOutList = checkOut.toArray(AttendanceStatus[]::new);
        Stream<AttendanceStatus> checkIn = attendanceRepository.findAttendanceByUserId(findAttendanceOptional.get().getUser().getId())
                .filter(x -> x.getCheckInStatus() != NONE)
                .filter(x -> x.getCalendar().getDate().getMonth().equals(findAttendanceOptional.get().getCalendar().getDate().getMonth()))
                .filter(x -> !x.getCalendar().getDate().isAfter(findAttendanceOptional.get().getCalendar().getDate()))
                .map(x -> x.getCheckInStatus());
        AttendanceStatus[] checkInList = checkIn.toArray(AttendanceStatus[]::new);

        List<AttendanceStatus> list1 = new ArrayList(Arrays.asList(checkInList));
        List<AttendanceStatus> list2 = new ArrayList(Arrays.asList(checkOutList));
        list1.addAll(list2);

        LocalDate date = LocalDate.now().with(TemporalAdjusters.previous(DayOfWeek.SUNDAY));
        Stream<AttendanceStatus> checkOut2 = attendanceRepository.findAttendanceByUserId(user.get().getId())
                .filter(x -> x.getCheckOutStatus() != NONE)
                .filter(x -> x.getCalendar().getDate().getMonth().equals(findAttendanceOptional.get().getCalendar().getDate().getMonth()))
                .filter(x -> x.getCalendar().getDate().isBefore(date))
                .map(x -> x.getCheckOutStatus());
        AttendanceStatus[] checkOutList2 = checkOut2.toArray(AttendanceStatus[]::new);
        Stream<AttendanceStatus> checkIn2 = attendanceRepository.findAttendanceByUserId(user.get().getId())
                .filter(x -> x.getCheckInStatus() != NONE)
                .filter(x -> x.getCalendar().getDate().getMonth().equals(findAttendanceOptional.get().getCalendar().getDate().getMonth()))
                .filter(x -> x.getCalendar().getDate().isBefore(date))
                .map(x -> x.getCheckInStatus());
        AttendanceStatus[] checkInList2 = checkIn2.toArray(AttendanceStatus[]::new);
        List<AttendanceStatus> list3 = new ArrayList(Arrays.asList(checkInList2));
        List<AttendanceStatus> list4 = new ArrayList(Arrays.asList(checkOutList2));
        list3.addAll(list4);


        double score = 0;
        double participateScore = 0;
        double weekAbsentScore = 0;
        for (AttendanceStatus a : list1) {
            if (a == TARDY) {
                score += 0.25;
            } else if (a == ABSENT) {
                score += 0.5;
            } else if (a == PRESENT) {
                participateScore += 0.5;
            }
        }
        for (AttendanceStatus a : list3) {
            if (a == PRESENT)
                weekAbsentScore += 0.5;
        }

        double result = score;
        double participateResult = participateScore;
        if (status == VACATION) {
            vacationService.useVacation(new UseVacationRequest(0.5D, "휴가"), apiId);
        }
        long noOfDaysBetween = ChronoUnit.DAYS.between(findAttendanceOptional.get().getCalendar().getDate(), LocalDate.now());
        Long calendarId = findAttendanceOptional.get().getCalendar().getId();
        for (int i = 0; i < noOfDaysBetween + 1; i++) {
            Optional<DayStatisticalData> change = statisticalDataRepository.findAllByUser_idAndCalendar_id(findAttendanceOptional.get().getUser().getId(), calendarId);
            Optional<Attendance> attendStatus = attendanceRepository.findByUserIdAndCalendarId(findAttendanceOptional.get().getUser().getId(), calendarId);
            if (i != 0) {
                if (attendStatus.get().getCheckInStatus() == TARDY) {
                    score += 0.25;
                } else if (attendStatus.get().getCheckInStatus() == ABSENT) {
                    score += 0.5;
                } else if (attendStatus.get().getCheckInStatus() == PRESENT) {
                    participateScore += 0.5;
//                    if (Math.abs(calendarId - calendarRepository.findByDate(LocalDate.now()).get().getId()) < 7)
//                        weekAbsentScore += 0.5;
                }
                if (attendStatus.get().getCheckOutStatus() == TARDY) {
                    score += 0.25;
                } else if (attendStatus.get().getCheckOutStatus() == ABSENT) {
                    score += 0.5;
                } else if (attendStatus.get().getCheckOutStatus() == PRESENT) {
                    participateScore += 0.5;
//                    if (Math.abs(calendarId - calendarRepository.findByDate(LocalDate.now()).get().getId()) < 7)
//                        weekAbsentScore += 0.5;
                }
            }
            result = score;
            participateResult = participateScore;
            double finalResult = result;
            double finalParticipateResult = participateResult;
            double finalWeekAbsentScore = weekAbsentScore;
            change.ifPresent(userInfo -> {
                userInfo.setAbsentScore(finalResult);
                userInfo.setAttendanceScore(finalParticipateResult);
                userInfo.setWeekAbsentScore(finalWeekAbsentScore);
                userInfo.setTotalScore(finalResult - userInfo.getStudyTimeScore());
                statisticalDataRepository.save(userInfo);
            });
            calendarId++;
        }
    }

    @Override
    @Transactional
    public void checkOut(Long attendanceId, String userApiId, AttendanceStatus status) throws NoPermissionException {

        Optional<Attendance> findAttendanceOptional = attendanceRepository.findById(attendanceId);
        Optional<User> user = userRepository.findByApiId(userApiId);            // 변경 권한 확인하기

        final Optional<Attendance> original = attendanceRepository.findById(attendanceId);
        if (original.get().getCheckOutStatus().equals(VACATION)) {
            vacationService.addVacation(new AddVacationRequest(0.5D), findAttendanceOptional.get().getUser().getApiId());
        }
        original
                .ifPresent(allUser -> {
                    allUser.setCheckOutStatus(status);
                    attendanceRepository.save(allUser);
                });
        Stream<AttendanceStatus> checkOut = attendanceRepository.findAttendanceByUserId(user.get().getId())
                .filter(x -> x.getCheckOutStatus() != NONE)
                .filter(x -> x.getCalendar().getDate().getMonth().equals(findAttendanceOptional.get().getCalendar().getDate().getMonth()))
                .filter(x -> !x.getCalendar().getDate().isAfter(findAttendanceOptional.get().getCalendar().getDate()))
                .map(x -> x.getCheckOutStatus());
        AttendanceStatus[] checkOutList = checkOut.toArray(AttendanceStatus[]::new);
        Stream<AttendanceStatus> checkIn = attendanceRepository.findAttendanceByUserId(user.get().getId())
                .filter(x -> x.getCheckInStatus() != NONE)
                .filter(x -> x.getCalendar().getDate().getMonth().equals(findAttendanceOptional.get().getCalendar().getDate().getMonth()))
                .filter(x -> !x.getCalendar().getDate().isAfter(findAttendanceOptional.get().getCalendar().getDate()))
                .map(x -> x.getCheckInStatus());
        AttendanceStatus[] checkInList = checkIn.toArray(AttendanceStatus[]::new);

        List<AttendanceStatus> list1 = new ArrayList(Arrays.asList(checkInList));
        List<AttendanceStatus> list2 = new ArrayList(Arrays.asList(checkOutList));
        list1.addAll(list2);

        LocalDate date = LocalDate.now().with(TemporalAdjusters.previous(DayOfWeek.SUNDAY));
        Stream<AttendanceStatus> checkOut2 = attendanceRepository.findAttendanceByUserId(user.get().getId())
                .filter(x -> x.getCheckOutStatus() != NONE)
                .filter(x -> x.getCalendar().getDate().getMonth().equals(findAttendanceOptional.get().getCalendar().getDate().getMonth()))
                .filter(x -> x.getCalendar().getDate().isBefore(date))
                .map(x -> x.getCheckOutStatus());
        AttendanceStatus[] checkOutList2 = checkOut2.toArray(AttendanceStatus[]::new);
        Stream<AttendanceStatus> checkIn2 = attendanceRepository.findAttendanceByUserId(user.get().getId())
                .filter(x -> x.getCheckInStatus() != NONE)
                .filter(x -> x.getCalendar().getDate().getMonth().equals(findAttendanceOptional.get().getCalendar().getDate().getMonth()))
                .filter(x -> x.getCalendar().getDate().isBefore(date))
                .map(x -> x.getCheckInStatus());
        AttendanceStatus[] checkInList2 = checkIn2.toArray(AttendanceStatus[]::new);
        List<AttendanceStatus> list3 = new ArrayList(Arrays.asList(checkInList2));
        List<AttendanceStatus> list4 = new ArrayList(Arrays.asList(checkOutList2));
        list3.addAll(list4);



        double score = 0;
        double participateScore = 0;
        double weekAbsentScore = 0;
        for (AttendanceStatus a : list1) {
            if (a == TARDY) {
                score += 0.25;
            } else if (a == ABSENT) {
                score += 0.5;
            } else if (a == PRESENT) {
                participateScore += 0.5;
            }
        }
        for (AttendanceStatus a : list3) {
            if (a == PRESENT)
                weekAbsentScore += 0.5;
        }

        double result = score;
        double participateResult = participateScore;

        if (status == VACATION) {
            vacationService.useVacation(new UseVacationRequest(0.5D, "휴가"), userApiId);
        }

        long noOfDaysBetween = ChronoUnit.DAYS.between(findAttendanceOptional.get().getCalendar().getDate(), LocalDate.now());

        Long calendarId = findAttendanceOptional.get().getCalendar().getId();
        for (int i = 0; i < noOfDaysBetween + 1; i++) {
            Optional<DayStatisticalData> change = statisticalDataRepository.findAllByUser_idAndCalendar_id(findAttendanceOptional.get().getUser().getId(), calendarId);
            Optional<Attendance> attendStatus = attendanceRepository.findByUserIdAndCalendarId(findAttendanceOptional.get().getUser().getId(), calendarId);
            if (i != 0) {
                if (attendStatus.get().getCheckInStatus() == TARDY) {
                    score += 0.25;
                } else if (attendStatus.get().getCheckInStatus() == ABSENT) {
                    score += 0.5;
                } else if (attendStatus.get().getCheckInStatus() == PRESENT) {
                    participateScore += 0.5;
//                    if (Math.abs(calendarId - calendarRepository.findByDate(LocalDate.now()).get().getId()) < 7)
//                        weekAbsentScore += 0.5;
                }
                if (attendStatus.get().getCheckOutStatus() == TARDY) {
                    score += 0.25;
                } else if (attendStatus.get().getCheckOutStatus() == ABSENT) {
                    score += 0.5;
                } else if (attendStatus.get().getCheckOutStatus() == PRESENT) {
                    participateScore += 0.5;
//                    if (Math.abs(calendarId - calendarRepository.findByDate(LocalDate.now()).get().getId()) < 7)
//                        weekAbsentScore += 0.5;
                }
            }
            result = score;
            participateResult = participateScore;
            double finalResult = result;
            double finalParticipateResult = participateResult;
            double finalWeekAbsentScore = weekAbsentScore;
            change.ifPresent(userInfo -> {
                userInfo.setAbsentScore(finalResult);
                userInfo.setAttendanceScore(finalParticipateResult);
                userInfo.setWeekAbsentScore(finalWeekAbsentScore);
                userInfo.setTotalScore(finalResult - userInfo.getStudyTimeScore());
                statisticalDataRepository.save(userInfo);
            });
            calendarId++;
        }
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

    public Map<Long, Attendance> getAllAttendanceByDateAndAttendStatus(LocalDate date, AttendStatus attendStatus) {

        return attendanceRepository.findAllByDateAndAttendStatusWithUser(date, attendStatus)
                .stream()
                .collect(Collectors.toMap(
                        attendance -> attendance.getUser().getId(),
                        attendance -> attendance
                ));
    }

    public Map<Long, AttendanceDto> getAllAttendanceByDate(LocalDate date) {


        List<Attendance> list = attendanceRepository.findByCalendarId(calendarRepository.findAllByDate(date).getId());
        Map<Long, AttendanceDto> map = new HashMap<>();

        for (int i = 0; i < list.size(); i++) {
            map.put(list.get(i).getId(), new AttendanceDto(list.get(i).getUser().getApiId(), list.get(i).getId(), list.get(i).getCheckInStatus(), list.get(i).getCheckOutStatus()));
        }

        return map;
    }
}
