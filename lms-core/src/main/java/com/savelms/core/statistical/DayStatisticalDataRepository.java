package com.savelms.core.statistical;

import org.springframework.data.jpa.repository.JpaRepository;

public interface DayStatisticalDataRepository extends JpaRepository<DayStatisticalData, Long> {
    DayStatisticalData findByuser_idAndCalendar(Long id, Long calendar_id);
}
