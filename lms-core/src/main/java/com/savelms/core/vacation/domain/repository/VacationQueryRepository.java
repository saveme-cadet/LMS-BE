package com.savelms.core.vacation.domain.repository;

import com.savelms.core.vacation.domain.entity.Vacation;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.Optional;

public interface VacationQueryRepository {

    Optional<Vacation> findAllByDate(@Param("date") LocalDate date);
    Optional<Vacation> findFirstByUserApiId(String userApiId);
    Optional<Vacation> findFirstByUsername(String username);

}
