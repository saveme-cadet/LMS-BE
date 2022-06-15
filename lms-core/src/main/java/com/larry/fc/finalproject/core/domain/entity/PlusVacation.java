package com.larry.fc.finalproject.core.domain.entity;

import com.larry.fc.finalproject.core.domain.entity.user.User;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "plusvacation")
public class PlusVacation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    private LocalDate startAt;
    private LocalDate endAt;
    private boolean finished;

    public static PlusVacation gainServent(User user){
        return PlusVacation.builder()
                .user(user)
                .startAt(LocalDate.now())
                .endAt(LocalDate.now())
                .finished(true)
                .build();
    }
}
