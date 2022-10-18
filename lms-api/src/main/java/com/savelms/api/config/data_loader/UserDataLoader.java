package com.savelms.api.config.data_loader;

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
//         Authority 생성
        Authority createUser = saveNewAuthority("user.create");
        Authority updateUser = saveNewAuthority("user.update");
        Authority updateUserRole = saveNewAuthority("user.role.update");
        Authority updateUserTeam = saveNewAuthority("user.team.update");
        Authority updateUserAttendStatus = saveNewAuthority("user.attend-status.update");
        Authority readUser = saveNewAuthority("user.read");
        Authority deleteUser = saveNewAuthority("user.delete");


        Authority createTodo = saveNewAuthority("todo.create");
        Authority createUserTodo = saveNewAuthority("user.todo.create");
        Authority updateTodo = saveNewAuthority("todo.update");
        Authority updateUserTodo = saveNewAuthority("user.todo.update");
        Authority readTodo = saveNewAuthority("todo.read");
        Authority readUserTodo = saveNewAuthority("user.todo.read");
        Authority deleteTodo = saveNewAuthority("todo.delete");
        Authority deleteUserTodo = saveNewAuthority("user.todo.delete");


        Authority createVacation = saveNewAuthority("vacation.create");
        Authority createUserVacation = saveNewAuthority("user.vacation.create");
        Authority updateVacation = saveNewAuthority("vacation.update");
        Authority updateUserVacation = saveNewAuthority("user.vacation.update");
        Authority readVacation = saveNewAuthority("vacation.read");
        Authority readUserVacation = saveNewAuthority("user.vacation.read");
        Authority deleteVacation = saveNewAuthority("vacation.delete");
        Authority deleteUserVacation = saveNewAuthority("user.vacation.delete");



        Authority createStudyTime = saveNewAuthority("study-time.create");
        Authority updateStudyTime = saveNewAuthority("study-time.update");
        Authority readStudyTime = saveNewAuthority("study-time.read");
        Authority deleteStudyTime = saveNewAuthority("study-time.delete");

        Authority createUserStudyTime = saveNewAuthority("user.study-time.create");
        Authority updateUserStudyTime = saveNewAuthority("user.study-time.update");
        Authority readUserStudyTime = saveNewAuthority("user.study-time.read");
        Authority readStudyTimeUser = saveNewAuthority("study-time.user.read");
        Authority deleteUserStudyTime = saveNewAuthority("user.study-time.delete");

        Authority createAttendance = saveNewAuthority("attendance.create");
        Authority updateAttendance = saveNewAuthority("attendance.update");
        Authority readAttendance = saveNewAuthority("attendance.read");
        Authority deleteAttendance = saveNewAuthority("attendance.delete");

        Authority createUserAttendance = saveNewAuthority("user.attendance.create");
        Authority updateUserAttendance = saveNewAuthority("user.attendance.update");
        Authority readUserAttendance = saveNewAuthority("user.attendance.read");
        Authority deleteUserAttendance = saveNewAuthority("user.attendance.delete");

        Authority readDayLog = saveNewAuthority("day-log.read");

        authorityRepository.saveAll(Arrays.asList(
            createUser, updateUser, updateUserRole, updateUserTeam, updateUserAttendStatus, readUser, deleteUser,
            createTodo, createUserTodo, updateTodo, updateUserTodo, readTodo, readUserTodo, deleteTodo, deleteUserTodo,
            createVacation, createUserVacation, updateVacation, updateUserVacation, readVacation, readUserVacation, deleteVacation, deleteUserVacation,
            createStudyTime, updateStudyTime, readStudyTime, deleteStudyTime,
            createUserStudyTime, updateUserStudyTime, readUserStudyTime, readStudyTimeUser, deleteUserStudyTime,
            createAttendance, updateAttendance, readAttendance, deleteAttendance,
            createUserAttendance, updateUserAttendance, readUserAttendance, deleteUserAttendance,
            readDayLog
        ));

        //있는 것 찾음
//        Authority createUser = authorityRepository.findByPermission("user.create").get();
//        Authority updateUser = authorityRepository.findByPermission("user.update").get();
//        Authority updateUserRole = authorityRepository.findByPermission("user.role.update").get();
//        Authority updateUserTeam = authorityRepository.findByPermission("user.team.update").get();
//        Authority updateUserAttendStatus = authorityRepository.findByPermission("user.attend-status.update").get();
//        Authority readUser = authorityRepository.findByPermission("user.read").get();
//        Authority deleteUser = authorityRepository.findByPermission("user.delete").get();
//
//
//        Authority createTodo = authorityRepository.findByPermission("todo.create").get();
//        Authority createUserTodo = authorityRepository.findByPermission("user.todo.create").get();
//        Authority updateTodo = authorityRepository.findByPermission("todo.update").get();
//        Authority updateUserTodo = authorityRepository.findByPermission("user.todo.update").get();
//        Authority readTodo = authorityRepository.findByPermission("todo.read").get();
//        Authority readUserTodo = authorityRepository.findByPermission("user.todo.read").get();
//        Authority deleteTodo = authorityRepository.findByPermission("todo.delete").get();
//        Authority deleteUserTodo = authorityRepository.findByPermission("user.todo.delete").get();
//
//
//        Authority createVacation = authorityRepository.findByPermission("vacation.create").get();
//        Authority createUserVacation = authorityRepository.findByPermission("user.vacation.create").get();
//        Authority updateVacation = authorityRepository.findByPermission("vacation.update").get();
//        Authority updateUserVacation = authorityRepository.findByPermission("user.vacation.update").get();
//        Authority readVacation = authorityRepository.findByPermission("vacation.read").get();
//        Authority readUserVacation = authorityRepository.findByPermission("user.vacation.read").get();
//        Authority deleteVacation = authorityRepository.findByPermission("vacation.delete").get();
//        Authority deleteUserVacation = authorityRepository.findByPermission("user.vacation.delete").get();
//
//
//
//        Authority createStudyTime = authorityRepository.findByPermission("study-time.create").get();
//        Authority updateStudyTime = authorityRepository.findByPermission("study-time.update").get();
//        Authority readStudyTime = authorityRepository.findByPermission("study-time.read").get();
//        Authority deleteStudyTime = authorityRepository.findByPermission("study-time.delete").get();
//
//        Authority createUserStudyTime = authorityRepository.findByPermission("user.study-time.create").get();
//        Authority updateUserStudyTime = authorityRepository.findByPermission("user.study-time.update").get();
//        Authority readUserStudyTime = authorityRepository.findByPermission("user.study-time.read").get();
//        Authority readStudyTimeUser = authorityRepository.findByPermission("study-time.user.read").get();
//        Authority deleteUserStudyTime = authorityRepository.findByPermission("user.study-time.delete").get();
//
//        Authority createAttendance = authorityRepository.findByPermission("attendance.create").get();
//        Authority updateAttendance = authorityRepository.findByPermission("attendance.update").get();
//        Authority readAttendance = authorityRepository.findByPermission("attendance.read").get();
//        Authority deleteAttendance = authorityRepository.findByPermission("attendance.delete").get();
//
//        Authority createUserAttendance = authorityRepository.findByPermission("user.attendance.create").get();
//        Authority updateUserAttendance = authorityRepository.findByPermission("user.attendance.update").get();
//        Authority readUserAttendance = authorityRepository.findByPermission("user.attendance.read").get();
//        Authority deleteUserAttendance = authorityRepository.findByPermission("user.attendance.delete").get();
//
//        Authority readDayLog = authorityRepository.findByPermission("day-log.read").get();


        //Authority createUser = saveNewAuthority("user.create");
        //Role 생성
        Role adminRole = saveNewRole(RoleEnum.ROLE_ADMIN);
        Role managerRole = saveNewRole(RoleEnum.ROLE_MANAGER);
        Role userRole = saveNewRole(RoleEnum.ROLE_USER);
        Role unauthorizedRole = saveNewRole(RoleEnum.ROLE_UNAUTHORIZED);

//        Role adminRole = roleRepository.findByValue(RoleEnum.ROLE_ADMIN).get();
//        Role managerRole = roleRepository.findByValue(RoleEnum.ROLE_MANAGER).get();
//        Role userRole = roleRepository.findByValue(RoleEnum.ROLE_USER).get();
//        Role unauthorizedRole = roleRepository.findByValue(RoleEnum.ROLE_UNAUTHORIZED).get();

        adminRole.getAuthorities().clear();
        adminRole.addAuthorities(createUser, updateUser,updateUserRole, updateUserTeam, updateUserAttendStatus,  readUser, deleteUser,
            createTodo, createUserTodo, updateTodo, updateUserTodo, readTodo, readUserTodo, deleteTodo, deleteUserTodo,
            createVacation, createUserVacation, updateVacation, updateUserVacation, readVacation, readUserVacation, deleteVacation, deleteUserVacation,
            createStudyTime, updateStudyTime, readStudyTime, deleteStudyTime,
            createUserStudyTime, updateUserStudyTime, readUserStudyTime, readStudyTimeUser, deleteUserStudyTime,
            createAttendance, updateAttendance, readAttendance, deleteAttendance, createUserAttendance, updateUserAttendance, readUserAttendance, deleteUserAttendance,
            readDayLog);
        managerRole.getAuthorities().clear();

        managerRole.addAuthorities(createUser, updateUser, updateUserRole, updateUserTeam, updateUserAttendStatus,  readUser,
            createUserTodo, updateUserTodo, readTodo, readUserTodo, deleteUserTodo,
            createVacation, createUserVacation, updateVacation, updateUserVacation, readVacation, readUserVacation, deleteVacation, deleteUserVacation,
            createUserStudyTime, updateUserStudyTime, readUserStudyTime, readStudyTimeUser, deleteUserStudyTime,
                            updateAttendance, readAttendance, deleteAttendance, createUserAttendance, updateUserAttendance, readUserAttendance,
            readDayLog);
        userRole.getAuthorities().clear();
        userRole.addAuthorities(createUser, readUser, updateUser,
            createUserTodo, updateUserTodo, readTodo, readUserTodo, deleteUserTodo,
            createUserVacation, updateUserVacation,readUserVacation,
            createUserStudyTime, updateUserStudyTime, readUserStudyTime, readStudyTimeUser, deleteUserStudyTime,
                             updateAttendance, readAttendance, deleteAttendance, createUserAttendance, updateUserAttendance, readUserAttendance,
            readDayLog);
        unauthorizedRole.getAuthorities().clear();
        unauthorizedRole.addAuthorities(createUser, updateUser);

        roleRepository.saveAll(Arrays.asList(adminRole, managerRole, userRole, unauthorizedRole));
        //Team 생성
        Team red = Team.builder()
            .value(TeamEnum.RED)
            .build();
        Team blue = Team.builder()
            .value(TeamEnum.BLUE)
            .build();
        Team none = Team.builder()
            .value(TeamEnum.NONE)
            .build();

        teamRepository.saveAll(Arrays.asList(red, blue, none));


        //유저 생성
        User admin = User.createDefaultUser("admin", passwordEncoder.encode("42Seoul!"),
            "admin@gmail.com");

        User manager = User.createDefaultUser("manager", passwordEncoder.encode("42Seoul!"),
            "manager@gmail.com");

        User user = User.createDefaultUser("user", passwordEncoder.encode("42Seoul!"),
            "user@gmail.com");

        User unauthorized = User.createDefaultUser("unauthorized",
            passwordEncoder.encode("42Seoul!"),
            "unauthorized@gmail.com");

        //UserTeam 생성
        UserTeam.createUserTeam(admin, none, "initial", true);
        UserTeam.createUserTeam(manager, none, "initial", true);
        UserTeam.createUserTeam(user, none, "initial", true);
        UserTeam.createUserTeam(unauthorized, none, "initial", true);

        //UserRole 생성
        UserRole.createUserRole(admin, adminRole, "initial", true);
        UserRole.createUserRole(manager, managerRole, "initial", true);
        UserRole.createUserRole(user, userRole, "initial", true);
        UserRole.createUserRole(unauthorized, unauthorizedRole, "initial", true);

        userRepository.saveAll(Arrays.asList(admin, manager, user, unauthorized));

        캘린더_생성_7_10월();
    }


    private Role saveNewRole(RoleEnum roleEnum) {
        Role role = Role.builder().value(roleEnum).build();
        return roleRepository.save(role);
    }

    private Authority saveNewAuthority(String s) {
        return authorityRepository.save(
            Authority.builder().permission(s).build());
    }

    private void 캘린더_생성_7_10월() {
        //캘린더 데이터 저장
        LocalDate startDate = LocalDate.of(2022, 10, 1);
        LocalDate endDate = LocalDate.of(2022, 10, 18);

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
            loadSecurityData();
    }
}
