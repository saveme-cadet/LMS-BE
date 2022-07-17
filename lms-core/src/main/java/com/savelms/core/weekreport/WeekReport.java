package com.savelms.core.weekreport;

import com.savelms.core.BaseEntity;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Table(name = "WEEK_REPORT")
@Entity
@Builder
public class WeekReport extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="WEEK_REPORT_ID")
    private Long id;

    @Setter
    @Column(nullable = false, updatable = false)
    private String UserNickName;

    @Setter
    @Column(nullable = false, updatable = false)
    private String UserName;

}
