package com.savelms.core.monthreport;

import com.savelms.core.BaseEntity;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Table(name = "MONTH_REPORT")
@Entity
@Builder
public class MonthReport extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="MONTH_REPORT_ID")
    private Long id;

    @Setter
    @Column(nullable = false, updatable = false)
    private String UserName;

    @Setter
    @Column(nullable = false, updatable = false)
    private String UserNickName;

    @Setter
    @Column(nullable = false, updatable = false)
    private int grade;

    @Column(nullable = false, updatable = false)
    private LocalDate month;

    @Column(nullable = false, updatable = false)
    private double score;

}
