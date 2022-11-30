package batch.Tasklet.aoji;

import com.savelms.core.attendance.domain.AttendanceStatus;
import com.savelms.core.attendance.domain.entity.Attendance;
import com.savelms.core.calendar.domain.entity.Calendar;
import com.savelms.core.calendar.domain.repository.CalendarRepository;
import com.savelms.core.statistical.DayStatisticalData;
import com.savelms.core.statistical.DayStatisticalDataRepository;
import com.savelms.core.study_time.domain.entity.StudyTime;
import com.savelms.core.study_time.domain.repository.StudyTimeRepository;
import com.savelms.core.user.AttendStatus;
import com.savelms.core.user.domain.repository.UserRepository;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.stream.Stream;

public class TooMuchAojiTImeTasklet implements Tasklet {

    private final CalendarRepository calendarRepository;
    private final UserRepository userRepository;
    private final StudyTimeRepository studyTimeRepository;
    private final DayStatisticalDataRepository dayStatisticalDataRepository;

    public TooMuchAojiTImeTasklet(CalendarRepository calendarRepository,
                                  UserRepository userRepository,
                                  StudyTimeRepository studyTimeRepository,
                                  DayStatisticalDataRepository dayStatisticalDataRepository) {
        this.calendarRepository = calendarRepository;
        this.userRepository = userRepository;
        this.studyTimeRepository = studyTimeRepository;
        this.dayStatisticalDataRepository = dayStatisticalDataRepository;
    }

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        Stream<Long> attendUser = userRepository
                .findAllByAttendStatus(AttendStatus.PARTICIPATED)
                .stream()
                .map(x -> x.getId());
        Long[] attendUserList = attendUser.toArray(Long[]::new);

        final Calendar day = calendarRepository.findAllByDate(LocalDate.now());
        for (Long x : attendUserList) {
            if (studyTimeRepository.existsByUserIdAndCalendarIdAndIsStudying(x, day.getId(), true)) {
                Optional<StudyTime> studyTime = studyTimeRepository.findByUserIdAndCalendarIdAndIsStudying(x, day.getId(), true);
                final LocalDateTime endStudyTime = LocalDateTime.now();
                studyTime.ifPresent(studyTime1 -> {
                    studyTime1.setEndTime(endStudyTime);
                    studyTime1.setFinalStudyTime(StudyTime.getFinalStudyTime(studyTime1.getBeginTime(), endStudyTime));
                    studyTime1.setIsStudying(false);
                    studyTime1.setStudyScore(StudyTime.getStudyScore(studyTime1.getBeginTime(), endStudyTime));
                });
                Calendar calendar = calendarRepository.findAllByDate(LocalDate.now().plusDays(1));
                StudyTime studyTime3 = StudyTime.ofBeforeDailyConfig(userRepository.getById(x), calendar);
                studyTimeRepository.save(studyTime3);
                final double score = studyTime.get().getStudyScore();

                Optional<DayStatisticalData> dayStatisticalData = dayStatisticalDataRepository.findAllByUser_idAndCalendar_id(x, day.getId());
                dayStatisticalData.ifPresent(dayStatisticalData1 -> {
                    dayStatisticalData1.setStudyTimeScore(dayStatisticalData1.getStudyTimeScore() + score);
                    dayStatisticalData1.setTotalScore(dayStatisticalData1.getAbsentScore() - dayStatisticalData1.getStudyTimeScore()); // TODO: ? 왜 아오지 시간을 빼는 거지?
                    dayStatisticalDataRepository.save(dayStatisticalData1);
                });
            }
        }


        return RepeatStatus.FINISHED;
    }

}
