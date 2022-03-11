package com.larry.fc.finalproject.core.domain.entity;

import com.larry.fc.finalproject.core.domain.ScheduleType;
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
@Table(name = "aojitime")
@Entity
public class StudyTime extends BaseEntity{

    @JoinColumn(name = "user_id")
    @ManyToOne
    private UserInfo user;

    private LocalDate day;
    private LocalDateTime startAt;
    private LocalDateTime endAt;

    public static StudyTime studyTimeJoin(UserInfo writer){
        return StudyTime.builder()
                .user(writer)
                .day(LocalDate.now())
                .startAt(LocalDateTime.now())
                .endAt(null)
                .build();
    }
}
