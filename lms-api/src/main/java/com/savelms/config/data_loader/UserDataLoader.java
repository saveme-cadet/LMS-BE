package com.savelms.config.data_loader;

import com.savelms.core.calendar.DayType;
import com.savelms.core.calendar.domain.entity.Calendar;
import com.savelms.core.calendar.domain.repository.CalendarRepository;
import com.savelms.core.team.TeamEnum;
import com.savelms.core.team.domain.entity.Team;
import com.savelms.core.team.domain.entity.UserTeam;
import com.savelms.core.team.domain.repository.TeamRepository;
import com.savelms.core.team.domain.repository.UserTeamRepository;
import com.savelms.core.user.authority.domain.entity.Authority;
import com.savelms.core.user.authority.domain.repository.AuthorityRepository;
import com.savelms.core.user.domain.entity.User;
import com.savelms.core.user.domain.repository.UserRepository;
import com.savelms.core.user.role.RoleEnum;
import com.savelms.core.user.role.domain.entity.Role;
import com.savelms.core.user.role.domain.entity.UserRole;
import com.savelms.core.user.role.domain.repository.RoleRepository;
import com.savelms.core.user.role.domain.repository.UserRoleRepository;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Arrays;
import javax.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

//테스트 케이스 입력용
@RequiredArgsConstructor
@Component
@Slf4j
public class UserDataLoader implements CommandLineRunner {

    private final EntityManager em;
    private final BCryptPasswordEncoder passwordEncoder;
    private final AuthorityRepository authorityRepository;
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;

    private final TeamRepository teamRepository;
    private final UserTeamRepository userTeamRepository;

    private final CalendarRepository calendarRepository;

    @Transactional
    public void loadSecurityData() {
        // Authority 생성
        Authority createUser = saveNewAuthority("user.create");
        Authority updateUser = saveNewAuthority("user.update");
        Authority updateUserRole = saveNewAuthority("user.role.update");
        Authority updateUserTeam = saveNewAuthority("user.team.update");
        Authority updateUserAttendStatus = saveNewAuthority("user.update-attend.status");
        Authority readUser = saveNewAuthority("user.read");
        Authority deleteUser = saveNewAuthority("user.delete");

        authorityRepository.saveAll(
            Arrays.asList(createUser, updateUser, updateUserRole, updateUserTeam,
                updateUserAttendStatus, readUser, deleteUser));

        //Authority createUser = saveNewAuthority("user.create");
        //Role 생성
        Role adminRole = saveNewRole(RoleEnum.ROLE_ADMIN);
        Role managerRole = saveNewRole(RoleEnum.ROLE_MANAGER);
        Role userRole = saveNewRole(RoleEnum.ROLE_USER);
        Role unauthorizedRole = saveNewRole(RoleEnum.ROLE_UNAUTHORIZED);

        adminRole.addAuthorities(createUser, updateUser, readUser, deleteUser);
        managerRole.addAuthorities(createUser, updateUser, readUser, deleteUser);
        userRole.addAuthorities(createUser, updateUser, readUser, deleteUser);
        unauthorizedRole.addAuthorities(createUser, updateUser, readUser, deleteUser);

        roleRepository.saveAll(Arrays.asList(adminRole, managerRole, userRole, unauthorizedRole));
        //Team 생성
        Team red = Team.builder()
            .value(TeamEnum.RED)
            .build();
        Team blue = Team.builder()
            .value(TeamEnum.BLUE)
            .build();

        teamRepository.saveAll(Arrays.asList(red, blue));


        //유저 생성
        User admin = User.createDefaultUser("admin", passwordEncoder.encode("admin"),
            "admin@gmail.com");

        User manager = User.createDefaultUser("manager", passwordEncoder.encode("manager"),
            "manager@gmail.com");

        User user = User.createDefaultUser("user", passwordEncoder.encode("user"),
            "user@gmail.com");

        User unauthorized = User.createDefaultUser("unauthorized",
            passwordEncoder.encode("unauthorized"),
            "unauthorized@gmail.com");

        //UserTeam 생성
        UserTeam.createUserTeam(admin, red, "initial", true);
        UserTeam.createUserTeam(manager, red, "initial", true);
        UserTeam.createUserTeam(user, red, "initial", true);
        UserTeam.createUserTeam(unauthorized, red, "initial", true);

        //UserRole 생성
        UserRole.createUserRole(admin, adminRole, "initial", true);
        UserRole.createUserRole(manager, managerRole, "initial", true);
        UserRole.createUserRole(user, userRole, "initial", true);
        UserRole.createUserRole(unauthorized, unauthorizedRole, "initial", true);

        userRepository.saveAll(Arrays.asList(admin, manager, user, unauthorized));

        캘린더_생성_7월();
    }


    private Role saveNewRole(RoleEnum roleEnum) {
        Role role = Role.builder().value(roleEnum).build();
        return roleRepository.save(role);
    }

    private Authority saveNewAuthority(String s) {
        return authorityRepository.save(
            Authority.builder().permission(s).build());
    }

    private void 캘린더_생성_7월() {
        //캘린더 데이터 저장
        LocalDate startDate = LocalDate.of(2022, 7, 1);
        LocalDate endDate = LocalDate.of(2022, 10, 1);

        for (LocalDate date = startDate; date.isBefore(endDate); date = date.plusDays(1)) {
            Calendar calendar = Calendar.builder()
                .date(date)
                .dayType( date.getDayOfWeek() == DayOfWeek.SATURDAY ||
                    date.getDayOfWeek() == DayOfWeek.SUNDAY ?
                    DayType.HOLIDAY : DayType.STUDYDAY)
                .build();
            calendarRepository.save(calendar);
        }
    }

    @Override
    public void run(String... args) throws Exception {
        if (authorityRepository.count() == 0) {
            loadSecurityData();
        }
    }
}
