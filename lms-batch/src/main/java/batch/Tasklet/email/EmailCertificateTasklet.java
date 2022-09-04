package batch.Tasklet.email;

import com.savelms.core.user.domain.entity.User;
import com.savelms.core.user.domain.repository.UserRepository;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

import java.time.LocalDateTime;
import java.util.List;

public class EmailCertificateTasklet implements Tasklet {

    private final UserRepository userRepository;

    public EmailCertificateTasklet(UserRepository userRepository) {
        this.userRepository = userRepository;

    }

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {


        List<User> users = userRepository
                .findAllByEmailAuth(false);

        for (User x : users) {
            if (LocalDateTime.now().minusDays(2).isBefore(x.getCreatedAt())) {
                userRepository.delete(x);
            }

        }
        return RepeatStatus.FINISHED;
    }

}
