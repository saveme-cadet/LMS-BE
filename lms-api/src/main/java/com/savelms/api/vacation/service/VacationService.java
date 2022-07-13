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

import java.util.List;
import java.util.Optional;
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
    public VacationResponse createVacation(Long remainingDays, Long usedDays, String reason, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자가 존재하지 않습니다."));

        Vacation vacation = Vacation.of(remainingDays, usedDays, reason, user);
        vacationRepository.save(vacation);

        return new VacationResponse(vacation);
    }

    public VacationResponse createVacation(Long remainingDays, Long usedDays, String reason, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("사용자가 존재하지 않습니다."));

        Vacation vacation = Vacation.of(remainingDays, usedDays, reason, user);
        vacationRepository.save(vacation);

        return new VacationResponse(vacation);
    }

    public VacationResponse useVacation(UseVacationRequest vacationRequest, String username) {
        Vacation vacation = vacationRepository.findFirstByUsername(username)
                .orElseThrow(() -> new VacationNotFoundException("존재하는 휴가가 없습니다."));

        checkRemainingDays(vacation.getRemainingDays(), vacationRequest.getUsedDays());
        long remainingDays = vacation.getRemainingDays() - vacationRequest.getUsedDays();

        return createVacation(remainingDays, vacationRequest.getUsedDays(), vacationRequest.getReason(), username);
    }

    private void checkRemainingDays(Long remainingDays, Long usedDays) {
        if ((remainingDays - usedDays) < 0) {
            throw new VacationNotFoundException("사용할 휴가가 부족합니다.");
        }
    }


    /**
     * 조회
     * */
    public VacationResponse getRemainingVacation(String username) {
        Optional<Vacation> optional = vacationRepository.findFirstByUsername(username);

        if (optional.isPresent()) {
            Vacation vacation = optional.get();

            return new VacationResponse(vacation);
        }

        return createVacation(0L, 0L, "", username);
    }

    public List<VacationReasonResponse> getUsedVacation(String username) {
        List<Vacation> vacations = vacationRepository.findByUsernameAndUsedDaysNotZero(username)
                .orElseThrow(() -> new VacationNotFoundException("사용한 휴가가 없습니다."));

        return vacations.stream()
                .map(VacationReasonResponse::new)
                .collect(Collectors.toList());
    }


    /**
     * 수정
     * */
    public VacationResponse addVacation(AddVacationRequest vacationRequest, Long userId) {
        Optional<Vacation> optional = vacationRepository.findFirstByUserId(userId);

        if (optional.isPresent()) {
            Vacation vacation = optional.get();
            vacation.addVacationDays(vacationRequest.getAddedDays());

            return new VacationResponse(vacation);
        }

        return createVacation(vacationRequest.getAddedDays(), 0L, "", userId);
    }

}
