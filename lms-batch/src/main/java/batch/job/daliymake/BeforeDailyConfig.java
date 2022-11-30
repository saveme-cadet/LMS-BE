package batch.job.daliymake;

import batch.Tasklet.aoji.TooMuchAojiTImeTasklet;
import batch.custom.CustomItemReader;
import batch.validation.DayOfWeek;
import com.savelms.core.calendar.DayType;
import com.savelms.core.calendar.domain.entity.Calendar;
import com.savelms.core.calendar.domain.repository.CalendarRepository;
import com.savelms.core.statistical.DayStatisticalDataRepository;
import com.savelms.core.study_time.domain.repository.StudyTimeRepository;
import com.savelms.core.user.domain.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.database.builder.JpaItemWriterBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.persistence.EntityManagerFactory;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Configuration
public class BeforeDailyConfig {
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final EntityManagerFactory entityManagerFactory;
    private final UserRepository userRepository;
    private final CalendarRepository calendarRepository;
    private final StudyTimeRepository studyTimeRepository;
    private final DayStatisticalDataRepository dayStatisticalDataRepository;

    public BeforeDailyConfig(JobBuilderFactory jobBuilderFactory,
                             StepBuilderFactory stepBuilderFactory,
                             EntityManagerFactory entityManagerFactory,
                             UserRepository userRepository,
                             CalendarRepository calendarRepository,
                             StudyTimeRepository studyTimeRepository,
                             DayStatisticalDataRepository dayStatisticalDataRepository) {
        this.jobBuilderFactory = jobBuilderFactory;
        this.stepBuilderFactory = stepBuilderFactory;
        this.entityManagerFactory = entityManagerFactory;
        this.userRepository = userRepository;
        this.calendarRepository = calendarRepository;
        this.studyTimeRepository = studyTimeRepository;
        this.dayStatisticalDataRepository = dayStatisticalDataRepository;
    }

    /*
    전날 23시 59분에 실행
     */
    @Bean(name = "BeforeDailyJob")
    public Job BeforeDailyJob() throws Exception {
        return this.jobBuilderFactory.get("BeforeDailyJob")
                .incrementer(new RunIdIncrementer())
                .start(this.ItemDailyWriterStep())
                .next(this.cutStudyTimeStep())
                .build();
    }


    @Bean
    public Step ItemDailyWriterStep() throws Exception {
        return stepBuilderFactory.get("ItemDailyWriterStep")
                .<Calendar, Calendar>chunk(10)
                .reader(itemReader())
                .writer(jpaItemWriter())
                .build();
    }

    private ItemWriter<Calendar> jpaItemWriter() throws Exception {
        JpaItemWriter<Calendar> itemWriter = new JpaItemWriterBuilder<Calendar>()
                .entityManagerFactory(entityManagerFactory)
                .build();
        itemWriter.afterPropertiesSet();

        return itemWriter;
    }

    private ItemReader<Calendar> itemReader() {
        return new CustomItemReader<>(getItems());
    }


    private List<Calendar> getItems() {
        List<Calendar> items = new ArrayList<>();
        if (DayOfWeek.getDayOfWeek() == 6 || DayOfWeek.getDayOfWeek() == 7)
            items.add(new Calendar(LocalDate.now().plusDays(1),
                    DayType.HOLIDAY
            ));
        else
            items.add(new Calendar(LocalDate.now().plusDays(1),
                    DayType.STUDYDAY
            ));
        return items;
    }

    @Bean
    public Step cutStudyTimeStep() {
        return this.stepBuilderFactory.get("cutStudyTimeStep")
                .tasklet(new TooMuchAojiTImeTasklet(calendarRepository,
                        userRepository,
                        studyTimeRepository,
                        dayStatisticalDataRepository))
                .build();
    }
}
