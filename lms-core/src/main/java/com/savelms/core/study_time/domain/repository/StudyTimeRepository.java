package com.savelms.core.study_time.domain.repository;

import com.savelms.core.study_time.domain.entity.StudyTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface StudyTimeRepository extends JpaRepository<StudyTime, Long> {

    @Query("select s from StudyTime s join fetch s.user u " +
            "where u.apiId <> :apiId and s.isStudying = :isStudying")
    List<StudyTime> findByIsStudying(@Param("apiId") String apiId, @Param("isStudying") Boolean isStudying);

    @Query("select s from StudyTime s where s.user.apiId =:apiId and s.isStudying =:isStudying order by s.id desc")
    List<StudyTime> findByUserApiIdAndIsStudying(@Param("apiId") String apiId, Boolean isStudying);

    @Query("select s from StudyTime s where s.user.apiId =:apiId")
    List<StudyTime> findByUserApiId(@Param("apiId") String apiId);

    @Query(value = "select s from StudyTime s " +
            "join s.user u join s.calendar c " +
            "where u.apiId = :apiId and c.date = :date")
    List<StudyTime> findByUserApiIdAndDate(@Param("apiId") String apiId, @Param("date") LocalDate date);

    @Query(value = "select s from StudyTime s " +
            "join s.user u join s.calendar c " +
            "where u.apiId = :apiId " +
            "and s.beginTime <= :beginTime and s.endTime >= :beginTime " +
            "or s.beginTime <= :endTime and s.endTime >= :endTime")
    List<StudyTime> findByUserApiIdAndBeginTimeAndEndTimeBetween(
            @Param("apiId") String apiId,
            @Param("beginTime") LocalDateTime beginTime,
            @Param("endTime") LocalDateTime endTime);

    Optional<StudyTime> findByUserIdAndCalendarIdAndIsStudying(Long userId, Long calendarId, Boolean ch);
    boolean existsByUserIdAndCalendarIdAndIsStudying(Long userId, Long calendarId, Boolean ch);

}

