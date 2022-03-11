package com.larry.fc.finalproject.api.util;

import com.larry.fc.finalproject.api.dto.aojidto.AojiResponseDto;
import com.larry.fc.finalproject.api.dto.userinfodto.UserInfoDto;
import com.larry.fc.finalproject.core.domain.entity.StudyTime;

import java.time.Duration;


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
                .startAt(studyTime.getStartAt())
                .endAt(studyTime.getEndAt())
                .recodeTime(sb.toString())
                .build();
    }
}
