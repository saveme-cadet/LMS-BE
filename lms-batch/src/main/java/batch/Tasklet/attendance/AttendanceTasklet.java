package batch.Tasklet.attendance;

import com.savelms.core.attendance.domain.AttendanceStatus;
import com.savelms.core.attendance.domain.entity.Attendance;
import com.savelms.core.attendance.domain.repository.AttendanceRepository;
import com.savelms.core.calendar.domain.entity.Calendar;
import com.savelms.core.calendar.domain.repository.CalendarRepository;
import com.savelms.core.user.AttendStatus;
import com.savelms.core.user.domain.repository.UserRepository;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

public class AttendanceTasklet implements Tasklet {
    private final CalendarRepository calendarRepository;
    private final UserRepository userRepository;
    private final AttendanceRepository attendanceRepository;

    public AttendanceTasklet(CalendarRepository calendarRepository,
                             UserRepository userRepository,
                             AttendanceRepository attendanceRepository) {
        this.calendarRepository = calendarRepository;
        this.userRepository = userRepository;
        this.attendanceRepository = attendanceRepository;
    }

    @Override
    public RepeatStatus execute(StepContribution contribution,
                                ChunkContext chunkContext)
            throws Exception {
        List<Attendance> attendances = createAttendance();

        Collections.shuffle(attendances);
        attendanceRepository.saveAll(attendances);
        return RepeatStatus.FINISHED;
    }

    // 매일 출석 테이블 생성
    private List<Attendance> createAttendance() {
        List<Attendance> attendanceData = new ArrayList<>();

        // user 에서 참석하는 사람 뽑아내기
        Stream<Long> attendUser = userRepository
                .findAllByAttendStatus(AttendStatus.PARTICIPATED)
                .stream()
                .map(x -> x.getId());
        Long[] attendUserList = attendUser.toArray(Long[]::new);

        // calendar에서 요일 뽑아내기
        final Calendar day = calendarRepository.findAllByDate(LocalDate.now().minusDays(1));
        System.out.println(day.getId());

        for (Long x : attendUserList) {
            System.out.println("=============================" + x + "===================");
            //     DayStatisticalData dayStatisticalData = dayStatisticalDataRepository.findByuser_idAndCalendar(x, day.getId());
            attendanceData.add(Attendance.builder()
                            .calendar(day)
                            .user(userRepository.getById(x.longValue()))
                            .checkInStatus(AttendanceStatus.NONE)
                            .checkOutStatus(AttendanceStatus.NONE)
                            .build());
        }

        return attendanceData;
    }


}
