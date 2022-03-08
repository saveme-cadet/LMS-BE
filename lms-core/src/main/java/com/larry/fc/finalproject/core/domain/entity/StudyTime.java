package com.larry.fc.finalproject.core.domain.entity;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "studytime")
@Entity
public class StudyTime extends BaseEntity{
    @JoinColumn(name = "user_id")
    @ManyToOne
    private UserInfo user_id;

    private LocalDate day;
    private LocalDateTime startAt;
    private LocalDateTime endAt;
}
