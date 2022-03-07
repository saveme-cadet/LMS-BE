package com.larry.fc.finalproject.core.domain.entity.repository;

import com.larry.fc.finalproject.core.domain.entity.StatisticalChart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserStatisticalChartRepository extends JpaRepository<StatisticalChart, Long> {
    List<StatisticalChart> findByWriter_Id(Long id);
    List<StatisticalChart> deleteStatisticalChartByWriter_Id(Long id);

    Optional<StatisticalChart> findAllByWriter_Id(Long id);
}
