package com.savelms.core;

import com.savelms.core.auth.domain.entity.User;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
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
    private Boolean CheckIn;
    //하루 총 아오지 시간 저장
    //자료형 적절하게 추후 변경
    private Double aojiStudyTime;
    private Double todoCompleteRate;


    /********************************* 연관관계 매핑 *********************************/


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="USER_ID")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="CALENDAR_ID")
    private Calendar calendar;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="SCORE_ID")
    private Score Score;


    @OneToMany(mappedBy = "attendance")
    private List<Todo> todos = new ArrayList<>();

    @OneToMany(mappedBy = "attendance")
    private List<StudyTime> studyTimes = new ArrayList<>();

    /********************************* 비영속 필드 *********************************/

    /********************************* 비니지스 로직 *********************************/


}
