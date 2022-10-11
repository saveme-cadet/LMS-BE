package batch.Tasklet.statistical;

import com.savelms.core.calendar.domain.entity.Calendar;
import com.savelms.core.calendar.domain.repository.CalendarRepository;
import com.savelms.core.statistical.DayStatisticalData;
import com.savelms.core.statistical.DayStatisticalDataRepository;
import com.savelms.core.user.AttendStatus;
import com.savelms.core.user.domain.entity.User;
import com.savelms.core.user.domain.repository.UserRepository;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class ConfirmTotalScoreTasklet implements Tasklet {
    private final DayStatisticalDataRepository dayStatisticalDataRepository;
    private final UserRepository userRepository;
    private final CalendarRepository calendarRepository;

    public ConfirmTotalScoreTasklet(DayStatisticalDataRepository dayStatisticalDataRepository,
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
        List<User> userList = userRepository.findAllByAttendStatus(AttendStatus.PARTICIPATED);
        Calendar today = calendarRepository.findAllByDate(LocalDate.now());
        for (int i = 0; i < userList.size(); i++){
            Optional<DayStatisticalData> dayStatisticalData = dayStatisticalDataRepository.findAllByUser_idAndCalendar_id(userList.get(i).getId(), today.getId());
            dayStatisticalData.ifPresent(dayStatisticalData1 -> {
                dayStatisticalData1.setTotalScore(dayStatisticalData1.getAbsentScore() - dayStatisticalData1.getStudyTimeScore());
                dayStatisticalDataRepository.save(dayStatisticalData1);
            });
        }

        return RepeatStatus.FINISHED;
    }
}
