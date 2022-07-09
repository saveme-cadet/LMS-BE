package com.savelms.api.study_time.service;

import com.savelms.api.study_time.dto.StudyTimeResponse;
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
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class StudyTimeService {

    private final String CREATED_DATE_FORMAT = "yyyy-MM-dd";
    private final UserRepository userRepository;
    private final StudyTimeRepository studyTimeRepository;


    /**
     * 생성
     * */
    @Transactional
    public StudyTimeResponse startStudy(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저입니다."));

        StudyTime studyTime = StudyTime.createStudyTime(user);
        studyTimeRepository.save(studyTime);
        
        return new StudyTimeResponse(studyTime);
    }


    /**
     * 조회
     * */
    public List<StudyTimeResponse> getStudyTimes(Long userId) {
        List<StudyTime> studyTimes = studyTimeRepository.findByUserId(userId)
                .orElseThrow(() -> new StudyTimeNotFoundException("존재하는 공부 내역이 없습니다."));

        return studyTimes.stream()
                .map(StudyTimeResponse::new)
                .collect(Collectors.toList());
    }

    public List<StudyTimeResponse> getTodayStudyTimes(Long userId) {
        String createdDate = DateTimeFormatter.ofPattern(CREATED_DATE_FORMAT).format(LocalDateTime.now());

        List<StudyTime> studyTimes = studyTimeRepository.findByUserIdAndCreatedDate(userId, createdDate)
                .orElseThrow(() -> new StudyTimeNotFoundException("존재하는 공부 내역이 없습니다."));

        return studyTimes.stream()
                .map(StudyTimeResponse::new)
                .collect(Collectors.toList());
    }

    public List<StudyTimeResponse> getStudyTimesByDate(Long userId, String createdDate) throws ParseException {
        validateDateFormat(createdDate);

        List<StudyTime> studyTimes = studyTimeRepository.findByUserIdAndCreatedDate(userId, createdDate)
                .orElseThrow(() -> new StudyTimeNotFoundException("존재하는 공부 내역이 없습니다."));

        return studyTimes.stream()
                .map(StudyTimeResponse::new)
                .collect(Collectors.toList());
    }

    private void validateDateFormat(String createAt) throws ParseException {
        SimpleDateFormat dateFormatParser = new SimpleDateFormat(CREATED_DATE_FORMAT); //검증할 날짜 포맷 설정
        dateFormatParser.setLenient(false); //false일경우 처리시 입력한 값이 잘못된 형식일 시 오류가 발생
        dateFormatParser.parse(createAt); //대상 값 포맷에 적용되는지 확인
    }


    /**
     * 수정
     * */
    @Transactional
    public List<StudyTimeResponse> endStudy(Long userId) {
        List<StudyTime> studyTimes = studyTimeRepository.findByUserIdAndIsStudying(userId, true)
                .orElseThrow(() -> {
                    throw new StudyTimeNotFoundException("공부 중이 아닙니다.");
                });

        studyTimes.forEach(StudyTime::endStudyTime);

        return studyTimes.stream()
                .map(StudyTimeResponse::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public StudyTimeResponse updateStudyTime(Long studyTimeId, UpdateStudyTimeRequest request) {
        StudyTime studyTime = studyTimeRepository.findById(studyTimeId)
                .orElseThrow(() -> new StudyTimeNotFoundException("존재하는 공부 내역이 없습니다."));

        String date = studyTime.getCreatedAt().format(DateTimeFormatter.ofPattern(CREATED_DATE_FORMAT));

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
