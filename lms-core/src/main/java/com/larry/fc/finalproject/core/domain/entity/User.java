package com.larry.fc.finalproject.core.domain.entity;

import com.larry.fc.finalproject.core.domain.util.Encryptor;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDate;


@NoArgsConstructor
@Getter
@Setter
@Table(name = "user")
@Entity
public class User extends BaseEntity{
    private String name;
    private String email;
    private String password;
    private LocalDate bithday;
    private Long attendStatus;

    public User(String name, String email, String password, LocalDate bithday) { // attendStatus = 1 로 고정
        this.name = name;
        this.email = email;
        this.password = password;
        this.bithday = bithday;
        this.attendStatus = 1L;
    }

    public boolean isMatch(Encryptor encryptor, String password) {

        return encryptor.isMatch(password, this.password);
    }

}
