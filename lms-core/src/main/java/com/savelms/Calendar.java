package com.savelms;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Singular;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Table(name = "CALENDAR")
@Entity
@Builder
public class Calendar extends BaseEntity{


    //********************************* static final 상수 필드 *********************************/


    /********************************* PK 필드 *********************************/

    /**
     * 기본 키
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="CALENDAR_ID")
    private Long id;

    /********************************* PK가 아닌 필드 *********************************/
    LocalDate date;

    @Enumerated(value = EnumType.STRING)
    DayType dayType;


    /********************************* 비영속 필드 *********************************/


    /********************************* 연관관계 매핑 *********************************/

    @OneToMany(mappedBy = "calendar")
    private List<Attendance> attendances = new ArrayList<>();


    /********************************* 비니지스 로직 *********************************/




    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long calendarId;


}
