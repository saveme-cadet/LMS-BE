package com.larry.fc.finalproject.core.domain.entity.repository;

import com.larry.fc.finalproject.core.domain.entity.DayTable;
import com.larry.fc.finalproject.core.domain.entity.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface UserInfoRepository extends JpaRepository<UserInfo, Long>{
    //List<UserInfo> findAllById(Long id);
    List<UserInfo> findAllByWriter_Id(Long id);
    UserInfo findUserInfosByWriter_Id(Long id);
    UserInfo findByWriter_IdAndAttendeStatus(Long id, Long attend);
    Optional<UserInfo> findByWriter_Id(Long id);
    List<UserInfo> findAllByAttendeStatus(Long attendStatus);
    List<UserInfo> deleteUserInfoByWriter_Id(Long id);
    List<IdMapping> findAllBy();
    Boolean existsUserInfoByAttendeStatusAndWriter_Id(Long status, Long id);
    String findRoleByWriter_Id(Long id);
    String findByAttendeStatusAndWriter_Id(Long id, Long is);
    String findTeamByWriter_Id(Long id);
}
