package batch.job.Tasklet;


import com.savelms.core.attendance.domain.AttendanceStatus;
import com.savelms.core.attendance.domain.entity.Attendance;
import com.savelms.core.attendance.repository.AttendanceRepository;
import com.savelms.core.calendar.domain.entity.Calendar;
import com.savelms.core.calendar.domain.repository.CalendarRepository;
import com.savelms.core.user.AttendStatus;
import com.savelms.core.user.domain.repository.UserRepository;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

import java.time.LocalDate;
import java.util.Optional;
import java.util.stream.Stream;

public class CheckOutTasklet implements Tasklet {
    private final AttendanceRepository attendanceRepository;
    private final CalendarRepository calendarRepository;
    private final UserRepository userRepository;

    public CheckOutTasklet(AttendanceRepository attendanceRepository, CalendarRepository calendarRepository, UserRepository userRepository) {
        this.attendanceRepository = attendanceRepository;
        this.calendarRepository = calendarRepository;
        this.userRepository = userRepository;
    }

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        Stream<Long> attendUser = userRepository
                .findAllByAttendStatus(AttendStatus.PARTICIPATED)
                .stream()
                .map(x -> x.getId());
        Long[] attendUserList = attendUser.toArray(Long[]::new);

        // calendar에서 요일 뽑아내기
        final Calendar day = calendarRepository.findAllByDate(LocalDate.now());
        for (Long x : attendUserList) {
            Optional<Attendance> attendances = attendanceRepository.findAllByUserIdAndCalendarId(x, day.getId());
            if (attendances.get().getCheckOutStatus().equals(AttendanceStatus.NONE)) {
                attendances.ifPresent(attendance -> {
                    attendance.setCheckOutStatus(AttendanceStatus.ABSENT);
                    attendanceRepository.save(attendance);
                });
            }
        }
        return RepeatStatus.FINISHED;
    }
}
