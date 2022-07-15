package com.savelms.core.statistical;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.Optional;

public interface DayStatisticalDataRepository extends JpaRepository<DayStatisticalData, Long> {
    DayStatisticalData findByuser_idAndCalendar(Long id, Long calendar_id);

    @Query("select d from DayStatisticalData d where d.user.username =:username and d.calendar.date =:date")
    Optional<DayStatisticalData> findByUsernameAndDate(@Param("username") String username, LocalDate date);
}
