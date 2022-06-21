//package com.savelms;
//

//import java.util.Optional;
//import java.util.Set;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//@Service
//@RequiredArgsConstructor
//@Slf4j
//public class UserService {
//    private final BCryptPasswordEncoder bCryptPasswordEncoder;
//    private final Encryptor encryptor;
//    private final UserRepository userRepository;
//    private final UserInfoRepository userInfoRepository;
//    private final DayTableRepository dayTableRepository;
//    private final TodoRepository todoRepository;
//    private final UserStatisticalChartRepository userStatisticalChartRepository;
//    private final PlusVacationRepository plusVacationRepository;
//    private final RoleRepository roleRepository;
//    private final AuthorityRepository authorityRepository;
//
////    @Transactional
////    public User create(UserCreateReq userCreateReq){
////        userRepository.findByUsername(userCreateReq.getName())
////                .ifPresent(u -> {
////                    throw new RuntimeException("user already exit!");
////                });
////        return userRepository.save(new User(
////                userCreateReq.getName(),
////                userCreateReq.getEmail(),
////                encryptor.encrypt(userCreateReq.getPassword()),
////                userCreateReq.getBirthday()
////        ));
////    }
//
//    @Transactional
//    public Long validateUserNameAndCreate(User user) {
//        validateUsernameDuplicate(user);
//        //첫 회원가입시 유저 상태 저장.
//        setUserDefaultStatus(user);
//        User savedUser = userRepository.save(user);
//        return savedUser.getId();
//    }
//
//    private void setUserDefaultStatus(User user) {
//        Role defaultRole = roleRepository.findByName(RoleEnum.UNAUTHORIZED.name())
//            .orElseThrow(IllegalStateException::new);
//        user.getRoles().add(defaultRole);
//        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
//        user.setNickName(user.getUsername());
//        user.setAttendStatus(1L);
//    }
//
//    private void validateUsernameDuplicate(User user) {
//        userRepository.findByUsername(user.getUsername())
//            .ifPresent(u -> {
//                    throw new RuntimeException("Id : "+ u.getUsername()+"는 이미 사용중입니다.");
//                });
//    }
//
//
//    @Transactional
//    public Optional<User> findPwMatchUser(String email, String password) {
//        return userRepository.findByUsername(email)
//        //        .map(user -> user.getPassword().equals(password) ? user : null);
//                .map(user -> user.isMatch(encryptor, password) ? user : null);
//    }
//
//    @Transactional
//    public User findByUserId(Long userId){
//        return userRepository.findById(userId)
//                .orElseThrow(() -> new RuntimeException("no user by id"));
//    }
//
//    @Transactional
//    public UserInfo findByUserInfoId(Long userId){
//        return userInfoRepository.findByWriter_Id(userId)
//                .orElseThrow(() -> new RuntimeException("no user by id"));
//    }
//
//    @Transactional
//    public void delete(Long userId){
//        try{
//           userInfoRepository.deleteUserInfoByWriter_Id(userId);
//            dayTableRepository.deleteDayTableByCadet_Id(userId);
//            todoRepository.deleteTodoByWriter_Id(userId);
//            userStatisticalChartRepository.deleteStatisticalChartByWriter_Id(userId);
//            plusVacationRepository.deletePlusVacationByUserId(userId);
//            userRepository.deleteById(userId);
//        } catch (Exception e){
//            log.error("error deleting entity Todo", userId, e);
//            throw new RuntimeException("error deleting entity Todo " + userId);
//        }
//    }
//}
