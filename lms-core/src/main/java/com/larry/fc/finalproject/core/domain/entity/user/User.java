package com.larry.fc.finalproject.core.domain.entity.user;

import com.larry.fc.finalproject.core.domain.entity.BaseEntity;
import com.larry.fc.finalproject.core.domain.entity.PlusVacation;
import com.larry.fc.finalproject.core.domain.entity.StatisticalChart;
import com.larry.fc.finalproject.core.domain.entity.StudyTime;
import com.larry.fc.finalproject.core.domain.entity.Todo;
import com.larry.fc.finalproject.core.domain.entity.UserInfo;
import com.larry.fc.finalproject.core.domain.util.Encryptor;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "user")
@Entity
@Builder
public class User extends BaseEntity {
    private String username;
    private String password;

    @Singular
    @ManyToMany(cascade = {CascadeType.MERGE})
    @JoinTable(name = "user_role",
        joinColumns = {@JoinColumn(name = "USER_ID", referencedColumnName = "ID")},
        inverseJoinColumns = {@JoinColumn(name = "ROLE_ID", referencedColumnName = "ID")})
    private Set<Role> roles;

    @Transient
    private Set<Authority> authorities;

    public Set<Authority> getAuthorities() {
        return roles.stream()
            .map((role)->
                role.getAuthorities())
            .flatMap(Set::stream)
            .collect(Collectors.toSet());
    }

    @Builder.Default
    private Boolean accountNonExpired = true;
    @Builder.Default
    private Boolean accountNonLocked = true;
    @Builder.Default
    private Boolean credentialsNonExpired = true;
    @Builder.Default
    private Boolean enabled = true;

    private String email;
    private Long attendStatus;

   // @OneToMany(mappedBy = "cadet",cascade = {CascadeType.REMOVE}, orphanRemoval = true)
    //private List<DayTable> dayTableList = new ArrayList<>();
    @OneToMany(mappedBy = "writer", cascade = {CascadeType.REMOVE}, orphanRemoval = true)
    private List<UserInfo> userInfoList = new ArrayList<>();
    @OneToMany(mappedBy = "writer", cascade = {CascadeType.REMOVE}, orphanRemoval = true)
    private List<Todo> todoList = new ArrayList<>();
    @OneToMany(mappedBy = "user", cascade = {CascadeType.REMOVE}, orphanRemoval = true)
    private List<PlusVacation> plusVacationList = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = {CascadeType.ALL}, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<StudyTime> studyTimes = new ArrayList<>();

    @OneToMany(mappedBy = "writer", cascade = {CascadeType.REMOVE}, orphanRemoval = true)
    private List<StatisticalChart> statisticalCharts = new ArrayList<>();


    public User(String username, String email, String password, LocalDate bithday) { // attendStatus = 1 로 고정
        this.username = username;
        this.email = email;
        this.password = password;
        this.attendStatus = 1L;
    }

    public boolean isMatch(Encryptor encryptor, String password) {
        return encryptor.isMatch(password, this.password);
    }

}
