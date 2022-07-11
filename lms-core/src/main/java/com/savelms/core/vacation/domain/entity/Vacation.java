package com.savelms.core.vacation.domain.entity;

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
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Table(name = "VACATION")
@Entity
@Builder
public class Vacation extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="VACATION_ID")
    private Long id;

    @Column(nullable = false)
    private Long remainingDays;

    @Column(nullable = false)
    private Long usedDays;

    private String reason;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="USER_ID", nullable = false, updatable = false)
    private User user;


    /**
     * 생성 메서드
     * */
    public static Vacation createVacation(Long remainingDays, Long usedDays, String reason, User user) {
        return Vacation.builder()
                .remainingDays(remainingDays)
                .usedDays(usedDays)
                .reason(reason)
                .user(user)
                .build();
    }


    /**
     * 비즈니스 로직
     * */
    public void addVacationDays(Long vacationDays) {
        this.remainingDays += vacationDays;
    }

    public void useVacationDays(Long usedDays, String reason) {
        if ((this.remainingDays - usedDays) < 0) {
            throw new IllegalArgumentException("사용할 휴가가 부족합니다.");
        }

        this.remainingDays -= usedDays;
        this.usedDays += usedDays;
        this.reason = reason;
    }

    public void updateReason(String reason) {
        this.reason = reason;
    }

}
