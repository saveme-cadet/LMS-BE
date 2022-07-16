package batch.Tasklet.attendance;


import com.savelms.core.attendance.domain.AttendanceStatus;
import com.savelms.core.attendance.domain.entity.Attendance;
import com.savelms.core.attendance.repository.AttendanceRepository;
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

import java.time.LocalDate;
import java.util.Optional;
import java.util.stream.Stream;

public class CheckInTasklet implements Tasklet {
    private final AttendanceRepository attendanceRepository;
    private final CalendarRepository calendarRepository;
    private final UserRepository userRepository;
    private final DayStatisticalDataRepository dayStatisticalDataRepository;

    public CheckInTasklet(AttendanceRepository attendanceRepository,
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
        for (Long x : attendUserList) {
            Optional<Attendance> attendances = attendanceRepository.findAllByUserIdAndCalendarId(x, day.getId());
            if (attendances.get().getCheckInStatus().equals(AttendanceStatus.NONE)) {
                attendances.ifPresent(attendance -> {
                    attendance.setCheckInStatus(AttendanceStatus.ABSENT);
                    attendanceRepository.save(attendance);
                });
            }
            Optional<DayStatisticalData> dayStatisticalData = dayStatisticalDataRepository.findAllByUserIdAndCalendarID(x, day.getId());
            dayStatisticalData.ifPresent(dayStatisticalData1 -> {
                dayStatisticalData1.setAbsentScore(dayStatisticalData1.getAbsentScore() + 0.5);
                dayStatisticalData1.setTotalScore(dayStatisticalData1.getAbsentScore() - dayStatisticalData1.getStudyTimeScore());
                dayStatisticalDataRepository.save(dayStatisticalData1);
            });
        }
        return RepeatStatus.FINISHED;
    }

    /* update
      final Optional<DayTable> original1 = dayTableRepository.findAllByCadet_IdAndTableDay(tableCheckOutDto.getUserId(), tableCheckOutDto.getTableDay());
            original1.filter(userTable -> userTable.getTableDay().getMonth().equals(tableCheckOutDto.getTableDay().getMonth()))
                    .ifPresent(allUser -> {
                        allUser.setAttendScore(result);
                        allUser.setParticipateScore(participateResult);
                        dayTableRepository.save(allUser);
                    });
     */

}
