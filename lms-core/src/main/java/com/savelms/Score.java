package com.savelms;


import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Singular;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@Table(name = "SCORE")
@Entity
public class Score extends BaseEntity{

    //********************************* static final 상수 필드 *********************************/


    /********************************* PK 필드 *********************************/

    /**
     * 기본 키
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="SCORE_ID")
    private Long id;

    /********************************* PK가 아닌 필드 *********************************/
    private Double attendanceScore;
    private Double absentScore;


    /********************************* 비영속 필드 *********************************/


    /********************************* 연관관계 매핑 *********************************/




    /********************************* 비니지스 로직 *********************************/



}
