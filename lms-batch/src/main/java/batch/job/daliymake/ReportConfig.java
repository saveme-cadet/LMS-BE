package batch.job.daliymake;

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
public class ReportConfig {
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final DayStatisticalDataRepository dayStatisticalDataRepository;
    private final UserRepository userRepository;

    public ReportConfig(JobBuilderFactory jobBuilderFactory,
                        StepBuilderFactory stepBuilderFactory,
                        DayStatisticalDataRepository dayStatisticalDataRepository,
                        UserRepository userRepository) {
        this.jobBuilderFactory = jobBuilderFactory;
        this.stepBuilderFactory = stepBuilderFactory;
        this.dayStatisticalDataRepository = dayStatisticalDataRepository;
        this.userRepository = userRepository;
    }

    @Bean
    public Job ReportJob(){
        return this.jobBuilderFactory.get("ReportJob")
                .incrementer(new RunIdIncrementer())
                .start(ReportStep())        // 일일에 한번
                .build();
    }

    @Bean
    public Step ReportStep() {
        return this.stepBuilderFactory.get("ReportStep")
                .tasklet()
                .build();
    }


}
