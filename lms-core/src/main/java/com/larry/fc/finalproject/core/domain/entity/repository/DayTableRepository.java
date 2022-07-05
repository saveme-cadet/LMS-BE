package com.larry.fc.finalproject.core.domain.entity.repository;

import com.larry.fc.finalproject.core.domain.entity.DayTable;
import com.larry.fc.finalproject.core.domain.entity.Todo;
import org.apache.tomcat.jni.Local;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.Month;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;


public interface DayTableRepository extends JpaRepository<DayTable, Long> {

    List<DayTable> deleteDayTableByCadet_Id(Long id);
    @Transactional
    @Modifying
    List<DayTable> deleteDayTableByTableDayAndCadet_Id(@Param("table_day")LocalDate date,
                                                       @Param("cadet_id") Long id);
    List<DayTable> removeAllByTableDayAndCadet_Id(LocalDate date, Long id);
    Optional<DayTable> findAllByCadet_IdAndTableDay(Long id, LocalDate date);
    Optional<DayTable> findAllOptionalByCadet_IdAndAttendeStatus(Long id, Long attend );
    List<DayTable> findAllByCadet_Id(Long id);
    Optional<DayTable> findAllByTableDayAndCadet_Id(LocalDate date, Long id);
    DayTable findByTableDayAndCadet_IdAndAttendeStatus(LocalDate date, Long id, Long status);
    Boolean existsByTableDayAndCadet_Id(LocalDate date, Long id);
    List<DayMapping> findByCadet_IdAndAttendeStatus(Long id, Long attend);
    List<CheckMapping> findAllByCadet_IdAndAttendeStatus(Long id, Long attend);

    Optional<DayTable> findDayTableByCadet_Id(Long id);
    Optional<DayTable> findOptionalByCadet_IdAndTableDay(Long id, LocalDate date);
}