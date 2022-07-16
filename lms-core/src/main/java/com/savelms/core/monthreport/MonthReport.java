package com.savelms.core.monthreport;

import com.savelms.core.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
@Setter
@Table(name = "MONTH_REPORT")
@Entity
@Builder
public class MonthReport extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="MONTH_REPORT_ID")
    private Long id;

    @Column(nullable = false, updatable = false)
    private String UserName;

    @Column(nullable = false, updatable = false)
    private String UserNickName;

    @Column(nullable = false, updatable = false)
    private int grade;


    public MonthReport(){
        setCreatedAt(LocalDateTime.now());
        setUpdatedAt(LocalDateTime.now());
        setCreateId("ADMIN");
        setUpdateId("ADMIN");
    }

}
