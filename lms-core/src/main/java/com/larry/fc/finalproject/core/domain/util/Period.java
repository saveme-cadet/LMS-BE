package com.larry.fc.finalproject.core.domain.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class Period {
    private final LocalDate startAt;
    private final LocalDate endAt;

    public Period(LocalDate startAt, LocalDate endAt) {
        this.startAt = startAt;
        this.endAt = endAt;
    }

    public Period(LocalDate startAt){
        this.startAt = startAt;
        this.endAt = startAt;
    }

    public static Period of(LocalDate startAt, LocalDate endAt){
        return new Period(startAt, endAt);
    }


    public static Period of(LocalDate startAt){
        return new Period(startAt);
    }

    public boolean isOverlapped(LocalDate startAt) {
        return !this.startAt.isBefore(startAt);
    }

    public boolean isOverlapped(Period period){
        return Period.of(period.startAt).isOverlapped(period.startAt);
    }

    public boolean isOverlapped(LocalDate startAt, LocalDate endAt){
        return this.startAt.isBefore(endAt) && startAt.isBefore(this.endAt);
    }

    public LocalDate getStartAt() {
        return startAt;
    }

    public LocalDate getEndAt() {
        return endAt;
    }
}
