package com.larry.fc.finalproject.core.domain.entity;

import com.larry.fc.finalproject.core.domain.ScheduleType;
import com.larry.fc.finalproject.core.domain.util.Period;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "Todo")
@Entity
public class Todo extends BaseEntity{
    private String title;
    private boolean titleCheck;
    private LocalDate todoDay;

    @Enumerated(EnumType.STRING)
    private ScheduleType scheduleType;

    @JoinColumn(name = "writer_id")
    @ManyToOne
    private User writer;

    public static Todo todoJoin(String title, User writer){
        return Todo.builder()
                .title(title)
                .titleCheck(false)
                .scheduleType(ScheduleType.TODO)
                .todoDay(LocalDate.now())
                .writer(writer)
                .build();
    }

    public boolean isOverlapped(LocalDate date) {
        return Period.of(todoDay).isOverlapped(date);
    }

    public boolean isOverlapped(Period period, LocalDate date){
        return Period.of(date).isOverlapped(date);
    }
}
