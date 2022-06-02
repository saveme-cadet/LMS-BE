# lmssavecadet

# Save Cadet 구해줘 카뎃 LMS Back-End 

## Skill

<img src="https://img.shields.io/badge/Java-007396?style=flat-square&logo=Java&logoColor=white"/> <img src="https://img.shields.io/badge/Spring-6DB33F?style=flat-square&logo=Spring&logoColor=white"/> <img src="https://img.shields.io/badge/Spring Boot-6DB33F?style=flat-square&logo=Spring Boot&logoColor=white"/> <img src="https://img.shields.io/badge/Spring Security-6DB33F?style=flat-square&logo=Spring Security&logoColor=white"/> <img src="https://img.shields.io/badge/AWS-232F3E?style=flat-square&logo=Amazon AWS&logoColor=white"/> <img src="https://img.shields.io/badge/MySQL-4479A1?style=flat-square&logo=MySQL&logoColor=white"/> 

## tools

<img src="https://img.shields.io/badge/Intellij-000000?style=flat-square&logo=Intellij IDEA&logoColor=white"/> <img src="https://img.shields.io/badge/GitHub-181717?style=flat-square&logo=GitHub&logoColor=white"/>


# LMS-API Directory tree
```
.\
├── ApiApplication.java\
├── advice\
│   └── GlobalControllerAdvice.java\
├── config\
│   ├── AuthUserResolver.java\
│   ├── ConnectorConfig.java\
│   ├── DevWebConfig.java\
│   ├── FakeAuthUserResolver.java\
│   └── WebConfig.java\
├── controller\
│   ├── AllInfoController.java\
│   ├── LoginApiController.java\
│   ├── TodoController.java\
│   ├── aojicontroller\
│   │   └── AojiTimeController.java\
│   ├── useradmincontroller\
│   │   ├── AllUserShowController.java\
│   │   ├── UserInfoController.java\
│   │   └── UserStatisticalChartController.java\
│   ├── userentitycontroller\
│   │   └── UserCreateDeleteController.java\
│   └── utilcontroller\
│       ├── IndexController.java\
│       └── VacationDayController.java\
├── dto\
│   ├── AllUserDto.java\
│   ├── AuthUser.java\
│   ├── DayTableDto.java\
│   ├── LoginReq.java\
│   ├── PlusVacationDto.java\
│   ├── ResponseDto.java\
│   ├── SignUpReq.java\
│   ├── aojidto\
│   │   ├── AojiDto.java\
│   │   ├── AojiResponseDto.java\
│   │   └── AojiUserDto.java\
│   ├── error\
│   │   ├── Error.java\
│   │   └── ErrorResponse.java\
│   ├── tabledto\
│   │   ├── AllTableDto.java\
│   │   ├── AllUserTableDto.java\
│   │   ├── TableCheckInDto.java\
│   │   └── TableCheckOutDto.java\
│   ├── tododto\
│   │   ├── AllUserTodoDto.java\
│   │   ├── DeleteTodoDto.java\
│   │   ├── RequestTodoDto.java\
│   │   └── TodoDto.java\
│   ├── userdto\
│   │   ├── UserAttendenceDto.java\
│   │   ├── UserDto.java\
│   │   └── UserTeamAndRoleDto.java\
│   ├── userinfodto\
│   │   ├── AllUserInfoDto.java\
│   │   ├── UserInfoDayDto.java\
│   │   ├── UserInfoDto.java\
│   │   ├── UserInfoMonthDto.java\
│   │   ├── UserInfoWeekDto.java\
│   │   ├── UserRoleChangeDto.java\
│   │   ├── UserTeamChangeDto.java\
│   │   └── UserVacationChangeDto.java\
│   └── userstatisticalchartdto\
│       ├── AllObjectDto.java\
│       ├── AttendDto.java\
│       └── ObjectDto.java\
├── filter\
│   └── CorsFilter.java\
├── scheduler\
│   └── MakeMonthScheduler.java\
├── service\
│   ├── LoginService.java\
│   ├── UserStatisticalChartService.java\
│   ├── aojiservice\
│   │   ├── AojiQuertService.java\
│   │   └── AojiService.java\
│   ├── todoservice\
│   │   ├── TodoQueryService.java\
│   │   └── TodoService.java\
│   ├── userservice\
│   │   ├── AllUserTableQueryService.java\
│   │   ├── AllUserTableService.java\
│   │   ├── UserInfoQueryService.java\
│   │   ├── UserInfoService.java\
│   │   └── UserQueryService.java\
│   └── utilservice\
│       └── PlusVacationService.java\
└── util\
├── AojiDtoConverter.java\
├── DtoConverter.java\
├── PercentMesure.java\
└── UserDtoConverter.java
```
# LMS-CORE Directory tree
```
.\
├── Check.java\
├── InfoType.java\
├── RequestStatus.java\
├── ScheduleType.java\
├── UserTableInfo.java\
├── dto\
│   └── UserCreateReq.java\
├── entity\
│   ├── Announcement.java\
│   ├── BaseEntity.java\
│   ├── Comment.java\
│   ├── DayTable.java\
│   ├── NoticeBoard.java\
│   ├── PlusVacation.java\
│   ├── StatisticalChart.java\
│   ├── StudyTime.java\
│   ├── Todo.java\
│   ├── User.java\
│   ├── UserInfo.java\
│   └── repository\
│       ├── AnnouncementRepository.java\
│       ├── AojiIdMapping.java\
│       ├── CheckMapping.java\
│       ├── CommentRepository.java\
│       ├── DayMapping.java\
│       ├── DayTableRepository.java\
│       ├── IdMapping.java\
│       ├── NoticeBoardRepository.java\
│       ├── PlusVacationRepository.java\
│       ├── RoleAndTeamMapping.java\
│       ├── StudyTimeRepository.java\
│       ├── TodoRepository.java\
│       ├── UserInfoRepository.java\
│       ├── UserRepository.java\
│       └── UserStatisticalChartRepository.java\
├── service\
│   └── UserService.java\
└── util\
├── BCryptEncryptor.java\
├── Encryptor.java\
└── Period.java
```

# Description

- 구해줘 카뎃 동아리의 Learning Management System 입니다.
- 기존의 Numbers로 진행한 출결 관리, Todo List, 아오지 탄광 기능을 수정 및 디자인하였습니다.
- 인원교체가 빈번하게 발생하면서 머슴의 일이 복잡해졌기에 머슴의 기능을 최소화 하는 측면에서 만들기 시작했습니다.

### Team Project Notion
___Link___: [Notion][https://www.notion.so/savemecadet/LMS-24781856e3e5429490b8b88e0a1b8af8]

* * *

## 기존의 Numbers 출결 관리 시스템

![스크린샷 2022-02-19 오전 8 04 58](https://user-images.githubusercontent.com/56079997/154772720-688fee97-d235-4576-82e5-f3b64586a71d.png)

## 문제점
### 달마다 인원추가가 있어서 Numbers를 전체적으로 수정을 해줘야 했습니다.
### UI 측면에서 인원이 늘어남에 따라 가로로 추가되어 보기 어려워 졌습니다. 
### 2주마다 팀이 변경되면서 인원 이동에 따라 팀 수정을 해야하는 불편한 점이 있었습니다.


* * *
## 초기 Figma - 기본 디자인

![스크린샷 2022-02-19 오전 8 15 30](https://user-images.githubusercontent.com/56079997/154773501-273fbbf5-52cd-472e-82e5-94709c560c6c.png)
![스크린샷 2022-02-19 오전 8 15 35](https://user-images.githubusercontent.com/56079997/154773545-cc5d5fa7-2458-4618-94d9-8e9e4461f1ce.png)


## 수정 Figma - by dhyeon
### PC .ver
<img width="762" alt="Screen Shot 2022-03-24 at 3 29 45 PM" src="https://user-images.githubusercontent.com/56079997/159855928-772d7868-84e0-48e1-bf72-28b8d5e865d3.png">

### Mobile .ver
<img width="831" alt="Screen Shot 2022-03-24 at 3 30 21 PM" src="https://user-images.githubusercontent.com/56079997/159856022-5c4180f5-5b9d-416c-84be-1d8a3a335856.png">


* * *
## 문제 해결을 위한 기능 추가

### 출결 관리
- 머슴 관리 페이지와 출석표를 통해서 개인 수정 및 머슴 수정으로 수정 관리를 분리 하였습니다.
- 출석 외 
  - 지각 +0.25 출결 점수 추가
  - 결석 +0.5 출결 점수 추가
  - 병결 '.'
  - 휴가 '.'
  - 출석 label에 따라 출결 점수 차등 추가 하도록 하였습니다.
- 출석 점수와 출결 점수를 구분 하여 참여한 달에 출석 우수자를 선정 할 수 있도록 하였습니다.

### ToDo List
- 학습 시간에 무엇을 할지 작성하는 ToDo List에서 달성 점수를 추가하였습니다.
- 달성 Percentage 를 주어서 학습 동기를 부여하였습니다.

### 아오지 탄광
- 평일 외에 공부 학습 시간을 통해 출석 점수를 낮출 수 있는 시스템을 구현하였습니다.
- 평일 외에 학습 시간에서 동료들과 함께 공부한다는 느낌을 주기위해 아오지 학습을 진행중인 동료들을 표기를 해주었습니다.
- 아오지 탄광은 매일 오전 7시에 초기화 되며 아오지 탄광시 추가된 학습은 출결 점수에 반영이 되도록 하였으나 출석 점수에는 반영하지 않도록 하였습니다.
  


* * *
# Environment

- AWS - Docker
- __FrontEnd 기술 스택__  : React, java script
- __BackEnd 기술 스택__ : Spring, java

## 팀 원

- ___Porject Design Plan___ : Taeskim
- ___UI Design___ : Dhyeon
- ___FrontEnd___ : Sham, Chanhyle
- ___BackEnd___ : sjin

