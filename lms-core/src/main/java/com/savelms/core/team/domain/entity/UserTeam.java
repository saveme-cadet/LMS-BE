package com.savelms.core.team.domain.entity;

import com.savelms.core.BaseEntity;
import com.savelms.core.user.domain.entity.User;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "USER_TEAM")
@Entity
@Builder(access = AccessLevel.PRIVATE)
public class UserTeam extends BaseEntity {

    //********************************* static final 상수 필드 *********************************/


    /********************************* PK 필드 *********************************/

    /**
     * 기본 키
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "USER_TEAM_ID")
    private Long id;

    /********************************* PK가 아닌 필드 *********************************/

    private String reason;

    @Column(nullable = false)
    private Boolean currentlyUsed;

    /********************************* 비영속 필드 *********************************/


    /********************************* 연관관계 매핑 *********************************/

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="TEAM_ID", nullable = false)
    private Team team;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="USER_ID", nullable = false)
    private User user;

    public void setUserAndAddUserTeamToUser(User user) {
        this.user = user;
        user.getUserTeams().add(this);
    }

    /********************************* 생성메서드 *********************************/

    public static UserTeam createUserTeam(User user, Team team, String reason, Boolean currentlyUsed) {
        UserTeam userTeam = UserTeam.builder()
                .team(team)
                .reason(reason)
                .currentlyUsed(currentlyUsed)
                .build();
        userTeam.setUserAndAddUserTeamToUser(user);
        return userTeam;
    }


    /********************************* 비니지스 로직 *********************************/
    public void notCurrentlyUsed() {
        this.currentlyUsed = false;
    }






}
