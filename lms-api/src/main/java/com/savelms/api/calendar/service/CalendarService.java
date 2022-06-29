package com.savelms.api.calendar.service;

import com.savelms.core.calendar.DayType;
import com.savelms.core.calendar.domain.entity.Calendar;
import com.savelms.core.calendar.domain.repository.CalendarRepository;
import java.time.LocalDate;
import javax.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class CalendarService {

    private final CalendarRepository calendarRepository;

    public Calendar findByDate(LocalDate date) {
        return calendarRepository.findByDate(date)
            .orElseThrow(() ->
                new EntityNotFoundException("없는 날짜입니다. " + date));
    }

    public Calendar findById(Long calendarId) {
        return calendarRepository.findById(calendarId)
            .orElseThrow(() ->
                new EntityNotFoundException(
                    calendarId
                        + " 에 해당하는 "
                        + Calendar.class.getSimpleName()
                        + "가 없습니다."));
    }

    @Transactional
    public Long updateDayType(Long calendarId, DayType updateDayType) {
        Calendar calendar = findById(calendarId);
        calendar.changeDayType(updateDayType);
        return calendar.getId();
    }


}
