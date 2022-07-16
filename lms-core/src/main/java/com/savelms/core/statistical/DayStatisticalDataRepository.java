package com.savelms.core.statistical;

import com.savelms.core.calendar.domain.entity.Calendar;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DayStatisticalDataRepository extends JpaRepository<DayStatisticalData, Long> {
    DayStatisticalData findByuser_idAndCalendar_id(Long id, Long calendar_id);
    Optional<DayStatisticalData> findAllByUser_idAndCalendar_id(Long aLong, Long calendar_id);
}
