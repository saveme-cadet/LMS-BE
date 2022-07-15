package com.savelms.api.study_time.service;

import com.savelms.api.study_time.dto.StudyTimeResponse;
import com.savelms.api.study_time.dto.StudyingUserResponse;
import com.savelms.api.study_time.dto.UpdateStudyTimeRequest;
import com.savelms.core.exception.StudyTimeNotFoundException;
import com.savelms.core.study_time.domain.entity.StudyTime;
import com.savelms.core.study_time.domain.repository.StudyTimeRepository;
import com.savelms.core.user.domain.entity.User;
import com.savelms.core.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class StudyTimeService {

    private final UserRepository userRepository;
    private final StudyTimeRepository studyTimeRepository;


    /**
     * 생성
     * */
    @Transactional
    public StudyTimeResponse startStudy(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저입니다."));

        StudyTime studyTime = StudyTime.of(user);
        studyTimeRepository.save(studyTime);
        
        return new StudyTimeResponse(studyTime);
    }


    /**
     * 조회
     * */
    public List<StudyTimeResponse> getStudyTimes(String username) {
        List<StudyTime> studyTimes = studyTimeRepository.findByUsername(username)
                .orElseThrow(() -> new StudyTimeNotFoundException("존재하는 공부 내역이 없습니다."));


        return studyTimes.stream()
                .map(StudyTimeResponse::new)
                .collect(Collectors.toList());
    }

    public List<StudyTimeResponse> getTodayStudyTimes(String username) {
        String createdDate = DateTimeFormatter.ofPattern(StudyTime.CREATED_DATE_FORMAT).format(LocalDateTime.now());

        System.out.println(username);

        List<StudyTime> studyTimes = studyTimeRepository.findByUsernameAndCreatedDate(username, createdDate)
                .orElseThrow(() -> new StudyTimeNotFoundException("존재하는 공부 내역이 없습니다."));

        return studyTimes.stream()
                .map(StudyTimeResponse::new)
                .collect(Collectors.toList());
    }

    public List<StudyTimeResponse> getStudyTimesByDate(String username, String createdDate) throws ParseException {
        validateDateFormat(createdDate);

        List<StudyTime> studyTimes = studyTimeRepository.findByUsernameAndCreatedDate(username, createdDate)
                .orElseThrow(() -> new StudyTimeNotFoundException("존재하는 공부 내역이 없습니다."));

        return studyTimes.stream()
                .map(StudyTimeResponse::new)
                .collect(Collectors.toList());
    }

    public List<StudyingUserResponse> getStudyingUser() {
        List<StudyTime> studyTimes = studyTimeRepository.findByIsStudying(true)
                .orElseThrow(() -> new StudyTimeNotFoundException("공부 중인 회원이 없습니다."));

        return studyTimes.stream()
                .map(StudyingUserResponse::new)
                .collect(Collectors.toList());
    }

    private Double getStudyScore(LocalDateTime beginTime, LocalDateTime endTime) {
        double second = (double) Duration.between(beginTime, endTime).getSeconds();
        double studyTimeScore = second / (8 * 60 * 60);

        return Math.round(studyTimeScore * 100) / 100.0 ;
    }

    private void validateDateFormat(String createAt) throws ParseException {
        SimpleDateFormat dateFormatParser = new SimpleDateFormat(StudyTime.CREATED_DATE_FORMAT); //검증할 날짜 포맷 설정
        dateFormatParser.setLenient(false); //false일경우 처리시 입력한 값이 잘못된 형식일 시 오류가 발생
        dateFormatParser.parse(createAt); //대상 값 포맷에 적용되는지 확인
    }


    /**
     * 수정
     * */
    @Transactional
    public List<StudyTimeResponse> endStudy(String username) {
        List<StudyTime> studyTimes = studyTimeRepository.findByUsernameAndIsStudying(username, true)
                .orElseThrow(() -> {throw new StudyTimeNotFoundException("공부 중이 아닙니다.");});

        studyTimes.forEach(StudyTime::endStudyTime);

        return studyTimes.stream()
                .map(StudyTimeResponse::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public StudyTimeResponse updateStudyTime(Long studyTimeId, UpdateStudyTimeRequest request) {
        StudyTime studyTime = studyTimeRepository.findById(studyTimeId)
                .orElseThrow(() -> new StudyTimeNotFoundException("존재하는 공부 내역이 없습니다."));

        String date = studyTime.getCreatedAt().format(DateTimeFormatter.ofPattern(StudyTime.CREATED_DATE_FORMAT));

        LocalDateTime beginTime = stringToLocalDateTime(date + " " + request.getBeginTime());
        LocalDateTime endTime = stringToLocalDateTime(date + " " + request.getEndTime());

        studyTime.updateStudyTime(beginTime, endTime);

        return new StudyTimeResponse(studyTime);
    }

    private LocalDateTime stringToLocalDateTime(String dateTime) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        return LocalDateTime.parse(dateTime, dateTimeFormatter);
    }


    /**
     * 삭제
     * */
    @Transactional
    public void deleteStudyTime(Long studyTimeId) {
        StudyTime studyTime = studyTimeRepository.findById(studyTimeId)
                .orElseThrow(() -> new StudyTimeNotFoundException("존재하는 공부 내역이 없습니다."));

        studyTimeRepository.delete(studyTime);
    }

}