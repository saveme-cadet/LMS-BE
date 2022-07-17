package batch.job.month;

import batch.Tasklet.report.GradeExcellentUserTasklet;
import com.savelms.core.monthreport.MonthReportRepository;
import com.savelms.core.statistical.DayStatisticalDataRepository;
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
public class ReportMonthConfig {
    /*
    매달 말 출석률 좋은 사람 뽑아내기
     */
    private final DayStatisticalDataRepository dayStatisticalDataRepository;
    private final MonthReportRepository monthReportRepository;
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;


    public ReportMonthConfig(DayStatisticalDataRepository dayStatisticalDataRepository,
                             MonthReportRepository monthReportRepository,
                             JobBuilderFactory jobBuilderFactory,
                             StepBuilderFactory stepBuilderFactory) {
        this.dayStatisticalDataRepository = dayStatisticalDataRepository;
        this.monthReportRepository = monthReportRepository;
        this.jobBuilderFactory = jobBuilderFactory;
        this.stepBuilderFactory = stepBuilderFactory;
    }

    @Bean
    public Job ReportMonthJob(){
        return this.jobBuilderFactory.get("ReportMonthJob")
                .incrementer(new RunIdIncrementer())
                .start(ReportMonthStep())        // 매달 마지막에
                .build();
    }

    /*
     성적 좋은 유저 day_statistial_data에서 매달 마지막에 등수 정해서  Report month table에 저장
     */

    @Bean
    public Step ReportMonthStep() {
        return this.stepBuilderFactory.get("ReportMonthStep")
                .tasklet(new GradeExcellentUserTasklet(dayStatisticalDataRepository,
                        monthReportRepository))
                .build();
    }
}
