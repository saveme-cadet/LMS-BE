package batch.Tasklet;

import batch.validation.DayOfWeek;
import com.savelms.core.attendance.repository.AttendanceRepository;
import com.savelms.core.calendar.domain.entity.Calendar;
import com.savelms.core.calendar.domain.repository.CalendarRepository;
import com.savelms.core.report.Report;
import com.savelms.core.report.ReportRepository;
import com.savelms.core.statistical.DayStatisticalDataRepository;
import com.savelms.core.user.AttendStatus;
import com.savelms.core.user.domain.entity.User;
import com.savelms.core.user.domain.repository.UserRepository;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class CheckDangerUserTasklet implements Tasklet {

    private final DayStatisticalDataRepository dayStatisticalDataRepository;
    private final UserRepository userRepository;
    private final ReportRepository reportRepository;
    private final AttendanceRepository attendanceRepository;
    private final CalendarRepository calendarRepository;

    public CheckDangerUserTasklet(DayStatisticalDataRepository dayStatisticalDataRepository,
                                  UserRepository userRepository,
                                  ReportRepository reportRepository,
                                  AttendanceRepository attendanceRepository,
                                  CalendarRepository calendarRepository) {
        this.dayStatisticalDataRepository = dayStatisticalDataRepository;
        this.userRepository = userRepository;
        this.reportRepository = reportRepository;
        this.attendanceRepository = attendanceRepository;
        this.calendarRepository = calendarRepository;
    }

    @Override
    public RepeatStatus execute(StepContribution contribution,
                                ChunkContext chunkContext)
            throws Exception {

        List<Report> userList = checkUser();
        reportRepository.saveAll(userList);
        return RepeatStatus.FINISHED;
    }

    private List<Report> checkUser() {
        List<Report> reportList = new ArrayList<>();

        List<User> users = userRepository.findAllByAttendStatus(AttendStatus.PARTICIPATED);
        Calendar calendar = calendarRepository.findAllByDate(LocalDate.now());

        int day = DayOfWeek.getDayOfWeek();
        if (day == 1)
            day = 7;
        else
            day -= 1;



        return reportList;
    }
}
