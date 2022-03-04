package com.larry.fc.finalproject.core.domain.entity.repository;

import java.time.LocalDate;

public interface CheckMapping {
    Short getCheckIn();
    Short getCheckOut();
    LocalDate getTableDay();
}
