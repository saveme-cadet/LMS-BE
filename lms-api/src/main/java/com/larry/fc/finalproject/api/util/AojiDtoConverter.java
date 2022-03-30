package com.larry.fc.finalproject.api.util;

import com.larry.fc.finalproject.api.dto.aojidto.AojiDto;
import com.larry.fc.finalproject.api.dto.aojidto.AojiResponseDto;
import com.larry.fc.finalproject.api.dto.userinfodto.UserInfoDto;
import com.larry.fc.finalproject.core.domain.entity.StudyTime;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;


public abstract class AojiDtoConverter {
    public static AojiResponseDto fromStudyTime(StudyTime studyTime){
        StringBuilder sb = new StringBuilder();
        LocalDateTime ld;
        if (ChronoUnit.SECONDS.between(studyTime.getStartAt(), studyTime.getEndAt()) != 0) {
            Long time = Duration.between(studyTime.getStartAt(), studyTime.getEndAt()).getSeconds();
            Long hour = time / (60 * 60);
            Long minute = time / 60 - (hour * 60);
            Long second = time % 60;
            sb.append(hour + ":").append(minute + ":").append(second);
            ld = studyTime.getEndAt();
        }
        else {
            ld = null;
            sb.append("doing study");
        }
        return AojiResponseDto.builder()
                .userId(studyTime.getUser().getId())
                .aojiTimeIndex(studyTime.getAojiTimeIndex())
                .startAt(studyTime.getStartAt())
                .endAt(ld)
                .recodeTime(sb.toString())
                .build();
    }

    public static StudyTime fromAojiDto(AojiDto aojiDto){
        LocalDateTime end;
        LocalDateTime start;
        if (aojiDto.getEndAt() == null){
            end = aojiDto.getStartAt().plusSeconds(1);
        } else {
            end = aojiDto.getEndAt();
        }
        if (aojiDto.getStartAt() == null){
            start = aojiDto.getEndAt().plusSeconds(1);
        } else {
            start = aojiDto.getStartAt();
        }
        return StudyTime.builder()
                .startAt(start)
                .endAt(end)
                .build();
    }


}
