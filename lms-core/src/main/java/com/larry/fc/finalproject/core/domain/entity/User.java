package com.larry.fc.finalproject.core.domain.entity;

import com.larry.fc.finalproject.core.domain.util.Encryptor;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


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
    @OneToMany(mappedBy = "cadet",cascade = {CascadeType.REMOVE}, orphanRemoval = true)
    private List<DayTable> dayTableList = new ArrayList<>();
    @OneToMany(mappedBy = "writer", cascade = {CascadeType.REMOVE}, orphanRemoval = true)
    private List<UserInfo> userInfoList = new ArrayList<>();
    @OneToMany(mappedBy = "writer", cascade = {CascadeType.REMOVE}, orphanRemoval = true)
    private List<Todo> todoList = new ArrayList<>();
    @OneToMany(mappedBy = "user", cascade = {CascadeType.REMOVE}, orphanRemoval = true)
    private List<PlusVacation> plusVacationList = new ArrayList<>();


    @OneToMany(mappedBy = "writer", cascade = {CascadeType.REMOVE}, orphanRemoval = true)
    private List<StatisticalChart> statisticalCharts = new ArrayList<>();


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
