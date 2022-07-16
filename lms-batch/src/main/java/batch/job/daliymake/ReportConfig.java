package batch.job.daliymake;

import batch.Tasklet.report.CheckDangerUserTasklet;
import com.savelms.core.attendance.repository.AttendanceRepository;
import com.savelms.core.calendar.domain.repository.CalendarRepository;
import com.savelms.core.report.ReportRepository;
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
    private final ReportRepository reportRepository;
    private final CalendarRepository calendarRepositor;

    public ReportConfig(JobBuilderFactory jobBuilderFactory,
                        StepBuilderFactory stepBuilderFactory,
                        DayStatisticalDataRepository dayStatisticalDataRepository,
                        UserRepository userRepository,
                        ReportRepository reportRepository,
                        CalendarRepository calendarRepositor) {
        this.jobBuilderFactory = jobBuilderFactory;
        this.stepBuilderFactory = stepBuilderFactory;
        this.dayStatisticalDataRepository = dayStatisticalDataRepository;
        this.userRepository = userRepository;
        this.reportRepository = reportRepository;
        this.calendarRepositor = calendarRepositor;
    }

    @Bean
    public Job ReportJob(){
        return this.jobBuilderFactory.get("ReportJob")
                .incrementer(new RunIdIncrementer())
                .start(ReportStep())        // 일일에 한번
                .build();
    }

    /*
     위험한 유저 day_statistial_data 에서 Report table로 저장
     */

    @Bean
    public Step ReportStep() {
        return this.stepBuilderFactory.get("ReportStep")
                .tasklet(new CheckDangerUserTasklet(dayStatisticalDataRepository,
                        userRepository,
                        reportRepository,
                        calendarRepositor))
                .build();
    }


}
