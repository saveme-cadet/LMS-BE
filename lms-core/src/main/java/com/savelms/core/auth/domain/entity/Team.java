package com.savelms.core.auth.domain.entity;

import com.savelms.core.BaseEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Table(name = "TEAM")
@Entity
@Builder
public class Team extends BaseEntity {

    //********************************* static final 상수 필드 *********************************/


    /********************************* PK 필드 *********************************/

    /**
     * 기본 키
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "TEAM_ID")
    private Long id;

    /********************************* PK가 아닌 필드 *********************************/

    private String team;

    /********************************* 비영속 필드 *********************************/


    /********************************* 연관관계 매핑 *********************************/




    /********************************* 비니지스 로직 *********************************/



}
