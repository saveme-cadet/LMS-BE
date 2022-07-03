package com.savelms.core.study_time.domain.repository;

import com.savelms.core.study_time.domain.entity.StudyTime;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface StudyTimeRepository extends JpaRepository<StudyTime, Long> {

    Optional<List<StudyTime>> findByUserId(Long userId);
    Optional<List<StudyTime>> findByUserIdAndIsStudying(Long userId, Boolean isStudying);

}
