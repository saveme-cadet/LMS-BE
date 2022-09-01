package com.savelms.core.study_time.domain.repository;

import com.savelms.core.study_time.domain.entity.StudyTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface StudyTimeRepository extends JpaRepository<StudyTime, Long> {

    Optional<List<StudyTime>> findByUserId(Long userId);

    @Query("select s from StudyTime s join fetch s.user u where s.isStudying =:isStudying")
    Optional<List<StudyTime>> findByIsStudying(Boolean isStudying);

    @Query("select s from StudyTime s where s.user.username =:username and s.isStudying =:isStudying")
    Optional<StudyTime> findByUsernameAndIsStudying(String username, Boolean isStudying);

    @Query("select s from StudyTime s where s.user.apiId =:apiId and s.isStudying =:isStudying")
    Optional<StudyTime> findByUserApiIdAndIsStudying(@Param("apiId") String apiId, Boolean isStudying);

    @Query("select s from StudyTime s where s.user.username =:username")
    Optional<List<StudyTime>> findByUsername(String username);

    @Query("select s from StudyTime s where s.user.apiId =:apiId")
    Optional<List<StudyTime>> findByUserApiId(@Param("apiId") String apiId);

    @Query("select s from StudyTime s where s.user.username =:username and s.calendar.date =:date")
    Optional<List<StudyTime>> findByUsernameAndDate(@Param("username") String username,
                                                    @Param("date") LocalDate date);

    @Query("select s from StudyTime s where s.user.apiId =:apiId and s.calendar.date =:date")
    Optional<List<StudyTime>> findByUserApiIdAndDate(@Param("apiId") String apiId,
                                                    @Param("date") LocalDate date);
}
