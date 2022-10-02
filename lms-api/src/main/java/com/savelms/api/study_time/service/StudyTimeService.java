package com.savelms.api.study_time.service;

import com.savelms.api.statistical.service.DayStatisticalDataService;
import com.savelms.api.study_time.dto.StudyTimeResponse;
import com.savelms.api.study_time.dto.StudyingUserResponse;
import com.savelms.api.study_time.dto.UpdateStudyTimeRequest;
import com.savelms.core.calendar.domain.entity.Calendar;
import com.savelms.core.calendar.domain.repository.CalendarRepository;
import com.savelms.core.exception.StudyTimeNotFoundException;
import com.savelms.core.statistical.DayStatisticalData;
import com.savelms.core.statistical.DayStatisticalDataRepository;
import com.savelms.core.study_time.domain.entity.StudyTime;
import com.savelms.core.study_time.domain.repository.StudyTimeRepository;
import com.savelms.core.user.domain.entity.User;
import com.savelms.core.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class StudyTimeService {

    private final UserRepository userRepository;
    private final StudyTimeRepository studyTimeRepository;
    private final CalendarRepository calendarRepository;
    private final DayStatisticalDataRepository dayStatDataRepository;

    @Transactional
    public StudyTimeResponse startStudy(String apiId) {
        User user = userRepository.findByApiId(apiId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저입니다."));

        Calendar calendar = calendarRepository.findAllByDate(LocalDate.now());

        StudyTime studyTime = StudyTime.of(user, calendar);
        studyTimeRepository.save(studyTime);
        
        return StudyTimeResponse.from(studyTime);
    }

    @Transactional
    public void dividStudy(String apiId,
                           LocalDate date,
                           LocalDateTime beginTime,
                           LocalDateTime endTime,
                           Double studyScore,
                           String finalStudyTime ) {
        User user = userRepository.findByApiId(apiId)
                .orElseThrow( () -> new IllegalArgumentException("존재하지 않는 유저입니다."));

        Calendar calendar = calendarRepository.findAllByDate(date);

        StudyTime studyTime = StudyTime.of(user,
                calendar,
                beginTime,
                endTime,
                studyScore,
                finalStudyTime);
        System.out.println("beginTime = " + beginTime + " endTime = " + endTime + " studyScore = " + studyScore + " finalStudyScore " +finalStudyTime);
        studyTimeRepository.save(studyTime);
    }


    public List<StudyTimeResponse> getStudyTimes(String apiId) {
        List<StudyTime> studyTimes = studyTimeRepository.findByUserApiId(apiId);

        return studyTimes.stream()
                .map(StudyTimeResponse::from)
                .collect(Collectors.toList());
    }

    public List<StudyTimeResponse> getTodayStudyTimes(String apiId) {
        List<StudyTime> studyTimes = studyTimeRepository.findByUserApiIdAndDate(apiId, LocalDate.now());

        return studyTimes.stream()
                .map(StudyTimeResponse::from)
                .collect(Collectors.toList());
    }

    public List<StudyTimeResponse> getStudyTimesByDate(String apiId, LocalDate date) {

        //List<StudyTime> studyTimes = studyTimeRepository.findByUserApiIdAndDate(userRepository.findByApiId(apiId).get().getId(), date);

        List<StudyTime> studyTimes = studyTimeRepository.findByUserIdAndCalendarId(userRepository.findByApiId(apiId).get().getId()
                , calendarRepository.findAllByDate(date).getId());

        return studyTimes.stream()
                .map(StudyTimeResponse::from)
                .collect(Collectors.toList());
    }

    public List<StudyingUserResponse> getStudyingUser(String userId) {
        List<StudyTime> studyTimes = studyTimeRepository.findByIsStudying(userId, true)
                .orElseThrow(() -> new StudyTimeNotFoundException("공부 중인 회원이 없습니다."));

        return studyTimes.stream()
                .map(StudyingUserResponse::new)
                .collect(Collectors.toList());
    }


    @Transactional
    public StudyTimeResponse endStudy(String apiId) {
        List<StudyTime> studyTimes = studyTimeRepository.findByUserApiIdAndIsStudying(apiId, true);
        DayStatisticalData dayStatData = dayStatDataRepository.findByApiIdAndDate(apiId, LocalDate.now())
                .orElseThrow(() -> new EntityNotFoundException("존재하는 통계 내역이 없습니다."));

        if (studyTimes.isEmpty()) {
            throw new StudyTimeNotFoundException("아직 스터디를 시작하지 않았습니다.");
        }

        StudyTime studyTime = studyTimes.get(0);
        studyTime.endStudyTime();

        Double studyScore = StudyTime.getStudyScore(studyTime.getBeginTime(), studyTime.getEndTime());
        dayStatData.updateStudyTimeScore(studyScore);

        return StudyTimeResponse.from(studyTime);
    }

    @Transactional
    public StudyTimeResponse updateStudyTime(String apiId, Long studyTimeId, UpdateStudyTimeRequest request) {
        StudyTime studyTime = studyTimeRepository.findById(studyTimeId)
                .orElseThrow(() -> new StudyTimeNotFoundException("존재하는 공부 내역이 없습니다."));

        LocalDate requestedEndTime = request.getEndTime().toLocalDate();
        Double oldStudyScore = studyTime.getStudyScore();
        LocalDate studiedDate = studyTime.getEndTime().toLocalDate();
        Double newStudyScore = 0.0;

        if (request.getBeginTime().getDayOfMonth() != request.getEndTime().getDayOfMonth()) {
            newStudyScore = StudyTime.getStudyScore(request.getBeginTime(), LocalDateTime.of(request.getBeginTime().toLocalDate(), LocalTime.of(23,59,59)));

            final Optional<StudyTime> studyTime1 = studyTimeRepository.findAllById(studyTimeId);
            Double finalNewStudyScore = newStudyScore;
            studyTime1.ifPresent(studyTime2 -> {
                studyTime2.setBeginTime(request.getBeginTime());
                studyTime2.setEndTime(LocalDateTime.of(request.getBeginTime().toLocalDate(), LocalTime.of(23,59,59)));
                studyTime2.setStudyScore(finalNewStudyScore);
                studyTime2.setFinalStudyTime(StudyTime.getFinalStudyTime(request.getBeginTime(), LocalDateTime.of(request.getBeginTime().toLocalDate(), LocalTime.of(23,59,59))));
                studyTimeRepository.save(studyTime2);
            });

            Double secondScore = StudyTime.getStudyScore(
                    LocalDateTime.of(
                            request.getEndTime().toLocalDate(),
                            LocalTime.of(0,0,0)),
                    request.getEndTime());

            dividStudy(apiId,
                    request.getEndTime().toLocalDate(),
                    LocalDateTime.of(request.getEndTime().toLocalDate(), LocalTime.of(0,0,0)),
                    request.getEndTime(),
                    secondScore,
                    StudyTime.getFinalStudyTime(LocalDateTime.of(request.getEndTime().toLocalDate(), LocalTime.of(0,0,0)), request.getEndTime())
                    );
            newStudyScore += secondScore;
        } else {
            newStudyScore = StudyTime.getStudyScore(request.getBeginTime(), request.getEndTime());
            final Optional<StudyTime> studyTime1 = studyTimeRepository.findAllById(studyTimeId);
            Double finalNewStudyScore1 = newStudyScore;
            studyTime1.ifPresent(studyTime2 -> {
                studyTime2.setBeginTime(request.getBeginTime());
                studyTime2.setEndTime(request.getEndTime());
                studyTime2.setStudyScore(finalNewStudyScore1);
                studyTime2.setFinalStudyTime(StudyTime.getFinalStudyTime(request.getBeginTime(), request.getEndTime()));
                studyTimeRepository.save(studyTime2);
            });
        }


        if (!studiedDate.isEqual(requestedEndTime)) {
            DayStatisticalData dayStatData = dayStatDataRepository.findByApiIdAndDate(apiId, studiedDate)
                    .orElseThrow(() -> new EntityNotFoundException("존재하는 통계 내역이 없습니다."));
            DayStatisticalData prevDayData = dayStatDataRepository.findByApiIdAndDate(apiId, studiedDate.minusDays(1))
                    .orElseThrow(() -> new EntityNotFoundException("존재하는 통계 내역이 없습니다."));
            DayStatisticalData nextDayData = dayStatDataRepository.findByApiIdAndDate(apiId, studiedDate.plusDays(1))
                    .orElseThrow(() -> new EntityNotFoundException("존재하는 통계 내역이 없습니다."));

            if (requestedEndTime.isBefore(studiedDate)) {
                prevDayData.updateStudyTimeScore(studyTime.getStudyScore());

            } else {
                dayStatData.updateStudyTimeScore(oldStudyScore * -1);
                nextDayData.updateStudyTimeScore(oldStudyScore);
                studiedDate = studiedDate.plusDays(1);
            }
            studyTime.setCalendar(calendarRepository.findAllByDate(requestedEndTime));
        }
        final Optional<DayStatisticalData> dayStatisticalData = dayStatDataRepository.findAllByUser_idAndCalendar_id(userRepository.findByApiId(apiId).get().getId(),
                calendarRepository.findAllByDate(studiedDate).getId());
        Double finalNewStudyScore2 = newStudyScore;
        dayStatisticalData.ifPresent(dayStatisticalData1 -> {
            dayStatisticalData1.setStudyTimeScore(dayStatisticalData1.getStudyTimeScore() - oldStudyScore + finalNewStudyScore2);
            dayStatisticalData1.setTotalScore(dayStatisticalData1.getTotalScore() - oldStudyScore + finalNewStudyScore2);
        });
        //dayStatDataRepository.bulkUpdateStudyTimeScore(apiId, differenceScore, studiedDate);

        StudyTime studyTime3 = studyTimeRepository.findById(studyTimeId)
                    .orElseThrow(() -> new StudyTimeNotFoundException("존재하는 공부 내역이 없습니다."));
        return StudyTimeResponse.from(studyTime3);
    }


    @Transactional
    public void deleteStudyTime(Long studyTimeId) {
        StudyTime studyTime = studyTimeRepository.findById(studyTimeId)
                .orElseThrow(() -> new StudyTimeNotFoundException("존재하는 공부 내역이 없습니다."));

        studyTimeRepository.delete(studyTime);
    }
}
