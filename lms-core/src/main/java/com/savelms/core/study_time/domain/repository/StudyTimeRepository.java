package com.savelms.core.study_time.domain.repository;

import com.savelms.core.study_time.domain.entity.StudyTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface StudyTimeRepository extends JpaRepository<StudyTime, Long> {

    @Query("select s from StudyTime s join fetch s.user u " +
            "where u.apiId <> :apiId and s.isStudying = :isStudying")
    Optional<List<StudyTime>> findByIsStudying(@Param("apiId") String apiId, @Param("isStudying") Boolean isStudying);

    @Query("select s from StudyTime s where s.user.apiId =:apiId and s.isStudying =:isStudying order by s.id desc")
    List<StudyTime> findByUserApiIdAndIsStudying(@Param("apiId") String apiId, Boolean isStudying);

    @Query("select s from StudyTime s where s.user.apiId =:apiId")
    List<StudyTime> findByUserApiId(@Param("apiId") String apiId);

    @Query(value = "select s.* from study_time s " +
            "join user u on u.user_id = s.user_id " +
            "where u.api_id = :apiId and date(s.begin_time) = :date or date(s.end_time) = :date", nativeQuery = true)
    List<StudyTime> findByUserApiIdAndDate(@Param("apiId") String apiId, @Param("date") LocalDate date);

    Optional<StudyTime> findAllById(Long id);


    /**
     *
     * @param userId
     * @param calendarId
     * @return
     *
     *  findByUserApiIdAndDate 정상 작동 안됌
     *  where 절에서 u.api_id
     */
    List<StudyTime> findByUserIdAndCalendarId(Long userId, Long calendarId);
}

