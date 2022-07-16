package batch.Tasklet.report;

import com.savelms.core.calendar.domain.entity.Calendar;
import com.savelms.core.calendar.domain.repository.CalendarRepository;
import com.savelms.core.weekreport.WeekReport;
import com.savelms.core.weekreport.WeekReportRepository;
import com.savelms.core.statistical.DayStatisticalData;
import com.savelms.core.statistical.DayStatisticalDataRepository;
import com.savelms.core.user.AttendStatus;
import com.savelms.core.user.domain.repository.UserRepository;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class CheckDangerUserTasklet implements Tasklet {

    private final DayStatisticalDataRepository dayStatisticalDataRepository;
    private final UserRepository userRepository;
    private final WeekReportRepository weekReportRepository;
    private final CalendarRepository calendarRepository;

    public CheckDangerUserTasklet(DayStatisticalDataRepository dayStatisticalDataRepository,
                                  UserRepository userRepository,
                                  WeekReportRepository weekReportRepository,
                                  CalendarRepository calendarRepository) {
        this.dayStatisticalDataRepository = dayStatisticalDataRepository;
        this.userRepository = userRepository;
        this.weekReportRepository = weekReportRepository;
        this.calendarRepository = calendarRepository;
    }

    @Override
    public RepeatStatus execute(StepContribution contribution,
                                ChunkContext chunkContext)
            throws Exception {

        List<WeekReport> userList = checkUser();
        weekReportRepository.deleteAll();;
        weekReportRepository.saveAll(userList);
        return RepeatStatus.FINISHED;
    }

    private List<WeekReport> checkUser() {
        List<WeekReport> reportList = new ArrayList<>();

        Stream<Long> attendUser = userRepository
                .findAllByAttendStatus(AttendStatus.PARTICIPATED)
                .stream()
                .map(x -> x.getId());
        Long[] attendUserList = attendUser.toArray(Long[]::new);


        // calendar에서 요일 뽑아내기
        final Calendar day = calendarRepository.findAllByDate(LocalDate.now());
        for (Long x : attendUserList) {
            DayStatisticalData dataRepository = dayStatisticalDataRepository.findByuser_idAndCalendar_id(x, day.getId());
            if (dataRepository.getWeekAbsentScore() >= 3) {
                String userName = userRepository.findById(x).get().getUsername();
                String userNickName = userRepository.findById(x).get().getNickname();
                reportList.add(WeekReport.builder()
                        .UserName(userName)
                        .UserNickName(userNickName)
                        .build());
            }
        }

        return reportList;
    }
}
