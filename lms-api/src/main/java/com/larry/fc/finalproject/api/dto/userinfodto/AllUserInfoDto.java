package com.larry.fc.finalproject.api.dto.userinfodto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AllUserInfoDto {
    @Schema(name = "writer_id", example = "1")
    private final Long writer_id;
    @Schema(description = "참여자 이름" , example = "진성대")
    private final String userName;
    @Schema(description = "가진 휴가 일 수", example = "0.5")
    private final double vacation;
    @Schema(description = "level" , example = "구해줘 카뎃 참여한 총 달")
    private final int level;
    @Schema(description = "aojiscore", example = "아오지 한달 총 점수")
    private final double aojiscore;
}
