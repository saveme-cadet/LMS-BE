package com.larry.fc.finalproject.core.domain.service;

import com.larry.fc.finalproject.core.domain.dto.UserCreateReq;
import com.larry.fc.finalproject.core.domain.entity.User;
import com.larry.fc.finalproject.core.domain.entity.repository.*;
import com.larry.fc.finalproject.core.domain.util.BCryptEncryptor;
import com.larry.fc.finalproject.core.domain.util.Encryptor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final Encryptor encryptor;
    private final UserRepository userRepository;
    private final UserInfoRepository userInfoRepository;
    private final DayTableRepository dayTableRepository;
    private final TodoRepository todoRepository;
    private final UserStatisticalChartRepository userStatisticalChartRepository;
    private final PlusVacationRepository plusVacationRepository;

    @Transactional
    public User create(UserCreateReq userCreateReq){
        userRepository.findByEmail(userCreateReq.getEmail())
                .ifPresent(u -> {
                    throw new RuntimeException("user already exit!");
                });
        return userRepository.save(new User(
                userCreateReq.getName(),
                userCreateReq.getEmail(),
                encryptor.encrypt(userCreateReq.getPassword()),
                userCreateReq.getBirthday()
        ));
    }


    @Transactional
    public Optional<User> findPwMatchUser(String email, String password) {
        return userRepository.findByEmail(email)
                .map(user -> user.isMatch(encryptor, password) ? user : null);
    }

    @Transactional
    public User findByUserId(Long userId){
        return userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("no user by id"));
    }

    @Transactional
    public void delete(Long userId){
        try{
           userInfoRepository.deleteUserInfoByWriter_Id(userId);
            dayTableRepository.deleteDayTableByCadet_Id(userId);
            todoRepository.deleteTodoByWriter_Id(userId);
            userStatisticalChartRepository.deleteStatisticalChartByWriter_Id(userId);
            plusVacationRepository.deletePlusVacationByUserId(userId);
            userRepository.deleteById(userId);
        } catch (Exception e){
            log.error("error deleting entity Todo", userId, e);
            throw new RuntimeException("error deleting entity Todo " + userId);
        }
    }
}
