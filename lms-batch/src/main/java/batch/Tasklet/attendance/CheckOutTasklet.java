package batch.Tasklet.attendance;


import com.savelms.core.attendance.domain.AttendanceStatus;
import com.savelms.core.attendance.domain.entity.Attendance;
import com.savelms.core.attendance.domain.repository.AttendanceRepository;
import com.savelms.core.calendar.domain.entity.Calendar;
import com.savelms.core.calendar.domain.repository.CalendarRepository;
import com.savelms.core.statistical.DayStatisticalData;
import com.savelms.core.statistical.DayStatisticalDataRepository;
import com.savelms.core.user.AttendStatus;
import com.savelms.core.user.domain.repository.UserRepository;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static com.savelms.core.attendance.domain.AttendanceStatus.ABSENT;

public class CheckOutTasklet implements Tasklet {
    private final AttendanceRepository attendanceRepository;
    private final CalendarRepository calendarRepository;
    private final UserRepository userRepository;
    private final DayStatisticalDataRepository dayStatisticalDataRepository;

    public CheckOutTasklet(AttendanceRepository attendanceRepository,
                           CalendarRepository calendarRepository,
                           UserRepository userRepository,
                           DayStatisticalDataRepository dayStatisticalDataRepository) {
        this.attendanceRepository = attendanceRepository;
        this.calendarRepository = calendarRepository;
        this.userRepository = userRepository;
        this.dayStatisticalDataRepository = dayStatisticalDataRepository;
    }

    @Override
    public RepeatStatus execute(StepContribution contribution,
                                ChunkContext chunkContext)
            throws Exception {
        Stream<Long> attendUser = userRepository
                .findAllByAttendStatus(AttendStatus.PARTICIPATED)
                .stream()
                .map(x -> x.getId());
        Long[] attendUserList = attendUser.toArray(Long[]::new);

        // calendar에서 요일 뽑아내기
        final Calendar day = calendarRepository.findAllByDate(LocalDate.now());


        LocalDate date = LocalDate.now().with(TemporalAdjusters.previous(DayOfWeek.SATURDAY));
        for (Long x : attendUserList) {
            Optional<Attendance> attendances = attendanceRepository.findByUserIdAndCalendarId(x, day.getId());
            /*
            AttendStatus를 NONE에서 ABSENT 변경
            체크를 안해줬으니 자동으로 ABSENT로 변경해준다.
            혹은 진짜 결석일 수도 있으니
             */
            if (attendances.get().getCheckOutStatus().equals(AttendanceStatus.NONE)) {
                attendances.ifPresent(attendance -> {
                    attendance.setCheckOutStatus(AttendanceStatus.ABSENT);
                    attendanceRepository.save(attendance);
                });

                Stream<AttendanceStatus> checkOut2 = attendanceRepository.findAttendanceByUserId(x)
                        .filter(x1 -> x1.getCheckOutStatus() == ABSENT)
                        .filter(x1 -> x1.getCalendar().getDate().getMonth().equals(day.getDate().getMonth()))
                        .filter(x1 -> x1.getCalendar().getDate().isAfter(date))
                        .map(x1 -> x1.getCheckOutStatus());
                AttendanceStatus[] checkOutList2 = checkOut2.toArray(AttendanceStatus[]::new);
                Stream<AttendanceStatus> checkIn2 = attendanceRepository.findAttendanceByUserId(x)
                        .filter(x1 -> x1.getCheckInStatus() == ABSENT)
                        .filter(x1 -> x1.getCalendar().getDate().getMonth().equals(day.getDate().getMonth()))
                        .filter(x1 -> x1.getCalendar().getDate().isAfter(date))
                        .map(x1 -> x1.getCheckInStatus());
                AttendanceStatus[] checkInList2 = checkIn2.toArray(AttendanceStatus[]::new);
                List<AttendanceStatus> list3 = new ArrayList(Arrays.asList(checkInList2));
                List<AttendanceStatus> list4 = new ArrayList(Arrays.asList(checkOutList2));
                list3.addAll(list4);
                double weekAbsentScore = list3.size() * 0.5;

                /*
                AttendanceStatus 상태가 None에서 ABSENT로 변경됨에 따라 점수도 동시에 변경을 해준다.
                */

                Optional<DayStatisticalData> dayStatisticalData = dayStatisticalDataRepository.findAllByUser_idAndCalendar_id(x, day.getId());
                dayStatisticalData.ifPresent(dayStatisticalData1 -> {
                    dayStatisticalData1.setAbsentScore(dayStatisticalData1.getAbsentScore() + 0.5);
                    dayStatisticalData1.setTotalScore(dayStatisticalData1.getAbsentScore() - dayStatisticalData1.getStudyTimeScore());
                    dayStatisticalData1.setWeekAbsentScore(weekAbsentScore);
                    dayStatisticalDataRepository.save(dayStatisticalData1);
                });
            }
        }
        return RepeatStatus.FINISHED;
    }
}
