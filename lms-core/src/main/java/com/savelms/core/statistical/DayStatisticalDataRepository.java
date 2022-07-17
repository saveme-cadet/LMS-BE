package com.savelms.core.statistical;

import com.savelms.core.calendar.domain.entity.Calendar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.Optional;

import java.util.List;
import java.util.Optional;

public interface DayStatisticalDataRepository extends JpaRepository<DayStatisticalData, Long> {

    DayStatisticalData findByuser_idAndCalendar(Long id, Long calendar_id);

    @Query("select d from DayStatisticalData d where d.user.username =:username and d.calendar.date =:date")
    Optional<DayStatisticalData> findByUsernameAndDate(@Param("username") String username, LocalDate date);

    @Query("select d from DayStatisticalData d where d.user.apiId =:apiId and d.calendar.date =:date")
    Optional<DayStatisticalData> findByApiIdAndDate(@Param("apiId") String apiId, LocalDate date);

    DayStatisticalData findByuser_idAndCalendar_id(Long id, Long calendar_id);
    Optional<DayStatisticalData> findAllByUser_idAndCalendar_id(Long aLong, Long calendar_id);
}
