package com.larry.fc.finalproject.core.domain.entity;

import com.larry.fc.finalproject.core.domain.ScheduleType;
import com.larry.fc.finalproject.core.domain.util.Period;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "daytable")
public class DayTable extends BaseEntity{

    @JoinColumn(name = "cadet_id")
    @ManyToOne
    private User cadet;
    private short checkIn;
    private short checkOut;
    private String role;
    private String team;
    private double attendScore;
    private double participateScore;
    private Long attendeStatus;
    private LocalDate tableDay;


    public static DayTable dayTableJoin(User writer){

        return DayTable.builder()
                .checkIn((short) 0)
                .checkOut((short) 0)
                .cadet(writer)
                .tableDay(LocalDate.now())
                .attendeStatus(writer.getAttendStatus())
                .role("카뎃")
                .team("white")
                .attendScore(0.0)
                .participateScore(0.0)
                .build();
    }

    public static DayTable dayTableJoinWithDate(User writer, LocalDate date, String role, String team){

        return DayTable.builder()
                .checkIn((short) 0)
                .checkOut((short) 0)
                .cadet(writer)
                .tableDay(date)
                .attendeStatus(writer.getAttendStatus())
                .role(role)
                .team(team)
                .attendScore(0.0)
                .build();
    }

    public static DayTable dayTableJoinWith(User writer, String role, String team, double attendScore, double participateScore){

        return DayTable.builder()
                .checkIn((short) 0)
                .checkOut((short) 0)
                .cadet(writer)
                .tableDay(LocalDate.now())
                .attendeStatus(writer.getAttendStatus())
                .role(role)
                .team(team)
                .attendScore(attendScore)
                .participateScore(participateScore)
                .build();
    }

    public boolean isOverlapped(Period period) {
        return Period.of(tableDay).isOverlapped(period);
    }
}
