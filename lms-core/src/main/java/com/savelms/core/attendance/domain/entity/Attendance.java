package com.savelms.core.attendance.domain.entity;

import com.savelms.core.BaseEntity;
import com.savelms.core.attendance.domain.AttendanceStatus;
import com.savelms.core.calendar.domain.entity.Calendar;
import com.savelms.core.user.domain.entity.User;
import lombok.*;

import javax.persistence.*;

import static com.savelms.core.attendance.domain.AttendanceStatus.*;

@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Builder
@Entity
@Table(name = "ATTENDANCE")
public class Attendance extends BaseEntity {

    /********************************* PK 필드 *********************************/
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ATTENDANCE_ID")
    private Long id;

    /********************************* PK가 아닌 필드 *********************************/
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AttendanceStatus checkInStatus;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AttendanceStatus checkOutStatus;

    /********************************* FK 연관관계 매핑 *********************************/
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="USER_ID", nullable = false, updatable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="CALENDAR_ID", nullable = false, updatable = false)
    private Calendar calendar;

    //==연관관계 편의 메서드==//
    private void setCalendar(Calendar calendar) {
        this.calendar = calendar;
        calendar.getAttendances().add(this);
    }

    //==생성 메서드==//
    public static Attendance createAttendance(User user, Calendar calendar) {
        Attendance attendance = new Attendance();
        attendance.user = user;
        attendance.setCalendar(calendar);
        attendance.checkIn(NONE);
        attendance.checkOut(NONE);
        return attendance;
    }

    //==비즈니스 로직==//
    /**
     * 체크인
     */
    public void checkIn(AttendanceStatus status) {
        checkInStatus = status;
    }

    /**
     * 체크아웃
     */
    public void checkOut(AttendanceStatus status) {
        checkOutStatus = status;
    }

}
