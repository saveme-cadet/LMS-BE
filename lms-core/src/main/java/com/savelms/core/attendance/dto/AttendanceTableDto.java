package com.savelms.core.attendance.dto;

import com.savelms.core.attendance.domain.entity.Attendance;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Getter @Setter
public class AttendanceTableDto {

    @NotNull @Min(1)
    private Long attendance_id;

    private String team;
    private String role;
    private String name;
    private Double attendance_score;
    private Double absent_score;
    private Double study_time_score;
    private String check_in;
    private String check_out;
    private Double todo_success_rate;

    public AttendanceTableDto(Attendance attendance) {
        check_in = attendance.getCheckInStatus().name();
        check_out = attendance.getCheckOutStatus().name();
    }

}
