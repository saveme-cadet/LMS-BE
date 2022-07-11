package com.savelms.core.vacation.domain.repository;

import com.savelms.core.vacation.domain.entity.Vacation;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface VacationQueryRepository {

    Optional<Vacation> findFirstByUserId(Long userId);
    Optional<Vacation> findFirstByUsername(String username);

}
