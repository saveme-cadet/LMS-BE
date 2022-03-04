package com.larry.fc.finalproject.core.domain.entity;

import lombok.*;

import javax.persistence.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "staticchart")
@Entity
public class StatisticalChart{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private double monthAttendanceRate;
    private double weekAttendanceRate;
    private double dayObjectiveAchievementRate;
    private double weekObjectiveAchievementRate;
    private double monthObjectiveAchievementRate;


    @JoinColumn(name = "writer_id")
    @ManyToOne
    private User writer;

    public static StatisticalChart userStaticChartJoin(User writer){
        return StatisticalChart.builder()
                .monthAttendanceRate(100)
                .weekAttendanceRate(100)
                .dayObjectiveAchievementRate(100)
                .weekObjectiveAchievementRate(100)
                .monthObjectiveAchievementRate(100)
                .writer(writer)
                .build();
    }
}
