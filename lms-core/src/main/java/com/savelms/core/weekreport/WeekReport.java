package com.savelms.core.weekreport;

import com.savelms.core.BaseEntity;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
@Setter
@Table(name = "WEEK_REPORT")
@Entity
@Builder
public class WeekReport extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="WEEK_REPORT_ID")
    private Long id;

    @Column(nullable = false, updatable = false)
    private String UserNickName;

    @Column(nullable = false, updatable = false)
    private String UserName;


    public WeekReport(){
        setCreatedAt(LocalDateTime.now());
        setUpdatedAt(LocalDateTime.now());
        setCreateId("ADMIN");
        setUpdateId("ADMIN");
    }
}
