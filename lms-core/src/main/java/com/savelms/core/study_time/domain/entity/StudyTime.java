package com.savelms.core.study_time.domain.entity;

import com.savelms.core.BaseEntity;
import com.savelms.core.calendar.domain.entity.Calendar;
import com.savelms.core.exception.StudyTimeMeasurementException;
import com.savelms.core.user.domain.entity.User;

import java.time.Duration;
import java.time.LocalDateTime;
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

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="USER_ID", nullable = false, updatable = false)
    private User user;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CALENDAR_ID", nullable = false, updatable = false)
    private Calendar calendar;


    protected StudyTime() {}

    public static StudyTime of(User user, Calendar calendar) {
        return StudyTime.builder()
                .user(user)
                .calendar(calendar)
                .beginTime(LocalDateTime.now())
                .endTime(LocalDateTime.now())
                .isStudying(true)
                .finalStudyTime("00:00:00")
                .build();
    }

    public static Double getStudyScore(LocalDateTime beginTime, LocalDateTime endTime) {
        double second = (double) Duration.between(beginTime, endTime).getSeconds();
        double studyTimeScore = second / (8 * 60 * 60);

        return Math.round(studyTimeScore * 100) / 100.0 ;
    }

    public void updateStudyTime(LocalDateTime beginTime, LocalDateTime endTime) {
        this.beginTime = beginTime;
        this.endTime = endTime;
        this.finalStudyTime = getFinalStudyTime(this.beginTime, this.endTime);
    }

    public void endStudyTime() {
        this.endTime = LocalDateTime.now();
        this.isStudying = false;
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
