package com.savelms.core.statistical;

import com.savelms.core.BaseEntity;
import com.savelms.core.calendar.domain.entity.Calendar;
import com.savelms.core.user.domain.entity.User;

import javax.persistence.*;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Table(name = "DAY_STATISTICAL_DATA")
@Entity
@Builder
public class DayStatisticalData extends BaseEntity {


    //********************************* static final 상수 필드 *********************************/


    /********************************* 컬럼 *********************************/
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="DAY_STATISTICAL_DATA_ID")
    private Long id;

    @Column(nullable = false)
    private Double attendanceScore;

    @Column(nullable = false)
    private Double absentScore;

    @Column(nullable = false)
    private Double todoSuccessRate;

    @Column(nullable = false)
    private Double studyTimeScore;

    @Column(nullable = false)
    private Double totalScore;

    @Column(nullable = false)
    private Double weekAbsentScore;// 매주 결석 체크




    /********************************* 비영속 필드 *********************************/



    /********************************* 연관관계 매핑 *********************************/
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="USER_ID")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="CALENDAR_ID")
    private Calendar calendar;


    /********************************* 생성메서드 *********************************/
    public static DayStatisticalData createDayStatisticalData(User user, Calendar calendar) {
        return DayStatisticalData.builder()
            .attendanceScore(0.0)
            .absentScore(0.0)
            .todoSuccessRate(0.0)
            .studyTimeScore(0.0)
            .totalScore(0.0)
            .weekAbsentScore(0.0)
            .user(user)
            .calendar(calendar)
            .build();
    }

    /********************************* 비니지스 로직 *********************************/
    public void updateAttendanceScore(Double attendanceScore) {
        this.attendanceScore += attendanceScore;
    }

    public void updateAbsentScore(Double absentScore) {
        this.absentScore += absentScore;
        this.totalScore += absentScore;
    }

    public void updateTodoSuccessRate(Double todoSuccessRate) {
        this.absentScore = todoSuccessRate;
    }

    public void updateStudyTimeScore(Double studyTimeScore) {
        this.studyTimeScore += studyTimeScore;
        this.totalScore -= studyTimeScore;
    }

    public void updateWeekAbsentScore(Double weekAbsentScore) {
        this.weekAbsentScore += weekAbsentScore;
    }

    public void updateTotalScore(Double toTalStudyTimeScore) {
        this.totalScore = this.absentScore - toTalStudyTimeScore;
    }
}
