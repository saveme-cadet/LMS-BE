package batch.job.daliycheck;

import batch.Tasklet.attendance.CheckOutTasklet;
import com.savelms.core.attendance.domain.repository.AttendanceRepository;
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
public class CheckOutConfig {
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final EntityManagerFactory entityManagerFactory;
    private final AttendanceRepository attendanceRepository;
    private final CalendarRepository calendarRepository;
    private final UserRepository userRepository;
    private final DayStatisticalDataRepository dayStatisticalDataRepository;

    public CheckOutConfig(JobBuilderFactory jobBuilderFactory,
                          StepBuilderFactory stepBuilderFactory,
                          EntityManagerFactory entityManagerFactory,
                          AttendanceRepository attendanceRepository,
                          CalendarRepository calendarRepository,
                          UserRepository userRepository,
                          DayStatisticalDataRepository dayStatisticalDataRepository) {
        this.jobBuilderFactory = jobBuilderFactory;
        this.stepBuilderFactory = stepBuilderFactory;
        this.entityManagerFactory = entityManagerFactory;
        this.attendanceRepository = attendanceRepository;
        this.calendarRepository = calendarRepository;
        this.userRepository = userRepository;
        this.dayStatisticalDataRepository = dayStatisticalDataRepository;
    }

    // 18시 실행
    @Bean
    public Job CheckInCheckOutJob() throws Exception {
        return this.jobBuilderFactory.get("CheckOutConfig")
                .incrementer(new RunIdIncrementer())
                .start(checkOutStep())
                .build();
    }

    @Bean
    public Step checkOutStep() {
        return this.stepBuilderFactory.get("checkOutStep")
                .tasklet(new CheckOutTasklet(attendanceRepository,
                        calendarRepository,
                        userRepository,
                        dayStatisticalDataRepository
                ))
                .build();
    }


}