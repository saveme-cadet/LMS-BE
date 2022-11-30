package com.savelms.core.monthreport;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.stream.Stream;

public interface MonthReportRepository extends JpaRepository<MonthReport, Long> {
   // Stream<MonthReport> findAll();
}
