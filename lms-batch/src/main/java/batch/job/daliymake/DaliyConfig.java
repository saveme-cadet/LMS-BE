package batch.job.daliymake;

import batch.Tasklet.aoji.TooMuchAojiTImeTasklet;
import batch.Tasklet.attendance.AttendanceTasklet;
import batch.Tasklet.statistical.SaveDayStatisticalDataTasklet;
import batch.Tasklet.user.SaveUserTasklet;
import batch.custom.CustomItemReader;
import batch.validation.DayOfWeek;
import com.savelms.core.attendance.domain.repository.AttendanceRepository;

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
public class DaliyConfig {
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final EntityManagerFactory entityManagerFactory;
    private final UserRepository userRepository;
    private final CalendarRepository calendarRepository;
    private final DayStatisticalDataRepository dayStatisticalDataRepository;
    private final AttendanceRepository attendanceRepository;
    private final StudyTimeRepository studyTimeRepository;

    public DaliyConfig(JobBuilderFactory jobBuilderFactory,
                       StepBuilderFactory stepBuilderFactory,
                       EntityManagerFactory entityManagerFactory,
                       UserRepository userRepository,
                       CalendarRepository calendarRepository,
                       DayStatisticalDataRepository dayStatisticalDataRepository,
                       AttendanceRepository attendanceRepository,
                       StudyTimeRepository studyTimeRepository) {
        this.jobBuilderFactory = jobBuilderFactory;
        this.stepBuilderFactory = stepBuilderFactory;
        this.entityManagerFactory = entityManagerFactory;
        this.userRepository = userRepository;
        this.calendarRepository = calendarRepository;
        this.dayStatisticalDataRepository = dayStatisticalDataRepository;
        this.attendanceRepository = attendanceRepository;
        this.studyTimeRepository = studyTimeRepository;
    }

    @Bean
    public Job DaliyJob() throws Exception {
        return this.jobBuilderFactory.get("DaliyJob")
                .incrementer(new RunIdIncrementer())
                .start(this.ItemDailyWriterStep())
//                .next(saveUserStep())
                .next(this.saveDayStatisticalDataStep())
                .start(this.saveAttendanceStep())
                .build();
    }

    // =============================================== calendar make  ============================//

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
        if (DayOfWeek.getDayOfWeek() == 1 || DayOfWeek.getDayOfWeek() == 7)
            items.add(new Calendar(LocalDate.now(),
                    DayType.HOLIDAY
            ));
        else
            items.add(new Calendar(LocalDate.now(),
                    DayType.STUDYDAY
            ));
        return items;
    }


    // =============================================== day_statistical_data ========================//


    /*
    user 목업 데이터 생성
     */

    @Bean
    public Step saveUserStep() {
        return this.stepBuilderFactory.get("saveUserStep")
                .tasklet(new SaveUserTasklet(userRepository))
                .build();
    }



    /*
    score 전날 점수 저장 테이블 생성
     */

    @Bean
    public Step saveDayStatisticalDataStep() {
        return this.stepBuilderFactory.get("saveDayStatisticalDataStep")
                .tasklet(new SaveDayStatisticalDataTasklet(calendarRepository,
                        dayStatisticalDataRepository,
                        userRepository))
                .build();
    }

    /*
    참가 table 생성
     */

    @Bean
    public Step saveAttendanceStep() {
        return this.stepBuilderFactory.get("saveAttendanceStep")
                .tasklet(new AttendanceTasklet(calendarRepository,
                        userRepository,
                        attendanceRepository))
                .build();
    }

    /*
    aoji 시작했다가 안끈 aoji user 수정
     */

    @Bean
    public Step checkAojiTimeStep() {
        return this.stepBuilderFactory.get("checkAojiTimeStep")
                .tasklet(new TooMuchAojiTImeTasklet(calendarRepository,
                        userRepository,
                        studyTimeRepository, dayStatisticalDataRepository))
                .build();
    }

}
