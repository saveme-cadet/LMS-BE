package com.larry.fc.finalproject.api.dto.tabledto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
@Builder
public class AllUserTableDto {

    @Schema(name = "writer_id", example = "user id = 1 or 2..")
    @NotNull
    @Min(1)
    private final Long writer_id;
    @Schema(description = "체크 인" , example = "출석 = 1, 지각 = 2 ..")
    @Min(0)
    private final short checkIn;
    @Schema(description = "체크 아웃" , example = "출석 = 1, 지각 = 2 ..")
    @Min(0)
    private final short checkOut;
    @Schema(description = "찾고자 하는 요일" , example = "date=2022-02-10")
    private LocalDate tableDay;
    @Schema(description = "역할" , example = "카 뎃 or 머슴")
    private final String role;
    @Schema(description = "이번 주 팀", example = "red or blue")
    private final String team;
    @Schema(description = "출석 점수" ,example = "1, 2")
    private final double attendScore;
    @Schema(description = "매달 참가 점수 출결왕 뽑기 점수" , example = "2, 2.5")
    private final double participateScore;
}
