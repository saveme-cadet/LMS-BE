package com.savelms.core.calendar.domain.entity;

import com.savelms.core.BaseEntity;
import com.savelms.core.calendar.DayType;
import com.savelms.core.attendance.domain.entity.Attendance;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Singular;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Table(name = "CALENDAR", uniqueConstraints = @UniqueConstraint(name = "DATE_UNIQUE", columnNames = {
    "date"}))

@Entity
@Builder
public class Calendar extends BaseEntity {

    //********************************* static final 상수 필드 *********************************/

    /********************************* PK 필드 *********************************/

    /**
     * 기본 키
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CALENDAR_ID")
    private Long id;

    /********************************* PK가 아닌 필드 *********************************/

    LocalDate date;

    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false)
    DayType dayType;

    /********************************* 비영속 필드 *********************************/


    /********************************* 연관관계 매핑 *********************************/

    @Singular
    @OneToMany(mappedBy = "calendar")
    private final List<Attendance> attendances = new ArrayList<>();

    /********************************* 비니지스 로직 *********************************/


}
