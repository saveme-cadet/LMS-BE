package com.savelms.core.calendar.domain.repository;


import com.savelms.core.calendar.domain.entity.Calendar;
import java.time.LocalDate;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CalendarRepository extends JpaRepository<Calendar, Long> {

    Optional<Calendar> findByDate(LocalDate date);
    Calendar findAllByDate(LocalDate date);
}

