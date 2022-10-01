package com.savelms.core.study_time.domain.entity;

import com.savelms.core.BaseEntity;
import com.savelms.core.calendar.domain.entity.Calendar;
import com.savelms.core.exception.StudyTimeMeasurementException;
import com.savelms.core.user.domain.entity.User;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
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

@Getter
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

    public void setCalendar(Calendar calendar) {
        this.calendar = calendar;
    }

    public static Double getStudyScore(LocalDateTime beginTime, LocalDateTime endTime) {
        double second = (double) Duration.between(beginTime, endTime).getSeconds();
        double studyTimeScore = second / (8 * 60 * 60);

        double score = Math.round(studyTimeScore * 100) / 100.0;
        return score >= 0 ? score : 0.0;
    }

    public void updateStudyTime(LocalDateTime beginTime, LocalDateTime endTime) {
        validateUpdateStudyTime(beginTime, endTime);

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

    private String getFinalStudyTime(LocalDateTime beginTime, LocalDateTime endTime) {
        Duration between = Duration.between(beginTime, endTime);

        validateStudyTime(beginTime, endTime);
        return String.format("%02d:%02d:%02d",
                between.toHours(),
                between.toMinutesPart(),
                between.toSecondsPart());
    }

    private void validateStudyTime(LocalDateTime beginTime, LocalDateTime endTime) {
        Duration between = Duration.between(beginTime, endTime);

        if (endTime.isBefore(beginTime)) {
            throw new StudyTimeMeasurementException("종료시간이 시작시간보다 작을 수 없습니다.");
        } else if (between.toHours() >= 24) {
            throw new StudyTimeMeasurementException("스터디 시간은 24시간 이상 넘어가면 측정이 불가능합니다.");
        }
    }

    private void validateUpdateStudyTime(LocalDateTime beginTime, LocalDateTime endTime) {
        LocalDateTime beginMinDay = this.beginTime.minusDays(1).withHour(0).withMinute(0).withSecond(0);
        LocalDateTime beginMaxDay = this.beginTime.plusDays(1).withHour(23).withMinute(59).withSecond(59);
        LocalDateTime endMinDay = this.endTime.minusDays(1).withHour(0).withMinute(0).withSecond(0);
        LocalDateTime endMaxDay = this.endTime.plusDays(1).withHour(23).withMinute(59).withSecond(59);

        if (beginTime.isBefore(beginMinDay) || beginTime.isAfter(beginMaxDay)) {
            throw new StudyTimeMeasurementException("요청한 시작시간이 유효범위를 넘었습니다.");
        } else if (endTime.isBefore(endMinDay) || endTime.isAfter(endMaxDay)) {
            throw new StudyTimeMeasurementException("요청한 종료시간이 유효범위를 넘었습니다.");
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
