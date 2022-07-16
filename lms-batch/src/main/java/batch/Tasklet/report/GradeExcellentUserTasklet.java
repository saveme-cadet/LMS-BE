package batch.Tasklet.report;

import com.savelms.core.calendar.domain.entity.Calendar;
import com.savelms.core.calendar.domain.repository.CalendarRepository;
import com.savelms.core.monthreport.MonthReport;
import com.savelms.core.monthreport.MonthReportRepository;
import com.savelms.core.statistical.DayStatisticalData;
import com.savelms.core.statistical.DayStatisticalDataRepository;
import com.savelms.core.user.AttendStatus;
import com.savelms.core.user.domain.repository.UserRepository;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.data.domain.Sort;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class GradeExcellentUserTasklet implements Tasklet {

    private final DayStatisticalDataRepository dayStatisticalDataRepository;
    private final MonthReportRepository monthReportRepository;

    public GradeExcellentUserTasklet(DayStatisticalDataRepository dayStatisticalDataRepository,
                                     MonthReportRepository monthReportRepository) {
        this.dayStatisticalDataRepository = dayStatisticalDataRepository;
        this.monthReportRepository = monthReportRepository;
    }

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        List<MonthReport> monthReports = makeGradeBestUsers();
        monthReportRepository.saveAll(monthReports);
        return RepeatStatus.FINISHED;
    }

    private List<MonthReport> makeGradeBestUsers() {
        List<MonthReport> monthReportList = new ArrayList<>();
        List<DayStatisticalData> dayStatisticalData = (List<DayStatisticalData>) dayStatisticalDataRepository
                .findAll(Sort.by(Sort.Direction.DESC, "attendanceScore"))
                .stream().
                filter(dayStatisticalData1 ->
                        dayStatisticalData1
                                .getCalendar()
                                .getDate()
                                .equals(LocalDate.now()))
                .collect(Collectors.toList());

        for (int i = 1; i < 4; i++){
            monthReportList.add(MonthReport.builder()
                            .UserName(dayStatisticalData.get(i).getUser().getUsername())
                            .UserNickName(dayStatisticalData.get(i).getUser().getNickname())
                            .grade(i)
                    .build());
        }
        return monthReportList;
    }

}
