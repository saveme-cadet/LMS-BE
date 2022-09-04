package batch.job.UserMake;

import batch.Tasklet.user.SaveUserTasklet;
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
public class userConfig {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final UserRepository userRepository;

        public userConfig(JobBuilderFactory jobBuilderFactory,
                          StepBuilderFactory stepBuilderFactory,
                          UserRepository userRepository) {
            this.jobBuilderFactory = jobBuilderFactory;
            this.stepBuilderFactory = stepBuilderFactory;
            this.userRepository = userRepository;
        }

        @Bean
        public Job userJob() throws Exception {
            return this.jobBuilderFactory.get("userJob")
                    .incrementer(new RunIdIncrementer())
                    .start(this.userStep())
                    .build();
        }


    /*
    매주 월요일 week_absent_score 0점으로 초기화
     */

        @Bean
        public Step userStep() throws Exception {
            return this.stepBuilderFactory.get("userStep")
                    .tasklet(new SaveUserTasklet(userRepository))
                    .build();
        }
}
