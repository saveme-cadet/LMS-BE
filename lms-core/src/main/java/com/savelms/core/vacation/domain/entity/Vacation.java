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

import java.util.Objects;

@Getter
@Builder
@Entity
@Table(name = "VACATION")
@AllArgsConstructor
public class Vacation extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="VACATION_ID")
    private Long id;

    @Column(nullable = false)
    private Double remainingDays;

    @Column(nullable = false)
    private Double addedDays;

    @Column(nullable = false)
    private Double usedDays;

    private String reason;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="USER_ID", nullable = false, updatable = false)
    private User user;

    protected Vacation() {}

    public static Vacation of(Double remainingDays, Double addedDays, Double usedDays, String reason, User user) {
        return Vacation.builder()
                .remainingDays(remainingDays)
                .addedDays(addedDays)
                .usedDays(usedDays)
                .reason(reason)
                .user(user)
                .build();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vacation vacation = (Vacation) o;
        return id != null && id.equals(vacation.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}
