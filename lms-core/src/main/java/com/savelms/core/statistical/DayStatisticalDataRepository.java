package com.savelms.core.statistical;

import com.savelms.core.user.AttendStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface DayStatisticalDataRepository extends JpaRepository<DayStatisticalData, Long> {

    DayStatisticalData findByuser_idAndCalendar_id(Long id, Long calendar_id);

    boolean existsByuser_idAndCalendar_id(Long id, Long calendar_id);

    Optional<DayStatisticalData> findAllByUser_idAndCalendar_id(Long aLong, Long calendar_id);

    @Query("select distinct d from DayStatisticalData d " +
            "join fetch d.user u " +
            "join fetch u.userRoles ur " +
            "join fetch ur.role r " +
            "where d.calendar.date =:date order by u.id asc")
    List<DayStatisticalData> findAllByDate(@Param("date") LocalDate date);

    @Query("select distinct d from DayStatisticalData d " +
            "join fetch d.user u " +
            "join fetch u.userRoles ur " +
            "join fetch ur.role r " +
            "where d.calendar.date =:date and u.attendStatus = :attendStatus " +
            "order by u.id asc")
    List<DayStatisticalData> findAllByDateAndAttendStatus(
            @Param("date") LocalDate date,
            @Param("attendStatus") AttendStatus attendStatus);

    @Query("select d from DayStatisticalData d where d.user.username = :username and d.calendar.date = :date")
    Optional<DayStatisticalData> findByUsernameAndDate(@Param("username") String username, @Param("date") LocalDate date);

    @Query("select d from DayStatisticalData d where d.user.apiId = :apiId and d.calendar.date = :date")
    Optional<DayStatisticalData> findByApiIdAndDate(@Param("apiId") String apiId, @Param("date") LocalDate date);



    @Query("select SUM(d.studyTimeScore) from DayStatisticalData d where d.user.username =:username " +
            "and SUBSTRING(d.calendar.date, 6, 2) =:month")
    Optional<Double> findTotalStudyTimePerMonth(@Param("username") String username, String month);

    @Modifying(clearAutomatically = true)
    @Query(value = "update day_statistical_data d " +
            "inner join User u on u.user_id = d.user_id " +
            "inner join Calendar c on c.calendar_id = d.calendar_id " +
            "set d.study_time_score = d.study_time_score + :studyScore, d.total_score = d.total_score + (:studyScore * -1) " +
            "where u.api_id = :apiId and c.date >= :date", nativeQuery = true)
    void bulkUpdateStudyTimeScore(@Param("apiId") String apiId,
                                  @Param("studyScore") double studyScore,
                                  @Param("date") LocalDate date);

    @Modifying(clearAutomatically = true)
    @Query(value = "update day_statistical_data d " +
            "inner join User u on u.user_id = d.user_id " +
            "inner join Calendar c on c.calendar_id = d.calendar_id " +
            "set d.study_time_score = :studyScore, d.total_score = :studyScore * -1 " +
            "where u.api_id = :apiId and c.date = :date", nativeQuery = true)
    void updateStudyTimeScore(@Param("apiId") String apiId,
                                  @Param("studyScore") double studyScore,
                                  @Param("date") LocalDate date);
}
