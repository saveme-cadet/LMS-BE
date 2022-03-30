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

    private Long aojiTimeIndex;
    private LocalDate day;
    private LocalDateTime startAt;
    private LocalDateTime endAt;
    private Long ch;

    public static StudyTime studyTimeJoin(UserInfo writer, Long aojiTimeIndex){
        return StudyTime.builder()
                .user(writer)
                .aojiTimeIndex(aojiTimeIndex)
                .day(LocalDate.now())
                .startAt(LocalDateTime.now())
                .endAt(null)
                .ch(0L)
                .build();
    }
}
