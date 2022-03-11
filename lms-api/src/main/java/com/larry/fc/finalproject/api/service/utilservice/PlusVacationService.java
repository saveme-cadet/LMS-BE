package com.larry.fc.finalproject.api.service.utilservice;

import com.larry.fc.finalproject.api.dto.AuthUser;
import com.larry.fc.finalproject.api.dto.PlusVacationDto;
import com.larry.fc.finalproject.api.util.DtoConverter;
import com.larry.fc.finalproject.core.domain.entity.PlusVacation;
import com.larry.fc.finalproject.core.domain.entity.repository.PlusVacationRepository;
import com.larry.fc.finalproject.core.domain.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Service
@RequiredArgsConstructor
public class PlusVacationService {

    private final PlusVacationRepository plusVacationRepository;
    private final UserService userService;

    public void create(AuthUser authUser) {
        final PlusVacation plusVacation = PlusVacation.gainServent(
                userService.findByUserId(authUser.getId()));
        plusVacationRepository.save(plusVacation);
    }

    public List<PlusVacationDto> getUserInfoByDay(AuthUser authUser) {
        final Stream<PlusVacationDto> userInfo = plusVacationRepository.findByUserId(authUser.getId()) // wirter 로 수정
                .stream()
                .map(userInfo1 -> DtoConverter.fromPlusVacation(userInfo1));
        final List<PlusVacationDto> response = userInfo.collect(Collectors.toList());
        return response;
    }

    public void update(PlusVacationDto todoDto, AuthUser authUser) {
        PlusVacation plusVacation = DtoConverter.fromPlusVacationDto(todoDto);
        final Optional<PlusVacation> original = plusVacationRepository.findAllByUserId(authUser.getId());

        original.ifPresent(todo1 -> {
            todo1.setStartAt(plusVacation.getStartAt());
            todo1.setEndAt(plusVacation.getEndAt());
            plusVacationRepository.save(todo1);
        });
    }
}
