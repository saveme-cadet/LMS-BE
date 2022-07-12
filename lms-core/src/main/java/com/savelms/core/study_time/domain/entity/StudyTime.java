package com.savelms.core.study_time.domain.entity;

import com.savelms.core.BaseEntity;
import com.savelms.core.exception.StudyTimeTooLongException;
import com.savelms.core.user.domain.entity.User;

import java.time.Duration;
import java.time.LocalDateTime;
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
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Table(name = "STUDY_TIME")
@Entity
@Builder
public class StudyTime extends BaseEntity {

    //********************************* static final 상수 필드 *********************************/
    public static final String TIME_FORMAT = "HH:mm:ss";
    public static final String CREATED_DATE_FORMAT = "yyyy-MM-dd";

    /********************************* PK 필드 *********************************/
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="STUDY_TIME_ID")
    private Long id;


    /********************************* PK가 아닌 필드 *********************************/
    @Column(nullable = false)
    private LocalDateTime beginTime;

    @Column(nullable = false)
    private LocalDateTime endTime;

    @Column(nullable = false)
    private String finalStudyTime;

    @Column(nullable = false)
    private Boolean isStudying;


    /********************************* 연관관계 매핑 *********************************/
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="USER_ID", nullable = false, updatable = false)
    private User user;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "CALENDAR_ID", nullable = false, updatable = false)
//    private Calendar calendar;


    /********************************* 비영속 필드 *********************************/


    /********************************* 비니지스 로직 *********************************/
    public static StudyTime createStudyTime(User user) {
        return StudyTime.builder()
                .user(user)
                .beginTime(LocalDateTime.now())
                .endTime(LocalDateTime.now())
                .isStudying(true)
                .finalStudyTime("00:00:00")
                .build();
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

        if (between.toHours() >= 24) {
            throw new StudyTimeTooLongException("24시간 이상은 측정이 불가능합니다.");
        }

        return String.format("%02d:%02d:%02d",
                between.toHours(),
                between.toMinutesPart(),
                between.toSecondsPart());
    }

}
