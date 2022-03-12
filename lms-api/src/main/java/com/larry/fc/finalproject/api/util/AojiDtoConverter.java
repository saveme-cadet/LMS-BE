package com.larry.fc.finalproject.api.util;

import com.larry.fc.finalproject.api.dto.aojidto.AojiDto;
import com.larry.fc.finalproject.api.dto.aojidto.AojiResponseDto;
import com.larry.fc.finalproject.api.dto.userinfodto.UserInfoDto;
import com.larry.fc.finalproject.core.domain.entity.StudyTime;

import java.time.Duration;
import java.time.LocalDateTime;


public abstract class AojiDtoConverter {
    public static AojiResponseDto fromStudyTime(StudyTime studyTime){
        Long time = Duration.between(studyTime.getStartAt(), studyTime.getEndAt()).getSeconds();
        Long hour = time/(60*60);
        Long minute = time/60-(hour*60);
        Long second = time%60;
        StringBuilder sb = new StringBuilder();
        sb.append(hour + ":").append(minute + ":").append(second);

        return AojiResponseDto.builder()
                .userId(studyTime.getUser().getWriter().getId())
                .aojiTimeIndex(studyTime.getAojiTimeIndex())
                .startAt(studyTime.getStartAt())
                .endAt(studyTime.getEndAt())
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
