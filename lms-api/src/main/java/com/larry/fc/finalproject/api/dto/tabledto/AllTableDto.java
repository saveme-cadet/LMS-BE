package com.larry.fc.finalproject.api.dto.tabledto;

import com.larry.fc.finalproject.api.dto.userinfodto.AllUserInfoDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
@Builder
public class AllTableDto {
    @Schema(name = "writer_id", example = "user id = 1 or 2..")
    private final Long writer_id;
    @Schema(description = "참여자 이름" , example = "진성대")
    private final String userName;
    @Schema(description = "체크 인" , example = "출석 = 1, 지각 = 2 ..")
    private final short checkIn;
    @Schema(description = "체크 아웃" , example = "출석 = 1, 지각 = 2 ..")
    private final short checkOut;
    @Schema(description = "역할" , example = "카 뎃 or 머슴")
    private final String role;
    @Schema(description = "이번 주 팀", example = "red or blue")
    private final String team;
    @Schema(description = "참석 점수", example = "2.4")
    private final double attendScore;
    @Schema(description = "매달 참가 점수 출결왕 뽑기 점수" , example = "2, 2.5")
    private final double participateScore;
    @Schema(description = "찾고자 하는 요일" , example = "date=2022-02-10")
    private final LocalDate tableDay;
    @Schema(description = "가진 휴가 일 수", example = "1 or 2 ++")
    private final double vacation;
    @Schema(description = "level" , example = "구해줘 카뎃 참여한 총 달")
    private final int level;
    @Schema(description = "일간 목표 달성률" , example = "70.0")
    @Min(0)
    @Max(100)
    private double dayObjectiveAchievementRate;
    @Schema(description = "월간 목표 달성률" , example = "70.0")
    @Min(0)
    @Max(100)
    private double monthObjectiveAchievementRate;
}
