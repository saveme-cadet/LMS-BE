package com.savelms.core.attendance.domain.entity;

import com.savelms.core.BaseEntity;
import com.savelms.core.attendance.domain.CheckIn;
import com.savelms.core.attendance.domain.CheckOut;
import com.savelms.core.calendar.domain.entity.Calendar;
import com.savelms.core.study_time.domain.entity.StudyTime;
import com.savelms.core.todo.domain.entity.Todo;
import com.savelms.core.user.domain.entity.User;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@Entity
@Table(name = "ATTENDANCE")
public class Attendance extends BaseEntity {

    //********************************* static final 상수 필드 *********************************/

    /********************************* PK 필드 *********************************/

    /**
     * 기본 키
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ATTENDANCE_ID")
    private Long id;

    /********************************* PK가 아닌 필드 *********************************/
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CheckIn checkIn;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CheckOut checkout;



    /********************************* 연관관계 매핑 *********************************/


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="USER_ID", nullable = false, updatable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="CALENDAR_ID", nullable = false, updatable = false)
    private Calendar calendar;




    /********************************* 비영속 필드 *********************************/

    /********************************* 비니지스 로직 *********************************/


}
