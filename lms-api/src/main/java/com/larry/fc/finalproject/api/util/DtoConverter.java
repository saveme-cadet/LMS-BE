package com.larry.fc.finalproject.api.util;

import com.larry.fc.finalproject.api.dto.*;
import com.larry.fc.finalproject.api.dto.tabledto.AllTableDto;
import com.larry.fc.finalproject.api.dto.tabledto.AllUserTableDto;
import com.larry.fc.finalproject.api.dto.tabledto.TableCheckInDto;
import com.larry.fc.finalproject.api.dto.tabledto.TableCheckOutDto;
import com.larry.fc.finalproject.api.dto.tododto.TodoDto;
import com.larry.fc.finalproject.api.dto.userdto.UserAttendenceDto;
import com.larry.fc.finalproject.api.dto.userinfodto.*;
import com.larry.fc.finalproject.core.domain.entity.DayTable;
import com.larry.fc.finalproject.core.domain.entity.PlusVacation;
import com.larry.fc.finalproject.core.domain.entity.Todo;
import com.larry.fc.finalproject.core.domain.entity.UserInfo;

public abstract class DtoConverter {
    public static UserInfoDto fromUserInfo(UserInfo userInfo){
        return UserInfoDto.builder()
                .userId(userInfo.getId())
                .userName(userInfo.getUserName())
                .level(userInfo.getLevel())
                .nowSubject(userInfo.getNowSubject())
                .confidenceSubject(userInfo.getConfidenceSubject())
                .attendScore(userInfo.getAttendScore())
                .attendeStatus(userInfo.getAttendeStatus())
                .role(userInfo.getRole())
                .team(userInfo.getTeam())
                .vacation(userInfo.getVacation())
                .participateScore(userInfo.getParticipateScore())
                .build();
    }

    public static AllTableDto fromUserInfoDay(AllUserTableDto allUserTableDto, AllUserInfoDto allUserInfoDto){
        return AllTableDto.builder()
                .writer_id(allUserInfoDto.getWriter_id())
                .userName(allUserInfoDto.getUserName())
                .role(allUserTableDto.getRole())
                .team(allUserTableDto.getTeam())
                .attendScore(allUserTableDto.getAttendScore())
                .participateScore(allUserTableDto.getParticipateScore())
                .level(allUserInfoDto.getLevel())
                .vacation(allUserInfoDto.getVacation())
                .checkIn(allUserTableDto.getCheckIn())
                .checkOut(allUserTableDto.getCheckOut())
                .tableDay(allUserTableDto.getTableDay())
                .build();
    }

    public static AllUserTableDto fromUserDayTable(DayTable dayTable){
        return AllUserTableDto.builder()
                .writer_id(dayTable.getCadet().getId())
                .role(dayTable.getRole())
                .team(dayTable.getTeam())
                .attendScore(dayTable.getAttendScore())
                .participateScore(dayTable.getParticipateScore())
                .checkIn(dayTable.getCheckIn())
                .checkOut(dayTable.getCheckOut())
                .tableDay(dayTable.getTableDay())
                .build();
    }

    public static AllUserInfoDto fromAllUserInfoDto(UserInfo userInfo){
        return AllUserInfoDto.builder()
                .writer_id(userInfo.getWriter().getId())
                .userName(userInfo.getUserName())
                .vacation(userInfo.getVacation())
                .level(userInfo.getLevel())
                .build();
    }

    public static UserInfoWeekDto fromUserInfoWeek(UserInfo userInfo){
        return UserInfoWeekDto.builder()
                .userId(userInfo.getId())
                .attendScore(userInfo.getAttendScore())
                .role(userInfo.getRole())
                .team(userInfo.getTeam())
                .vacation(userInfo.getVacation())
                .participateScore(userInfo.getParticipateScore())
                .build();
    }

    public static UserInfoMonthDto fromUserInfoMonth(UserInfo userInfo){
        return UserInfoMonthDto.builder()
                .userId(userInfo.getId())
                .level(userInfo.getLevel())
                .nowSubject(userInfo.getNowSubject())
                .confidenceSubject(userInfo.getConfidenceSubject())
                .attendeStatus(userInfo.getAttendeStatus())

                .build();
    }

    public static TodoDto fromTodo(Todo todo){
        return TodoDto.builder()
                .userId((todo.getWriter().getId()))
                .todoId(todo.getId())
                .title(todo.getTitle())
                .titleCheck(todo.isTitleCheck())
                .todoDay(todo.getTodoDay())
                .build();
    }

    public static AllUserTableDto fromDayTable(DayTable dayTable){
        return AllUserTableDto.builder()
                .writer_id(dayTable.getCadet().getId())
                .checkIn(dayTable.getCheckIn())
                .checkOut(dayTable.getCheckOut())
                .tableDay(dayTable.getTableDay())
                .build();
    }

    public static Todo fromTodoDto(TodoDto todoDto){
        return Todo.builder()
                .title(todoDto.getTitle())
                .titleCheck(todoDto.isTitleCheck())
                .build();
    }

    public static UserInfo fromUserInfoDto(UserInfoDto userInfoDto){
        return UserInfo.builder()
                .level(userInfoDto.getLevel())
                .nowSubject(userInfoDto.getNowSubject())
                .confidenceSubject(userInfoDto.getConfidenceSubject())
                .attendScore(userInfoDto.getAttendScore())
                .role(userInfoDto.getRole())
                .team(userInfoDto.getTeam())
                .vacation(userInfoDto.getVacation())
                .build();
    }

    public static UserInfo fromUserInfoWeekDto(UserInfoWeekDto userInfoDto){
        return UserInfo.builder()
                .attendScore(userInfoDto.getAttendScore())
                .role(userInfoDto.getRole())
                .team(userInfoDto.getTeam())
                .vacation(userInfoDto.getVacation())
                .build();
    }

    public static UserInfo fromUserInfoMonthDto(UserInfoMonthDto userInfoDto){
        return UserInfo.builder()
                .level(userInfoDto.getLevel())
                .nowSubject(userInfoDto.getNowSubject())
                .confidenceSubject(userInfoDto.getConfidenceSubject())
                .build();
    }

    public static UserInfo fromAttendStatus(UserAttendenceDto userAttendenceDto){
        return UserInfo.builder()
                .attendeStatus(userAttendenceDto.getAttendStatus())
                .build();
    }


    public static DayTable fromAllUserTableDto(AllUserTableDto allUserTableDto){
        return DayTable.builder()
                .checkIn(allUserTableDto.getCheckIn())
                .checkOut(allUserTableDto.getCheckOut())
                .tableDay(allUserTableDto.getTableDay())
                .build();
    }

    public static DayTable fromCheckInTableDto(TableCheckInDto tableCheckInDto){
        return DayTable.builder()
                .checkIn(tableCheckInDto.getCheckIn())
                .tableDay(tableCheckInDto.getTableDay())
                .build();
    }

    public static DayTable fromCheckOutTableDto(TableCheckOutDto tableCheckOutDto){
        return DayTable.builder()
                .checkOut(tableCheckOutDto.getCheckOut())
                .tableDay(tableCheckOutDto.getTableDay())
                .build();
    }

    public static PlusVacationDto fromPlusVacation(PlusVacation plusVacation){
        return PlusVacationDto.builder()
                .userId(plusVacation.getId())
                .startAt(plusVacation.getStartAt())
                .endAt(plusVacation.getEndAt())
                .finished(plusVacation.isFinished())
                .build();
    }

    public static PlusVacation fromPlusVacationDto(PlusVacationDto plusVacationDto){
        return PlusVacation.builder()
                .endAt(plusVacationDto.getEndAt())
                .startAt(plusVacationDto.getStartAt())
                .finished(plusVacationDto.isFinished())
                .build();
    }

    public static DayTable fromDayTableTeam(UserTeamChangeDto userTeamChangeDto) {
        return DayTable.builder()
                .team(userTeamChangeDto.getTeam())
                .build();
    }

    public static DayTable fromDayTableRole(UserRoleChangeDto userRoleChangeDto) {
        return DayTable.builder()
                .role(userRoleChangeDto.getRole())
                .build();
    }

    public static DayTable fromDayTableAll(AllTableDto allTableDto) {
        return DayTable.builder()
                .participateScore(allTableDto.getParticipateScore())
                .role(allTableDto.getRole())
                .team(allTableDto.getTeam())
                .attendScore(allTableDto.getAttendScore())
                .checkOut(allTableDto.getCheckOut())
                .checkIn(allTableDto.getCheckIn())
                .build();
    }
}
