package com.larry.fc.finalproject.core.domain.entity.repository;

import com.larry.fc.finalproject.core.domain.entity.StudyTime;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public interface StudyTimeRepository extends JpaRepository<StudyTime, Long> {
    Optional<StudyTime> findAllByUser_Id(Long id);
    Optional<StudyTime> findAllByUser_IdAndEndAt(Long id, LocalDateTime time);
    List<StudyTime> findStudyTimeByUser_Id(Long id);
}
