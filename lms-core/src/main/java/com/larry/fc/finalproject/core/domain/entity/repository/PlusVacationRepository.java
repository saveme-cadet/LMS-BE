package com.larry.fc.finalproject.core.domain.entity.repository;

import com.larry.fc.finalproject.core.domain.entity.PlusVacation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PlusVacationRepository extends JpaRepository<PlusVacation, Long> {
    Optional<PlusVacation> findAllByUserId(Long id);
    List<PlusVacation> findByUserId(Long id);
}
