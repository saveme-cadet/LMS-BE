package com.larry.fc.finalproject.core.domain.entity.repository;

import com.larry.fc.finalproject.core.domain.entity.DayTable;
import com.larry.fc.finalproject.core.domain.entity.StudyTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public interface StudyTimeRepository extends JpaRepository<StudyTime, Long> {
    //Optional<StudyTime> findAllByUser_Id(Long id);
    StudyTime findAllByUser_IdAndCh(Long id, Long ch);
    StudyTime findStudyTimeByAojiTimeIndexAndUser_Id(Long index, Long id);
    StudyTime findAllByUser_Id(Long id);
    List<StudyTime> findStudyTimeByUser_Id(Long id);
    List<AojiIdMapping> findAllBy();
    Long countAllByUser_Id(Long id);
    Optional<StudyTime> findAllByUser_IdAndAojiTimeIndex(Long id, Long index);
    List<AojiIdMapping> findAllByEndAt(LocalDateTime time);
    @Transactional
    @Modifying
    List<StudyTime> deleteByAojiTimeIndexAndUser_Id(@Param("aoji_time_index") Long aojiIndex,
                                                       @Param("user_id") Long id);
    @Transactional
    @Modifying
    List<StudyTime> deleteByUser_Id(@Param("user_id") Long id);
}
