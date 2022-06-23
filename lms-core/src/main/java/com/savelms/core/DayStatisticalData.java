package com.savelms.core;

import com.savelms.core.calendar.domain.entity.Calendar;
import com.savelms.core.user.domain.entity.User;
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
@Table(name = "DAY_STATISTICAL_DATA")
@Entity
@Builder
public class DayStatisticalData extends BaseEntity {


    //********************************* static final 상수 필드 *********************************/


    /********************************* PK 필드 *********************************/

    /**
     * 기본 키
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="DAY_STATISTICAL_DATA_ID")
    private Long id;

    /********************************* PK가 아닌 필드 *********************************/

    @Column(nullable = false, updatable = false)
    private Double attendanceScore;

    @Column(nullable = false, updatable = false)
    private Double absentScore;

    @Column(nullable = false, updatable = false)
    private Double todoSuccessRate;

    @Column(nullable = false, updatable = false)
    private Double studyTimeScore;

    @Column(nullable = false, updatable = false)
    private Double totalScore;



    /********************************* 비영속 필드 *********************************/


    /********************************* 연관관계 매핑 *********************************/

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="USER_ID")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="CALENDAR_ID")
    private Calendar calendar;

    /********************************* 비니지스 로직 *********************************/

}