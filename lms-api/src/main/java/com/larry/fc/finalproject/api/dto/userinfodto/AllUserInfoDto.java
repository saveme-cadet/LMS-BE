package com.larry.fc.finalproject.api.dto.userinfodto;

import com.larry.fc.finalproject.core.domain.entity.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
public class AllUserInfoDto {
    @Schema(name = "writer_id", example = "user id = 1 or 2..")
    private final Long writer_id;
    @Schema(description = "참여자 이름" , example = "진성대")
    private final String userName;
    @Schema(description = "가진 휴가 일 수", example = "1 or 2 ++")
    private final double vacation;
    @Schema(description = "level" , example = "구해줘 카뎃 참여한 총 달")
    private final int level;
}
