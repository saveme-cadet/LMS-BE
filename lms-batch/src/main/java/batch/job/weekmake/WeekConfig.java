package batch.job.weekmake;

import batch.Tasklet.statistical.WeekAbsentScoreInitTasklet;
import com.savelms.core.calendar.domain.repository.CalendarRepository;
import com.savelms.core.statistical.DayStatisticalDataRepository;
import com.savelms.core.user.domain.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class WeekConfig {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final DayStatisticalDataRepository dayStatisticalDataRepository;
    private final UserRepository userRepository;
    private final CalendarRepository calendarRepository;

    public WeekConfig(JobBuilderFactory jobBuilderFactory,
                      StepBuilderFactory stepBuilderFactory,
                      DayStatisticalDataRepository dayStatisticalDataRepository,
                      UserRepository userRepository,
                      CalendarRepository calendarRepository) {
        this.jobBuilderFactory = jobBuilderFactory;
        this.stepBuilderFactory = stepBuilderFactory;
        this.dayStatisticalDataRepository = dayStatisticalDataRepository;
        this.userRepository = userRepository;
        this.calendarRepository = calendarRepository;
    }

    @Bean
    public Job WeekJob() throws Exception {
        return this.jobBuilderFactory.get("WeekJob")
                .incrementer(new RunIdIncrementer())
                .start(this.WeekStep())
                .build();
    }


    /*
    매주 월요일 week_absent_score 0점으로 초기화
     */

    @Bean
    public Step WeekStep() throws Exception {
        return this.stepBuilderFactory.get("WeekStep")
                .tasklet(new WeekAbsentScoreInitTasklet(dayStatisticalDataRepository,
                        userRepository,
                        calendarRepository))
                .build();
    }
}
