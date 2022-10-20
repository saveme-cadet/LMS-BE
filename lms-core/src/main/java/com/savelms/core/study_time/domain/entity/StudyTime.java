package com.savelms.core.study_time.domain.entity;

import com.savelms.core.BaseEntity;
import com.savelms.core.calendar.domain.entity.Calendar;
import com.savelms.core.exception.ExceptionStatus;
import com.savelms.core.exception.study_time.StudyTimeException;
import com.savelms.core.user.domain.entity.User;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

@Getter
@Setter
@Builder
@Entity
@Table(name = "STUDY_TIME")
@AllArgsConstructor
public class StudyTime extends BaseEntity {

    public static final String TIME_FORMAT = "HH:mm:ss";
    public static final String DATE_FORMAT = "yyyy-MM-dd";

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="STUDY_TIME_ID")
    private Long id;

    @Column(nullable = false)
    private LocalDateTime beginTime;

    @Column(nullable = false)
    private LocalDateTime endTime;

    @Column(nullable = false)
    private String finalStudyTime;

    @Column(nullable = false)
    private Boolean isStudying;

    @Column(nullable = false)
    private Double studyScore;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="USER_ID", nullable = false, updatable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CALENDAR_ID", nullable = false)
    private Calendar calendar;


    protected StudyTime() {}

    public static StudyTime of(User user, Calendar calendar) {
        return StudyTime.builder()
                .user(user)
                .calendar(calendar)
                .beginTime(LocalDateTime.now())
                .endTime(LocalDateTime.now().with(LocalTime.MIN))
                .isStudying(true)
                .studyScore(0D)
                .finalStudyTime("00:00:00")
                .build();
    }

    public static StudyTime ofBeforeDailyConfig(User user, Calendar calendar) {
        return StudyTime.builder()
                .user(user)
                .calendar(calendar)
                .beginTime(LocalDateTime.now())
                .endTime(LocalDateTime.of(LocalDateTime.now().plusDays(1).toLocalDate(), LocalTime.of(00,00,00)))
                .isStudying(true)
                .studyScore(0D)
                .finalStudyTime("00:00:00")
                .build();
    }

    public static StudyTime of (
            User user,
            Calendar calendar,
            LocalDateTime beginTime,
            LocalDateTime endTime)
    {
        return StudyTime.builder()
                .user(user)
                .calendar(calendar)
                .beginTime(beginTime)
                .endTime(endTime)
                .isStudying(false)
                .studyScore(StudyTime.getStudyScore(beginTime, endTime))
                .finalStudyTime(StudyTime.getFinalStudyTime(beginTime, endTime))
                .build();
    }

    public void setCalendar(Calendar calendar) {
        this.calendar = calendar;
    }

    public void updateStudyTime(LocalDateTime beginTime, LocalDateTime endTime) {
        validateStudyTime(beginTime, endTime);

        this.beginTime = beginTime;
        this.endTime = endTime;
        this.studyScore = getStudyScore(this.beginTime, this.endTime);
        this.finalStudyTime = getFinalStudyTime(this.beginTime, this.endTime);
    }

    public void endStudyTime() {
        this.endTime = LocalDateTime.now();
        this.isStudying = false;
        this.studyScore = getStudyScore(this.beginTime, this.endTime);
        this.finalStudyTime = getFinalStudyTime(this.beginTime, this.endTime);
    }

    public static Double getStudyScore(LocalDateTime beginTime, LocalDateTime endTime) {
        double second = (double) Duration.between(beginTime, endTime).getSeconds();

        double studyTimeScore = second / (8 * 60 * 60);

        double score = Math.round(studyTimeScore * 100) / 100.0;
        return score >= 0 ? score : 0.0;
    }

    public static String getFinalStudyTime(LocalDateTime beginTime, LocalDateTime endTime) {
        Duration between = Duration.between(beginTime, endTime);

        return String.format("%02d:%02d:%02d",
                between.toHours(),
                between.toMinutesPart(),
                between.toSecondsPart());
    }

    private void validateStudyTime(LocalDateTime beginTime, LocalDateTime endTime) {
        Duration between = Duration.between(beginTime, endTime);

        if (endTime.isBefore(beginTime)) {
            throw new StudyTimeException(ExceptionStatus.STUDY_TIME_END_LESS_THEN_BEGIN);
        } else if (between.toHours() >= 24) {
            throw new StudyTimeException(ExceptionStatus.STUDY_TIME_OVER_24_HOURS);
        } else if (!endTime.toLocalDate().isEqual(LocalDate.now())) {
            throw new StudyTimeException(ExceptionStatus.STUDY_TIME_ONLY_TODAY);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StudyTime studyTime = (StudyTime) o;
        return id != null && id.equals(studyTime.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
