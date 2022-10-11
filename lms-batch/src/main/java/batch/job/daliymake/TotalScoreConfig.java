package batch.job.daliymake;

import batch.Tasklet.statistical.ConfirmTotalScoreTasklet;
import batch.Tasklet.statistical.SaveDayStatisticalDataTasklet;
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

import javax.persistence.EntityManagerFactory;

@Slf4j
@Configuration
public class TotalScoreConfig {
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final EntityManagerFactory entityManagerFactory;
    private final DayStatisticalDataRepository dayStatisticalDataRepository;
    private final UserRepository userRepository;
    private final CalendarRepository calendarRepository;

    public TotalScoreConfig(JobBuilderFactory jobBuilderFactory,
                            StepBuilderFactory stepBuilderFactory,
                            EntityManagerFactory entityManagerFactory,
                            DayStatisticalDataRepository dayStatisticalDataRepository,
                            UserRepository userRepository,
                            CalendarRepository calendarRepository) {
        this.jobBuilderFactory = jobBuilderFactory;
        this.stepBuilderFactory = stepBuilderFactory;
        this.entityManagerFactory = entityManagerFactory;
        this.dayStatisticalDataRepository = dayStatisticalDataRepository;
        this.userRepository = userRepository;
        this.calendarRepository = calendarRepository;
    }

    // * 4시간 주기
    @Bean(name = "TotalScoreJob")
    public Job TotalScoreJob() throws Exception {
        return this.jobBuilderFactory.get("TotalScoreJob")
                .incrementer(new RunIdIncrementer())
                .start(this.confirmTotalScoreStep())
                .build();
    }

    @Bean
    public Step confirmTotalScoreStep() {
        return this.stepBuilderFactory.get("confirmTotalScoreStep")
                .tasklet(new ConfirmTotalScoreTasklet(dayStatisticalDataRepository,
                        userRepository,
                        calendarRepository))
                .build();
    }
}
