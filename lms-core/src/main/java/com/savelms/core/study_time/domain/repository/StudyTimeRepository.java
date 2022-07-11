package com.savelms.core.study_time.domain.repository;

import com.savelms.core.study_time.domain.entity.StudyTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface StudyTimeRepository extends JpaRepository<StudyTime, Long> {

    Optional<List<StudyTime>> findByUserId(Long userId);

    @Query("select s from StudyTime s join fetch s.user u where s.isStudying =:isStudying")
    Optional<List<StudyTime>> findByIsStudying(Boolean isStudying);

    @Query("select s from StudyTime s inner join s.user u where u.username =:username and s.isStudying =:isStudying")
    Optional<List<StudyTime>> findByUsernameAndIsStudying(String username, Boolean isStudying);

    @Query("select s from StudyTime s inner join s.user u where u.username =:username")
    Optional<List<StudyTime>> findByUsername(String username);

    @Query(value = "SELECT s.* FROM study_time s WHERE s.user_id = :userId " +
            "AND DATE(s.created_at) = DATE(:createAt)", nativeQuery = true)
    Optional<List<StudyTime>> findByUserIdAndCreatedDate(@Param("userId") Long userId,
                                                         @Param("createAt") String createAt);

    @Query(value = "SELECT s.* FROM study_time s JOIN user u WHERE u.username = :username " +
            "AND DATE(s.created_at) = DATE(:createAt)", nativeQuery = true)
    Optional<List<StudyTime>> findByUsernameAndCreatedDate(@Param("username") String username,
                                                           @Param("createAt") String createAt);

}
