package com.larry.fc.finalproject.api.scheduler;

import com.larry.fc.finalproject.api.controller.useradmincontroller.AllUserShowController;
import com.larry.fc.finalproject.api.controller.useradmincontroller.UserInfoController;
import com.larry.fc.finalproject.api.service.userservice.AllUserTableService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;


@RequiredArgsConstructor
@Component
public class MakeMonthScheduler {
    private final UserInfoController userInfoController;
    private final AllUserTableService allUserTableService;
    //@Scheduled(cron = "0 0/1 0 * * *")
    @Scheduled(cron = "0 1 0 * * *", zone = "Asia/Seoul")
    @Transactional
    public void makeTable() {
        allUserTableService.createAllDate1();
    }

    // userinfo
    //출결 상태(2.5) - attendScore - / 매달 초기화
    //- **+ 출석 횟수? (number) - / 매달 초기화**
    //- **+ 아오지 탕감 시간? (number) - / 매달 초기화**
    @Scheduled(cron = "0 0 0 1 * *", zone = "Asia/Seoul")
    @Transactional
    public void initMonthUserInfo(){
        userInfoController.updateUserMonthInfo();
    }

}
