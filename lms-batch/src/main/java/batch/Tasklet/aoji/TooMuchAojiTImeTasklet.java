package batch.Tasklet.aoji;

import com.savelms.core.calendar.domain.repository.CalendarRepository;
import com.savelms.core.statistical.DayStatisticalDataRepository;
import com.savelms.core.study_time.domain.repository.StudyTimeRepository;
import com.savelms.core.user.AttendStatus;
import com.savelms.core.user.domain.repository.UserRepository;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

import java.util.stream.Stream;

public class TooMuchAojiTImeTasklet implements Tasklet {

    private final CalendarRepository calendarRepository;
    private final UserRepository userRepository;
    private final StudyTimeRepository studyTimeRepository;
    private final DayStatisticalDataRepository dayStatisticalDataRepository;

    public TooMuchAojiTImeTasklet(CalendarRepository calendarRepository,
                                  UserRepository userRepository,
                                  StudyTimeRepository studyTimeRepository,
                                  DayStatisticalDataRepository dayStatisticalDataRepository) {
        this.calendarRepository = calendarRepository;
        this.userRepository = userRepository;
        this.studyTimeRepository = studyTimeRepository;
        this.dayStatisticalDataRepository = dayStatisticalDataRepository;
    }

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        Stream<Long> attendUser = userRepository
                .findAllByAttendStatus(AttendStatus.PARTICIPATED)
                .stream()
                .map(x -> x.getId());
        Long[] attendUserList = attendUser.toArray(Long[]::new);
        
        return null;
    }

}
