package com.savelms.core.statistical;

import com.savelms.core.BaseEntity;
import com.savelms.core.attendance.domain.AttendanceStatus;
import com.savelms.core.attendance.domain.entity.Attendance;
import com.savelms.core.calendar.domain.entity.Calendar;
import com.savelms.core.study_time.domain.entity.StudyTime;
import com.savelms.core.todo.domain.entity.Todo;
import com.savelms.core.user.domain.entity.User;

import javax.persistence.*;

import lombok.*;

import java.util.List;

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

    //연관관계를 todo, attendance, study_time 이랑 1대1로 묶는건 어떨까?

//    @OneToOne(mappedBy = "statisticalData", fetch = FetchType.LAZY)
//    private Attendance attendance;
//
//    @OneToMany
//    private List<Todo> todos;
//
//    @OneToMany
//    private List<StudyTime> studyTimes;

    /********************************* 비니지스 로직 *********************************/


//    public void markAttendance(AttendanceStatus status) {
//        //계속 반복해서 찍는거 제외해야함
//        //아그냥 배치돌리고 오늘건 쿼리로 뽑아내는게 나을지도?
//
//        if (status == AttendanceStatus.PRESENT) {
//            attendanceScore += 0.5;
//        }
//
//    } -> 기각~~~~ 기존방식대로 하는게 더 좋을 듯
}
