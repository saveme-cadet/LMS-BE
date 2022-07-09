package com.savelms.core.vacation.domain.entity;


import com.savelms.core.BaseEntity;
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
@Table(name = "VACATION")
@Entity
@Builder
public class Vacation extends BaseEntity {

    //********************************* static final 상수 필드 *********************************/


    /********************************* PK 필드 *********************************/
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="VACATION_ID")
    private Long id;

    /********************************* PK가 아닌 필드 *********************************/
    @Column(nullable = false)
    private Long assignedDays;

    @Column(nullable = false)
    private Long usedDays;

    private String reason;

    /********************************* 연관관계 매핑 *********************************/
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="USER_ID", nullable = false, updatable = false)
    private User user;

    /********************************* 비영속 필드 *********************************/



    /********************************* 비니지스 로직 *********************************/




}
