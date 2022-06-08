package com.larry.fc.finalproject.core.domain.entity;

import com.larry.fc.finalproject.core.domain.entity.user.User;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "aojitime")
@Entity
@NamedQuery(name="aojitime.findAll", query="select m from StudyTime m")
public class StudyTime extends BaseEntity{

    @JoinColumn(name = "user_id")
    @ManyToOne (fetch = FetchType.EAGER)
    private User user;

    private Long aojiTimeIndex;
    private LocalDate day;
    private LocalDateTime startAt;
    private LocalDateTime endAt;
    private Long ch;

    public static StudyTime studyTimeJoin(User writer, Long aojiTimeIndex){
        return StudyTime.builder()
                .user(writer)
                .aojiTimeIndex(aojiTimeIndex)
                .day(LocalDate.now())
                .startAt(LocalDateTime.now())
                .endAt(LocalDateTime.now())
                .ch(0L)
                .build();
    }
}
