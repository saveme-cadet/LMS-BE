# lmssavecadet

## Save Cadet 구해줘 카뎃 LMS Back-End 2차


## Description

- 구해줘 카뎃 동아리의 Learning Management System 입니다.
- 실 사용자들의 feedback을 바탕으로 기능을 수정, 추가 하였습니다.



### Team Project Notion

___Link___: [Swagger] http://3.38.226.166:8080/swagger-ui/index.html?configUrl=/api-docs/swagger-config


* * *

## 문제점
- login을 할때 uuid값이 아닌 db index 값을 그대로 넘겨 줬기때문에 보안이 취약했습니다.
- admin계정이랑 머슴계정이랑 역할이 분리 되지 않았기 때문에 머슴의 비중이 높았습니다.
- 출석표에 column이 많았기 때문에 시각적으로 복잡해 보였습니다.
- 아오지 시간(할당 된 공부시간 외에 공부 시간)의 기록이 하루 지나면 없어지기 때문에 log를 볼 수 없어서 얼마 공부한지 알 수 없는 불편함이 있었습니다.
- 아오지 시간이 잘못 입력되면 수정을 하지 못해서 수정할 수 없는 불편함이 있었습니다.

## 문제 해결을 위한 기능 추가
***
### 출결 관리
- 일주일 동안 총 6번의 출결이 빠지면 위험한 유저로 등록될 수 있도록 Admin page, 출결 page에서 볼 수 있도록 기능을 추가했습니다.

### 아오지 탄광
- 공부 기록을 점수로만 기록 하도록 하였으나 실 유저들이 자신들이 언제 공부한지 알고 싶어했기에 batch로 data를 지우는 기능을 삭제했습니다.
- 같이 공부 할 수 있는 느낌이 들도록 아오지 탄광을 이용하는 유저를 확인 할 수 있는 기능을 추가했습니다.  
- 실수로 아오지 탄광 타이머를 끄지 못하거나, 시간 기록이 잘못 되었을때 점수가 잘못 올라갈 수 있기 때문에 수정기능, 삭제기능을 추가했습니다.

### Admin Page
- UUID를 통해서 보안의 취약점을 보완했습니다.
- 역할을 4가지로 분리했습니다. (Admin, Manager, User, Guest)



* * *
# Environment

- AWS - Ec2, S3, CloudFront
- __FrontEnd 기술 스택__ : React, java script
- __BackEnd 기술 스택__ : Spring Boot, Spring Security, Spring Batch, java


* * *
### PC .ver
<img width="762" alt="Screen Shot 2022-03-24 at 3 29 45 PM" src="https://user-images.githubusercontent.com/56079997/159855928-772d7868-84e0-48e1-bf72-28b8d5e865d3.png">


## 팀 원

- ___FrontEnd___ : Sham, Chanhyle, Mosong
- ___BackEnd___ : sjin, kyunkim, takim
