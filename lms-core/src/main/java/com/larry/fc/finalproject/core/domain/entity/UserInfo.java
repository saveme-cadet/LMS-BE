package com.larry.fc.finalproject.core.domain.entity;

import com.larry.fc.finalproject.core.domain.ScheduleType;
import com.larry.fc.finalproject.core.domain.util.Period;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Min;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "userinfo")
@Entity
public class UserInfo extends BaseEntity{
    private int level;          //매달
    private String nowSubject;  //잘 안바뀜
    private String confidenceSubject; // 잘 안바뀜
    private Long attendeStatus;
    private String userName;
    private String role;            // 주 마다
    private double attendScore;     // 매일 자동 갱신
    private String team;// 주 마다
    @Min(0)
    private double vacation;         // 가진 휴가 일 수

    private double participateScore;   // 구해줘 카뎃 평일 참가 점수
    @JoinColumn(name = "writer_id")
    @ManyToOne
    private User writer;





    public static UserInfo userInfoJoin(User writer){
        return UserInfo.builder()
                .level(0)
                .nowSubject("")
                .confidenceSubject("")
                .writer(writer)
                .attendScore(0)
                .attendeStatus(writer.getAttendStatus())
                .team("white")
                .role("카뎃")
                .participateScore(0.0)
                .userName(writer.getName())
                .vacation(0.0)
                .build();
    }

    public boolean isOverlapped(LocalDate date) {
        return Period.of(date).isOverlapped(date);
    }
}
