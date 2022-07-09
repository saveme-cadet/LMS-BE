package com.savelms.core.study_time.domain.repository;

import com.savelms.core.study_time.domain.entity.StudyTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface StudyTimeRepository extends JpaRepository<StudyTime, Long> {

    Optional<List<StudyTime>> findByUserId(Long userId);
    Optional<List<StudyTime>> findByUserIdAndIsStudying(Long userId, Boolean isStudying);

    @Query(value = "select s.* from study_time s where s.user_id = :userId and DATE(s.created_at) = DATE(:createAt)",
            nativeQuery = true)
    Optional<List<StudyTime>> findByUserIdAndCreatedDate(@Param("userId") Long userId,
                                                         @Param("createAt") String createAt);

}
