package com.larry.fc.finalproject.core.domain.entity.repository;

import com.larry.fc.finalproject.core.domain.entity.DayTable;
import com.larry.fc.finalproject.core.domain.entity.StudyTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public interface StudyTimeRepository extends JpaRepository<StudyTime, Long> {
    //Optional<StudyTime> findAllByUser_Id(Long id);

    //@Query("select m from StudyTime m where m.ch = :ch and m.user.id = :userId GROUP BY m.startAt")
    //@Query("select m from StudyTime left join User on m.owner_id = owner.id")
    //@Query("select m from StudyTime m join fetch m.user")
    //@Query("select m from StudyTime m where m.user.id = :userId and m.ch = : ch" )
    StudyTime findByUser_IdAndCh(Long userId, Long ch);
    //List<StudyTime> findDistinctByChAndUserId(Long ch, Long userId);
    StudyTime findStudyTimeByAojiTimeIndexAndUser_Id(Long index, Long id);
    StudyTime findAllByUser_Id(Long id);
    List<StudyTime> findStudyTimeByUser_IdAndCh(Long id, Long ch);
    List<StudyTime> findStudyTimeByUser_Id(Long id);
  //  List<AojiIdMapping> findAllByUser_IdAndCH(Long id, Long ch);
    List<AojiIdMapping> findAllBy();
    Long countAllByUser_Id(Long id);
    Optional<StudyTime> findAllByUser_IdAndAojiTimeIndex(Long id, Long index);
    List<AojiIdMapping> findAllByCh(Long ch);
    @Transactional
    @Modifying
    List<StudyTime> deleteByAojiTimeIndexAndUser_Id(@Param("aoji_time_index") Long aojiIndex,
                                                       @Param("user_id") Long id);
    @Transactional
    @Modifying
    List<StudyTime> deleteByUser_Id(@Param("user_id") Long id);
}
