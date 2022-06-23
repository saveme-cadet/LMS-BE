package com.savelms.core.todo.domain.entity;

import com.savelms.core.BaseEntity;
import com.savelms.core.attendance.domain.entity.Attendance;
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
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@SequenceGenerator(
    name = "TODO_GENERATOR",
    sequenceName = "MEMBER_SEQ",
    initialValue = 1,
    allocationSize = 50)
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "TODO")
@Entity
public class Todo extends BaseEntity {



    //********************************* static final 상수 필드 *********************************/


    /********************************* PK 필드 *********************************/

    /**
     * 기본 키
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="TODO_ID")
    private Long id;

    /********************************* PK가 아닌 필드 *********************************/

//    @GeneratedValue(strategy= GenerationType.SEQUENCE, generator = "TODO_GENERATOR")
//    private Integer order;

    private boolean complete;
    private String title;


    /********************************* 연관관계 매핑 *********************************/

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="USER_ID", nullable = false, updatable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CALENDAR_ID", nullable = false, updatable = false)
    private Calendar calendar;



    /********************************* 비영속 필드 *********************************/



    /********************************* 비니지스 로직 *********************************/



}