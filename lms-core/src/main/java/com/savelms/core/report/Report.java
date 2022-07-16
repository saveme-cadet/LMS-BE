package com.savelms.core.report;

import com.savelms.core.BaseEntity;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
@Setter
@Table(name = "REPORT")
@Entity
@Builder
public class Report extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="REPORT_ID")
    private Long id;

    @Column(nullable = false, updatable = false)
    private String UserNickName;

    @Column(nullable = false, updatable = false)
    private String UserName;


    public Report(){
        setCreatedAt(LocalDateTime.now());
        setUpdatedAt(LocalDateTime.now());
        setCreateId("ADMIN");
        setUpdateId("ADMIN");
    }
}
