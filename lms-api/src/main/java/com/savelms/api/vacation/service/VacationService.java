package com.savelms.api.vacation.service;

import com.savelms.api.vacation.dto.AddVacationRequest;
import com.savelms.api.vacation.dto.UseVacationRequest;
import com.savelms.api.vacation.dto.VacationReasonResponse;
import com.savelms.api.vacation.dto.VacationResponse;
import com.savelms.core.exception.ExceptionStatus;
import com.savelms.core.exception.user.UserException;
import com.savelms.core.exception.vacation.VacationException;
import com.savelms.core.user.AttendStatus;
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
     * 1. 사용가능한 휴가가 남았는지 확인
     *   1-1. if 휴가가 없으면 예외 던지기 (throw new)
     * 2. 현재 남은 휴가에서 사용할 휴가수를 뺌
     * 3. 새로구한 남은 휴가수랑 휴가 사용이유로 새 엔티티 생성해서 테이블에 저장
     * */
    public VacationResponse useVacation(UseVacationRequest vacationRequest, String userApiId) {
        Vacation vacation = vacationRepository.findFirstByUserApiId(userApiId)
                .orElseThrow(() -> new UserException(ExceptionStatus.USER_NOT_FOUND));

        validateRemainingVacation(vacation.getRemainingDays(), vacationRequest.getUsedDays());
        double remainingDays = vacation.getRemainingDays() - vacationRequest.getUsedDays();

        return createVacation(remainingDays, 0.0, vacationRequest.getUsedDays(), vacationRequest.getReason(), userApiId);
    }

    public VacationResponse addVacation(AddVacationRequest vacationRequest, String userApiId) {
        Double remainingDays = vacationRequest.getAddedDays();
        Optional<Vacation> vacationOptional = vacationRepository.findFirstByUserApiId(userApiId);

        if (vacationOptional.isPresent()) {
            remainingDays += vacationOptional.get().getRemainingDays();
        }

        return createVacation(remainingDays, vacationRequest.getAddedDays(), 0.0, "", userApiId);
    }

    private VacationResponse createVacation(Double remainingDays, Double addedDays, Double usedDays, String reason, String userApiId) {
        User user = userRepository.findByApiId(userApiId)
                .orElseThrow(() -> new UserException(ExceptionStatus.USER_NOT_FOUND));

        Vacation vacation = Vacation.of(remainingDays, addedDays, usedDays, reason, user);
        vacationRepository.save(vacation);

        return new VacationResponse(vacation);
    }

    private void validateRemainingVacation(Double remainingDays, Double usedDays) {
        if ((remainingDays - usedDays) < 0) {
            throw new VacationException(ExceptionStatus.VACATION_NOT_FOUND);
        }
    }


    /**
     * 조회
     * */
    public Map<Long, Double> getRemainingVacationByDateAndAttendStatus(LocalDate date) {
        List<Vacation> vacations = vacationRepository.findAllByDateAttendStatus(date);
        Map<Long, Double> allRemainingVacation = new HashMap<>();

        Map<User, List<Vacation>> collect = vacations.stream().collect(Collectors.groupingBy(Vacation::getUser));
        for (List<Vacation> userVacations : collect.values()) {
            Vacation vacation = userVacations.stream().max(Comparator.comparing(Vacation::getCreatedAt)).get();
            allRemainingVacation.put(vacation.getUser().getId(), vacation.getRemainingDays());
        }

        return allRemainingVacation;
    }

    public VacationResponse getRemainingVacation(String userApiId) {
        Optional<Vacation> optional = vacationRepository.findFirstByUserApiId(userApiId);

        if (optional.isPresent()) {
            Vacation vacation = optional.get();

            return new VacationResponse(vacation);
        }

        return createVacation(0.0, 0.0, 0.0, "", userApiId);
    }

    public List<VacationReasonResponse> getUsedVacation(String userApiId) {
        List<Vacation> vacations = vacationRepository.findByApiIdAndUsedDaysNotZero(userApiId);

        return vacations.stream()
                .map(VacationReasonResponse::new)
                .collect(Collectors.toList());
    }
}