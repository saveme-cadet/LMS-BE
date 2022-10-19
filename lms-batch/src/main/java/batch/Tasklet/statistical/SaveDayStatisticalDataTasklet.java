package batch.Tasklet.statistical;


import com.savelms.core.calendar.domain.entity.Calendar;
import com.savelms.core.calendar.domain.repository.CalendarRepository;
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
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

public class SaveDayStatisticalDataTasklet implements Tasklet {

    private final CalendarRepository calendarRepository;
    private final DayStatisticalDataRepository dayStatisticalDataRepository;
    private final UserRepository userRepository;

    public SaveDayStatisticalDataTasklet(CalendarRepository calendarRepository,
                                         DayStatisticalDataRepository dayStatisticalDataRepository,
                                         UserRepository userRepository) {
        this.calendarRepository = calendarRepository;
        this.dayStatisticalDataRepository = dayStatisticalDataRepository;
        this.userRepository = userRepository;
    }


    @Override
    public RepeatStatus execute(StepContribution contribution,
                                ChunkContext chunkContext)
            throws Exception {

        List<DayStatisticalData> dayStatisticalDataRepositories = createDayStatisticalData();

        Collections.shuffle(dayStatisticalDataRepositories);
        dayStatisticalDataRepository.saveAll(dayStatisticalDataRepositories);
        return RepeatStatus.FINISHED;

    }

    private List<DayStatisticalData> createDayStatisticalData (){
        List<DayStatisticalData> data = new ArrayList<>();

        // user 에서 참석하는 사람 뽑아내기
        Stream<Long> attendUser = userRepository
                .findAllByAttendStatus(AttendStatus.PARTICIPATED)
                .stream()
                .map(x -> x.getId());
        Long[] attendUserList = attendUser.toArray(Long[]::new);

        // calendar에서 요일 뽑아내기calendar
        final Calendar day = calendarRepository.findAllByDate(LocalDate.now().minusDays(1));
        final Calendar nowDay = calendarRepository.findAllByDate(LocalDate.now());

        if (LocalDate.now().getDayOfMonth() == 1) {
            for (Long x : attendUserList) {
                if (!dayStatisticalDataRepository.existsByuser_idAndCalendar_id(x, nowDay.getId())) {
                    data.add(DayStatisticalData.builder()
                            .absentScore((double) 0)
                            .attendanceScore((double) 0)
                            .user(userRepository.getById(x))
                            .todoSuccessRate((double) 0)
                            .weekAbsentScore((double) 0)
                            .calendar(nowDay)
                            .totalScore((double) 0)
                            .studyTimeScore((double) 0)
                            .build());
                }
            }
        } else {
            for (Long x : attendUserList) {
                if (!dayStatisticalDataRepository.existsByuser_idAndCalendar_id(x, nowDay.getId())) {
                    if (dayStatisticalDataRepository.existsByuser_idAndCalendar_id(x, day.getId())) {
                        List<DayStatisticalData> dayStatisticalData = dayStatisticalDataRepository.findByuser_idAndCalendar_id(x, day.getId());
                        data.add(DayStatisticalData.builder()
                                .absentScore(dayStatisticalData.get(0).getAbsentScore())
                                .attendanceScore(dayStatisticalData.get(0).getAttendanceScore())
                                .user(userRepository.getById(x))
                                .todoSuccessRate(dayStatisticalData.get(0).getTodoSuccessRate())
                                .weekAbsentScore(dayStatisticalData.get(0).getWeekAbsentScore())
                                .calendar(nowDay)
                                .totalScore(dayStatisticalData.get(0).getTotalScore())
                                .studyTimeScore(dayStatisticalData.get(0).getStudyTimeScore())
                                .build());
                    } else {
                        data.add(DayStatisticalData.builder()
                                .absentScore((double) 0)
                                .attendanceScore((double) 0)
                                .user(userRepository.getById(x))
                                .todoSuccessRate((double) 0)
                                .weekAbsentScore((double) 0)
                                .calendar(nowDay)
                                .totalScore((double) 0)
                                .studyTimeScore((double) 0)
                                .build());
                    }
                }
            }

        }

        return  data;

        //합쳐서 data 에 넣기

        //TODO: 매달 점수 발표하면서 점수 초기화 하기
    }
}