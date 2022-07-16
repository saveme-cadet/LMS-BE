package com.savelms.core.statistical;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DayStatisticalDataRepository extends JpaRepository<DayStatisticalData, Long> {
    DayStatisticalData findByuser_idAndCalendar(Long id, Long calendar_id);


    Optional<DayStatisticalData> findAllByUserIdAndCalendarID(Long aLong, Long calendar_id);
}
