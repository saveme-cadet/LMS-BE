package com.savelms.core.vacation.domain.repository;

import com.savelms.core.vacation.domain.entity.Vacation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface VacationRepository extends JpaRepository<Vacation, Long>, VacationQueryRepository {

    @Query("select v from Vacation v where v.user.username = :username and v.usedDays <> 0")
    List<Vacation> findByUsernameAndUsedDaysNotZero(@Param("username") String username);

    @Query("select v from Vacation v where v.user.apiId = :apiId and v.usedDays <> 0")
    List<Vacation>findByApiIdAndUsedDaysNotZero(@Param("apiId") String apiId);
}
