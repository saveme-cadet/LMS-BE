package batch.job.certification;

import batch.Tasklet.email.EmailCertificateTasklet;
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
public class MailConfig {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final UserRepository userRepository;

    public MailConfig(JobBuilderFactory jobBuilderFactory,
                      StepBuilderFactory stepBuilderFactory,
                      UserRepository userRepository) {
        this.jobBuilderFactory = jobBuilderFactory;
        this.stepBuilderFactory = stepBuilderFactory;
        this.userRepository = userRepository;
    }

    @Bean
    public Job mailJob() throws Exception {
        return this.jobBuilderFactory.get("mailJob")
                .incrementer(new RunIdIncrementer())
                .start(mailStep())
                .build();
    }

    @Bean
    public Step mailStep() {
        return this.stepBuilderFactory.get("mailStep")
                .tasklet(new EmailCertificateTasklet(
                        userRepository
                ))
                .build();
    }
}
