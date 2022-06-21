package com.savelms.core;

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

@Getter

//@SequenceGenerator(
//    name = "TODO_GENERATOR",
//    sequenceName = "MEMBER_SEQ",
//    initialValue = 1,
//    allocationSize = 50)
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "TODO")
@Entity
public class Todo extends BaseEntity{



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

    private boolean complete;
    private String title;

    /********************************* 연관관계 매핑 *********************************/

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="ATTENDANCE_ID")
    private Attendance attendance;



    /********************************* 비영속 필드 *********************************/



    /********************************* 비니지스 로직 *********************************/



}
