package batch.Tasklet.statistical;

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

/*
매 주 week absent score 점수 초기화
 */

public class WeekAbsentScoreInitTasklet implements Tasklet {

    private final DayStatisticalDataRepository dayStatisticalDataRepository;
    private final UserRepository userRepository;
    private final CalendarRepository calendarRepository;

    public WeekAbsentScoreInitTasklet(DayStatisticalDataRepository dayStatisticalDataRepository,
                                      UserRepository userRepository,
                                      CalendarRepository calendarRepository) {
        this.dayStatisticalDataRepository = dayStatisticalDataRepository;
        this.userRepository = userRepository;
        this.calendarRepository = calendarRepository;
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
        final Calendar day = calendarRepository.findAllByDate(LocalDate.now());

        for (Long x : attendUserList) {
            Optional<DayStatisticalData> dayStatisticalData = dayStatisticalDataRepository.findAllByUser_idAndCalendar_id(x, day.getId());
            dayStatisticalData.ifPresent(dayStatisticalData1 -> {
                dayStatisticalData1.setWeekAbsentScore(0.0);
                dayStatisticalDataRepository.save(dayStatisticalData1);
            });
        }

        return RepeatStatus.FINISHED;
    }
}
