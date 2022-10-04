package com.savelms.core.vacation.domain.repository;

import com.savelms.core.user.AttendStatus;
import com.savelms.core.vacation.domain.entity.Vacation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface VacationRepository extends JpaRepository<Vacation, Long>, VacationQueryRepository {

    @Query(value = "select v.*, u.* from vacation v join user u on u.user_id = v.user_id " +
            "where date(v.created_at) <= :date order by v.vacation_id desc", nativeQuery = true)
    List<Vacation> findAllByDate(@Param("date") LocalDate date);

    @Query("select v from Vacation v where v.user.username = :username and v.usedDays <> 0")
    List<Vacation> findByUsernameAndUsedDaysNotZero(@Param("username") String username);

    @Query("select v from Vacation v where v.user.apiId = :apiId and v.usedDays <> 0")
    List<Vacation>findByApiIdAndUsedDaysNotZero(@Param("apiId") String apiId);

    @Query(value = "select v.*, u.* from vacation v " +
            "join user u on u.user_id = v.user_id " +
            "where date(v.created_at) <= :date and u.attend_status = :attendStatus " +
            "order by v.vacation_id desc", nativeQuery = true)
    List<Vacation> findAllByDateAttendStatus(@Param("date") LocalDate date, @Param("attendStatus") String attendStatus);

    //List<Vacation> findAllBy
}
