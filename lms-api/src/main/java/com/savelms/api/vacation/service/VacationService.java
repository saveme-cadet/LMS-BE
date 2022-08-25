package com.savelms.api.vacation.service;

import com.savelms.api.vacation.dto.AddVacationRequest;
import com.savelms.api.vacation.dto.UseVacationRequest;
import com.savelms.api.vacation.dto.VacationReasonResponse;
import com.savelms.api.vacation.dto.VacationResponse;
import com.savelms.core.exception.VacationNotFoundException;
import com.savelms.core.user.domain.entity.User;
import com.savelms.core.user.domain.repository.UserRepository;
import com.savelms.core.vacation.domain.entity.Vacation;
import com.savelms.core.vacation.domain.repository.VacationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class VacationService {

    private final UserRepository userRepository;
    private final VacationRepository vacationRepository;


    /**
     * 생성
     * */
    public VacationResponse useVacation(UseVacationRequest vacationRequest, String userApiId) {
        Vacation vacation = vacationRepository.findFirstByUserApiId(userApiId)
                .orElseThrow(() -> new VacationNotFoundException("존재하는 휴가가 없습니다."));

        checkRemainingDays(vacation.getRemainingDays(), vacationRequest.getUsedDays());
        long remainingDays = vacation.getRemainingDays() - vacationRequest.getUsedDays();

        return createVacation(remainingDays, 0L, vacationRequest.getUsedDays(), vacationRequest.getReason(), userApiId);
    }

    public VacationResponse addVacation(AddVacationRequest vacationRequest, String userApiId) {
        Long remainingDays = vacationRequest.getAddedDays();
        Optional<Vacation> vacationOptional = vacationRepository.findFirstByUserApiId(userApiId);

        if (vacationOptional.isPresent()) {
            remainingDays += vacationOptional.get().getRemainingDays();
        }

        return createVacation(remainingDays, vacationRequest.getAddedDays(), 0L, "", userApiId);
    }

    private VacationResponse createVacation(Long remainingDays, Long addedDays, Long usedDays, String reason, String userApiId) {
        User user = userRepository.findByApiId(userApiId)
                .orElseThrow(() -> new IllegalArgumentException("사용자가 존재하지 않습니다."));

        Vacation vacation = Vacation.of(remainingDays, addedDays, usedDays, reason, user);
        vacationRepository.save(vacation);

        return new VacationResponse(vacation);
    }

    private void checkRemainingDays(Long remainingDays, Long usedDays) {
        if ((remainingDays - usedDays) < 0) {
            throw new VacationNotFoundException("사용할 휴가가 부족합니다.");
        }
    }


    /**
     * 조회
     * */
    public List<Map<Long, Long>> getAllRemainingVacationByDate(LocalDate date) {
        List<Vacation> vacations = vacationRepository.findAllByDate(date);
        List<Map<Long, Long>> allRemainingVacation = new ArrayList<>();

        Map<User, List<Vacation>> collect = vacations.stream().collect(Collectors.groupingBy(Vacation::getUser));
        for (List<Vacation> userVacations : collect.values()) {
            Vacation vacation = userVacations.stream().max(Comparator.comparing(Vacation::getCreatedAt)).get();
            allRemainingVacation.add(Map.of(vacation.getUser().getId(), vacation.getRemainingDays()));
        }

        return allRemainingVacation;
    }

    public VacationResponse getRemainingVacation(String userApiId) {
        Optional<Vacation> optional = vacationRepository.findFirstByUserApiId(userApiId);

        if (optional.isPresent()) {
            Vacation vacation = optional.get();

            return new VacationResponse(vacation);
        }

        return createVacation(0L, 0L, 0L, "", userApiId);
    }

    public List<VacationReasonResponse> getUsedVacation(String userApiId) {
        List<Vacation> vacations = vacationRepository.findByApiIdAndUsedDaysNotZero(userApiId);

        return vacations.stream()
                .map(VacationReasonResponse::new)
                .collect(Collectors.toList());
    }
}