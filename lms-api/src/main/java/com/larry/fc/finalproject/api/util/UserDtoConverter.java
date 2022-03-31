package com.larry.fc.finalproject.api.util;

import com.larry.fc.finalproject.api.dto.tabledto.AllTableDto;
import com.larry.fc.finalproject.api.dto.userdto.UserAttendenceDto;
import com.larry.fc.finalproject.api.dto.userdto.UserDto;
import com.larry.fc.finalproject.api.dto.userdto.UserTeamAndRoleDto;
import com.larry.fc.finalproject.api.dto.userinfodto.UserRoleChangeDto;
import com.larry.fc.finalproject.api.dto.userinfodto.UserTeamChangeDto;
import com.larry.fc.finalproject.api.dto.userinfodto.UserVacationChangeDto;
import com.larry.fc.finalproject.api.dto.userstatisticalchartdto.AttendDto;
import com.larry.fc.finalproject.api.dto.userstatisticalchartdto.ObjectDto;
import com.larry.fc.finalproject.core.domain.entity.StatisticalChart;
import com.larry.fc.finalproject.core.domain.entity.User;
import com.larry.fc.finalproject.core.domain.entity.UserInfo;


public abstract class UserDtoConverter {
    public static UserDto fromUser(User user){
        return UserDto.builder()
                .userName(user.getName())
                .userId(user.getId())
                .build();
    }

    public static AttendDto fromAttend(StatisticalChart statisticalChart){
        return AttendDto.builder()
                .userId(statisticalChart.getWriter().getId())
                .monthAttendanceRate(statisticalChart.getMonthAttendanceRate())
                .build();
    }

    public static ObjectDto fromObject(StatisticalChart statisticalChart){
        return ObjectDto.builder()
                .userId(statisticalChart.getWriter().getId())
                .dayObjectiveAchievementRate(statisticalChart.getDayObjectiveAchievementRate())
                .monthObjectiveAchievementRate(statisticalChart.getMonthObjectiveAchievementRate())
                .build();
    }

    public static UserInfo fromUserTeam(UserTeamChangeDto userTeamChangeDto){
        return UserInfo.builder()
                .team(userTeamChangeDto.getTeam())
                .build();
    }
    public static UserInfo fromUserRole(UserRoleChangeDto userRoleChangeDto){
        return UserInfo.builder()
                .role(userRoleChangeDto.getRole())
                .build();
    }

    public static UserInfo fromUserVacation(UserVacationChangeDto userVacationChangeDto){
        return UserInfo.builder()

                .build();
    }

    public static UserInfo fromUserAllInfo(AllTableDto allTableDto) {
        return UserInfo.builder()
                .vacation(allTableDto.getVacation())
                .level(allTableDto.getLevel())
                .build();
    }

    public static User fromUser(UserAttendenceDto userAttendenceDto){
        return User.builder()
                .attendStatus(userAttendenceDto.getAttendStatus())
                .build();
    }


    public static UserInfo fromUserInfoInAttendScore(Double attendScore){
        return UserInfo.builder()
                .attendScore(attendScore)
                .build();
    }
}
