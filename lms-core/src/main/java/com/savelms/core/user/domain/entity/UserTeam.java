package com.savelms.core.user.domain.entity;

import com.savelms.core.BaseEntity;
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
@Table(name = "USER_TEAM")
@Entity
@Builder
public class UserTeam extends BaseEntity {

    //********************************* static final 상수 필드 *********************************/


    /********************************* PK 필드 *********************************/

    /**
     * 기본 키
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "USER_TEAM_ID")
    private Long id;

    /********************************* PK가 아닌 필드 *********************************/


    /********************************* 비영속 필드 *********************************/


    /********************************* 연관관계 매핑 *********************************/

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="USER_ID", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="TEAM_ID", nullable = false)
    private Team team;


    /********************************* 비니지스 로직 *********************************/






}
